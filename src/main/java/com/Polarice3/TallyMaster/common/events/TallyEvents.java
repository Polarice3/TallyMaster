package com.Polarice3.TallyMaster.common.events;

import com.Polarice3.TallyMaster.TallyConfig;
import com.Polarice3.TallyMaster.TallyMaster;
import com.Polarice3.TallyMaster.common.advancements.TallyTrigger;
import com.Polarice3.TallyMaster.common.capabilities.tally.ITally;
import com.Polarice3.TallyMaster.common.capabilities.tally.TallyProvider;
import com.Polarice3.TallyMaster.util.TallyHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TallyMaster.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TallyEvents {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        Player original = event.getOriginal();

        original.reviveCaps();

        ITally tally = TallyHelper.getCapability(original);

        player.getCapability(TallyProvider.CAPABILITY)
                .ifPresent(tally1 -> {
                    for (EntityType<?> entityType : tally.tallyList().keySet()){
                        tally1.setTally(entityType, tally.tallyList().get(entityType));
                    }
                });
    }

    @SubscribeEvent
    public static void onPlayerFirstEntersWorld(PlayerEvent.PlayerLoggedInEvent event){
        Player player = event.getEntity();

        if (player.level instanceof ServerLevel) {
            if (player instanceof ServerPlayer serverPlayer) {
                for (EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES) {
                    int statsKilled = serverPlayer.getStats().getValue(Stats.ENTITY_KILLED.get(entityType));
                    int killAmount = TallyHelper.getKillAmount(serverPlayer, entityType);
                    if (statsKilled > 0 && statsKilled > killAmount) {
                        TallyHelper.setTally(serverPlayer, entityType, statsKilled);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void LivingHurt(LivingHurtEvent event){
        Entity entity = event.getSource().getEntity();
        LivingEntity target = event.getEntity();
        if (TallyConfig.tallyAttack > 0) {
            if (entity instanceof Player player) {
                int killAmount = TallyHelper.getKillAmount(player, target.getType());
                int postKill = killAmount / TallyConfig.tallyMilestone;
                float percent = TallyConfig.tallyAttack / 100.0F;
                if (TallyConfig.tallyAttackLimit > 0) {
                    float limit = TallyConfig.tallyAttackLimit / 100.0F;
                    percent = Math.min(percent, limit);
                }
                float prof = (percent * postKill) + 1.0F;
                event.setAmount(event.getAmount() * prof);
            }
        }
        if (TallyConfig.tallyDefense > 0) {
            if (target instanceof Player player) {
                if (entity instanceof LivingEntity livingSource) {
                    int killAmount = TallyHelper.getKillAmount(player, livingSource.getType());
                    int postKill = killAmount / TallyConfig.tallyMilestone;
                    float percent = TallyConfig.tallyDefense / 100.0F;
                    if (TallyConfig.tallyDefenseLimit > 0) {
                        float limit = TallyConfig.tallyDefenseLimit / 100.0F;
                        percent = Math.min(percent, limit);
                    }
                    float prof = percent * postKill;
                    float finalAmount = Math.min(event.getAmount() * prof, event.getAmount() - 1.0F);
                    event.setAmount(event.getAmount() - finalAmount);
                }
            }
        }
    }

    @SubscribeEvent
    public static void LivingDeath(LivingDeathEvent event){
        Entity entity = event.getSource().getEntity();
        LivingEntity target = event.getEntity();
        if (entity instanceof LivingEntity){
            Player player = null;
            if (entity instanceof Player player1){
                player = player1;
            } else if (TallyConfig.petTally && entity instanceof OwnableEntity ownable && ownable.getOwner() instanceof Player player1){
                player = player1;
            }
            if (player != null){
                int killAmount = TallyHelper.getKillAmount(player, target.getType());
                if (killAmount > 1 && TallyConfig.milestoneChat) {
                    if (killAmount % TallyConfig.tallyMilestone == 0) {
                        player.displayClientMessage(Component.translatable("info.tally_master.tally.milestone", player.getDisplayName(), killAmount, target.getType().getDescription()), false);
                    }
                    if (TallyConfig.tallyLooting){
                        int looting = Mth.clamp(killAmount / TallyConfig.tallyLootingAmount, 0, TallyConfig.tallyMaxLootingLevel);
                        if (killAmount % (TallyConfig.tallyLootingAmount) == 0 && looting > 0) {
                            if (looting == TallyConfig.tallyMaxLootingLevel){
                                player.displayClientMessage(Component.translatable("info.tally_master.tally.lootingMax", player.getDisplayName(), target.getType().getDescription()), false);
                            } else if (looting < TallyConfig.tallyMaxLootingLevel) {
                                player.displayClientMessage(Component.translatable("info.tally_master.tally.looting", player.getDisplayName(), target.getType().getDescription(), looting), false);
                            }
                        }
                    }
                }
                TallyHelper.increaseTally(player, target.getType());
                if (player instanceof ServerPlayer serverPlayer){
                    TallyTrigger.INSTANCE.trigger(serverPlayer, target, killAmount + 1);
                }
            }
        }
    }

    @SubscribeEvent
    public static void LootingEvent(LootingLevelEvent event){
        LivingEntity target = event.getEntity();
        if (TallyConfig.tallyLooting) {
            if (event.getDamageSource() != null) {
                Entity entity = event.getDamageSource().getEntity();
                if (entity instanceof LivingEntity) {
                    Player player = null;
                    if (entity instanceof Player player1) {
                        player = player1;
                    } else if (TallyConfig.petTally && entity instanceof OwnableEntity ownable && ownable.getOwner() instanceof Player player1) {
                        player = player1;
                    }
                    if (player != null) {
                        int killAmount = TallyHelper.getKillAmount(player, target.getType());
                        if (killAmount > 1) {
                            int looting = Mth.clamp(killAmount / TallyConfig.tallyLootingAmount, 0, TallyConfig.tallyMaxLootingLevel);
                            event.setLootingLevel(event.getLootingLevel() + looting);
                        }
                    }
                }
            }
        }
    }
}
