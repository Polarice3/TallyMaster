package com.Polarice3.TallyMaster;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = TallyMaster.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TallyConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue TALLY_MILESTONE = BUILDER
            .comment("Set the amount of kills to reach a milestone.")
            .defineInRange("tallyMilestone", 25, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue TALLY_ATTACK = BUILDER
            .comment("Percentage increase of player's damage against tallied mob per milestone. Set 0 to disable increase.")
            .defineInRange("tallyAttack", 5, 0, 100);
    private static final ForgeConfigSpec.IntValue TALLY_ATTACK_LIMIT = BUILDER
            .comment("Maximum percentage buff of the player's damage against tallied mob they can attain. Set 0 to have no buff limit.")
            .defineInRange("tallyAttackLimit", 0, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue TALLY_DEFENSE = BUILDER
            .comment("Percentage increase of player's defense against tallied mob per milestone. Set 0 to disable increase.")
            .defineInRange("tallyDefense", 5, 0, 100);
    private static final ForgeConfigSpec.IntValue TALLY_DEFENSE_LIMIT = BUILDER
            .comment("Maximum percentage buff of the player's defense against tallied mob they can attain. Set 0 to have no buff limit.")
            .defineInRange("tallyDefenseLimit", 0, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue TALLY_LOOTING_AMOUNT = BUILDER
            .comment("Only works if tallyLooting is enabled. Defines the amount of kills to increase looting level. Best if value is higher than tallyMilestone.")
            .defineInRange("tallyLootingMilestone", 100, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue TALLY_MAX_LOOTING_LEVEL = BUILDER
            .comment("Only works if tallyLooting is enabled. The maximum looting level the player can attain from looting milestones.")
            .defineInRange("tallyMaxLootingLevel", 3, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.BooleanValue PET_TALLY = BUILDER
            .comment("Whether kills by pets or owned mobs count to player's tally.")
            .define("petTally", true);
    private static final ForgeConfigSpec.BooleanValue TALLY_LOOTING = BUILDER
            .comment("Reaching a certain milestone amount will increase looting against the mob type.")
            .define("tallyLooting", true);
    private static final ForgeConfigSpec.BooleanValue MILESTONE_CHAT = BUILDER
            .comment("Shows in player's chat if they have reached a milestone")
            .define("milestoneChat", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int tallyMilestone;
    public static int tallyAttack;
    public static int tallyAttackLimit;
    public static int tallyDefense;
    public static int tallyDefenseLimit;
    public static int tallyLootingAmount;
    public static int tallyMaxLootingLevel;

    public static boolean petTally;
    public static boolean tallyLooting;
    public static boolean milestoneChat;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        tallyMilestone = TALLY_MILESTONE.get();
        tallyAttack = TALLY_ATTACK.get();
        tallyAttackLimit = TALLY_ATTACK_LIMIT.get();
        tallyDefense = TALLY_DEFENSE.get();
        tallyDefenseLimit = TALLY_DEFENSE_LIMIT.get();
        tallyLootingAmount = TALLY_LOOTING_AMOUNT.get();
        tallyMaxLootingLevel = TALLY_MAX_LOOTING_LEVEL.get();

        petTally = PET_TALLY.get();
        tallyLooting = TALLY_LOOTING.get();
        milestoneChat = MILESTONE_CHAT.get();
    }
}
