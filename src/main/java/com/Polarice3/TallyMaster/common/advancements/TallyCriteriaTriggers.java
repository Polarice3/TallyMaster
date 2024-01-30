package com.Polarice3.TallyMaster.common.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class TallyCriteriaTriggers {
    public static void init() {
        CriteriaTriggers.register(TallyTrigger.INSTANCE);
    }
}
