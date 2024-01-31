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
    private static final ForgeConfigSpec.IntValue TALLY_LOOTING_AMOUNT = BUILDER
            .comment("Only works if tallyLooting is enabled. Defines the amount of kills to increase looting level. Best if value is higher than tallyMilestone.")
            .defineInRange("tallyLootingAmount", 100, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue TALLY_MAX_LOOTING_LEVEL = BUILDER
            .comment("Only works if tallyLooting is enabled. The maximum looting level the player can attain from looting milestones.")
            .defineInRange("tallyMaxLootingLevel", 3, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.BooleanValue TALLY_DAMAGE = BUILDER
            .comment("Enables increase damage against tallied mobs after reaching milestones.")
            .define("tallyDamage", true);
    private static final ForgeConfigSpec.BooleanValue TALLY_RESISTANCE = BUILDER
            .comment("Enables increase resistance against tallied mobs after reaching milestones.")
            .define("tallyResistance", true);
    private static final ForgeConfigSpec.BooleanValue PET_TALLY = BUILDER
            .comment("Whether kills by pets or owned mobs count to player's tally.")
            .define("petTally", true);
    private static final ForgeConfigSpec.BooleanValue TALLY_LOOTING = BUILDER
            .comment("Reaching a certain milestone amount will increase looting against the mob type.")
            .define("tallyLooting", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int tallyMilestone;
    public static int tallyLootingAmount;
    public static int tallyMaxLootingLevel;

    public static boolean tallyDamage;
    public static boolean tallyResistance;
    public static boolean petTally;
    public static boolean tallyLooting;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        tallyMilestone = TALLY_MILESTONE.get();
        tallyLootingAmount = TALLY_LOOTING_AMOUNT.get();
        tallyMaxLootingLevel = TALLY_MAX_LOOTING_LEVEL.get();

        tallyDamage = TALLY_DAMAGE.get();
        tallyResistance = TALLY_RESISTANCE.get();
        petTally = PET_TALLY.get();
        tallyLooting = TALLY_LOOTING.get();
    }
}
