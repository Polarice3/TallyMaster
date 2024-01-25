package com.Polarice3.TallyMaster;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = TallyMaster.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TallyConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue TALLY_MILESTONE = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .defineInRange("tallyMilestone", 25, 1, Integer.MAX_VALUE);


    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int tallyMilestone;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        tallyMilestone = TALLY_MILESTONE.get();
    }
}
