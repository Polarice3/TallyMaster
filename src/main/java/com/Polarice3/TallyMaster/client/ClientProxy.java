package com.Polarice3.TallyMaster.client;

import com.Polarice3.TallyMaster.init.ModProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientProxy implements ModProxy {
    @Override
    public Player getPlayer() {
        return Minecraft.getInstance().player;
    }
}
