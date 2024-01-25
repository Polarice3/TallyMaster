package com.Polarice3.TallyMaster.util;

import com.Polarice3.TallyMaster.common.capabilities.tally.ITally;
import com.Polarice3.TallyMaster.common.capabilities.tally.TallyImp;
import com.Polarice3.TallyMaster.common.capabilities.tally.TallyProvider;
import com.Polarice3.TallyMaster.common.capabilities.tally.TallyUpdatePacket;
import com.Polarice3.TallyMaster.common.network.ModNetwork;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class TallyHelper {
    public static ITally getCapability(Player player) {
        return player.getCapability(TallyProvider.CAPABILITY).orElse(new TallyImp());
    }

    public static void increaseTally(Player player, EntityType<?> entityType){
        getCapability(player).addTally(entityType);
        sendTallyUpdatePacket(player);
    }

    public static int getKillAmount(Player player, EntityType<?> entityType){
        if (getCapability(player).tallyList().containsKey(entityType)){
            return getCapability(player).tallyList().get(entityType);
        }
        return 0;
    }

    public static void sendTallyUpdatePacket(Player player) {
        if (!player.getLevel().isClientSide()) {
            ModNetwork.sendTo(player, new TallyUpdatePacket(player));
        }
    }
}
