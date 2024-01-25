package com.Polarice3.TallyMaster.init;

import com.Polarice3.TallyMaster.TallyMaster;
import com.Polarice3.TallyMaster.common.capabilities.tally.TallyProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TallyMaster.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InitEvents {

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(TallyMaster.MOD_ID, "tally"), new TallyProvider());
        }
    }
}
