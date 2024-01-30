package com.Polarice3.TallyMaster.common.events;

import com.Polarice3.TallyMaster.TallyConfig;
import com.Polarice3.TallyMaster.TallyMaster;
import com.Polarice3.TallyMaster.common.advancements.TallyTrigger;
import com.Polarice3.TallyMaster.common.capabilities.tally.ITally;
import com.Polarice3.TallyMaster.common.capabilities.tally.TallyProvider;
import com.Polarice3.TallyMaster.util.TallyHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
    public static void LivingHurt(LivingHurtEvent event){
        Entity entity = event.getSource().getEntity();
        LivingEntity target = event.getEntity();
        if (entity instanceof Player player){
            int killAmount = TallyHelper.getKillAmount(player, target.getType());
            int postKill = killAmount / TallyConfig.tallyMilestone;
            float prof = (0.05F * postKill) + 1.0F;
            event.setAmount(event.getAmount() * prof);
        }
        if (target instanceof Player player){
            if (entity instanceof LivingEntity livingSource) {
                int killAmount = TallyHelper.getKillAmount(player, livingSource.getType());
                int postKill = killAmount / TallyConfig.tallyMilestone;
                float prof = 0.05F * postKill;
                float finalAmount = Math.min(event.getAmount() * prof, event.getAmount() - 1.0F);
                event.setAmount(event.getAmount() - finalAmount);
            }
        }
    }

    @SubscribeEvent
    public static void LivingDeath(LivingDeathEvent event){
        Entity entity = event.getSource().getEntity();
        LivingEntity target = event.getEntity();
        if (entity instanceof LivingEntity){
            if (entity instanceof Player player){
                int killAmount = TallyHelper.getKillAmount(player, target.getType());
                if (killAmount > 1 && killAmount % TallyConfig.tallyMilestone == 0){
                    player.displayClientMessage(Component.translatable("info.tally_master.tally.milestone", player.getDisplayName(), killAmount, target.getType().getDescription()), false);
                }
                TallyHelper.increaseTally(player, target.getType());
                if (player instanceof ServerPlayer serverPlayer){
                    TallyTrigger.INSTANCE.trigger(serverPlayer, target, killAmount + 1);
                }
            }
        }
    }
}
