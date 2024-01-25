package com.Polarice3.TallyMaster;

import com.Polarice3.TallyMaster.client.ClientProxy;
import com.Polarice3.TallyMaster.common.CommonProxy;
import com.Polarice3.TallyMaster.common.network.ModNetwork;
import com.Polarice3.TallyMaster.init.ModProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TallyMaster.MOD_ID)
public class TallyMaster {
    public static final String MOD_ID = "tally_master";
    public static ModProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public TallyMaster()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TallyConfig.SPEC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModNetwork.init();
    }
}
