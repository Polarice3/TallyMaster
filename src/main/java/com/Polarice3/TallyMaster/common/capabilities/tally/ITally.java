package com.Polarice3.TallyMaster.common.capabilities.tally;

import net.minecraft.world.entity.EntityType;

import java.util.Map;

public interface ITally {
    Map<EntityType<?>, Integer> tallyList();
    void addTally(EntityType<?> entityType);
    void removeTally(EntityType<?> entityType);
    void setTally(EntityType<?> entityType, int amount);
}
