package com.Polarice3.TallyMaster.common.capabilities.tally;

import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class TallyImp implements ITally {
    public Map<EntityType<?>, Integer> tallyList = new HashMap<>();

    @Override
    public Map<EntityType<?>, Integer> tallyList() {
        return this.tallyList;
    }

    @Override
    public void addTally(EntityType<?> entityType) {
        if (!this.tallyList.containsKey(entityType)){
            this.tallyList.put(entityType, 1);
        } else {
            this.tallyList.put(entityType, this.tallyList.get(entityType) + 1);
        }
    }

    @Override
    public void removeTally(EntityType<?> entityType) {
        this.tallyList.remove(entityType);
    }

    @Override
    public void setTally(EntityType<?> entityType, int amount) {
        this.tallyList.put(entityType, amount);
    }
}
