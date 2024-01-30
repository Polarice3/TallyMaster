package com.Polarice3.TallyMaster.common.advancements;

import com.Polarice3.TallyMaster.TallyMaster;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;

public class TallyTrigger extends SimpleCriterionTrigger<TallyTrigger.TriggerInstance> {
    public static final ResourceLocation ID = TallyMaster.location("tally_milestone");
    public static final TallyTrigger INSTANCE = new TallyTrigger();

    private TallyTrigger() {}

    @Override
    protected TriggerInstance createInstance(JsonObject p_66248_, EntityPredicate.Composite p_66249_, DeserializationContext p_66250_) {
        MinMaxBounds.Ints minmaxbounds$ints = MinMaxBounds.Ints.fromJson(p_66248_.get("amount"));
        return new TallyTrigger.TriggerInstance(p_66249_, EntityPredicate.Composite.fromJson(p_66248_, "entity", p_66250_), minmaxbounds$ints);
    }

    public void trigger(ServerPlayer p_48105_, Entity p_48106_, int amount) {
        LootContext lootcontext = EntityPredicate.createContext(p_48105_, p_48106_);
        this.trigger(p_48105_, (p_48112_) -> {
            return p_48112_.matches(lootcontext, amount);
        });
    }

    @NotNull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final EntityPredicate.Composite entityPredicate;
        private final MinMaxBounds.Ints amount;

        public TriggerInstance(EntityPredicate.Composite p_46890_, EntityPredicate.Composite p_46891_, MinMaxBounds.Ints p_46892_) {
            super(TallyTrigger.ID, p_46890_);
            this.entityPredicate = p_46891_;
            this.amount = p_46892_;
        }

        public boolean matches(LootContext p_48132_, int amount) {
            return this.entityPredicate.matches(p_48132_) && this.amount.matches(amount);
        }

        public JsonObject serializeToJson(SerializationContext p_46896_) {
            JsonObject jsonobject = super.serializeToJson(p_46896_);
            jsonobject.add("entity", this.entityPredicate.toJson(p_46896_));
            jsonobject.add("amount", this.amount.serializeToJson());
            return jsonobject;
        }
    }
}
