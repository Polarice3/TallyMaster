package com.Polarice3.TallyMaster.common.capabilities.tally;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TallyProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    public static Capability<ITally> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    ITally instance = new TallyImp();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CAPABILITY ? LazyOptional.of(() -> (T) instance) : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return save(new CompoundTag(), instance);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        load(nbt, instance);
    }

    public static CompoundTag save(CompoundTag tag, ITally tally) {
        if (tally.tallyList() != null){
            ListTag typeList = new ListTag();
            if (!tally.tallyList().isEmpty()) {
                for (EntityType<?> entityType : tally.tallyList().keySet()){
                    CompoundTag compoundTag = new CompoundTag();
                    compoundTag.putString("id", EntityType.getKey(entityType).toString());
                    compoundTag.putInt("amount", tally.tallyList().get(entityType));
                    typeList.add(compoundTag);
                }
                tag.put("typeList", typeList);
            }
        }
        return tag;
    }

    public static ITally load(CompoundTag tag, ITally tally) {
        if (tag.contains("typeList", Tag.TAG_LIST)) {
            ListTag listtag = tag.getList("typeList", Tag.TAG_COMPOUND);
            for (int i = 0; i < listtag.size(); ++i) {
                String string = listtag.getCompound(i).getString("id");
                int amount = listtag.getCompound(i).getInt("amount");
                if (EntityType.byString(string).isPresent()){
                    tally.setTally(EntityType.byString(string).get(), amount);
                }
            }
        }
        return tally;
    }
}
