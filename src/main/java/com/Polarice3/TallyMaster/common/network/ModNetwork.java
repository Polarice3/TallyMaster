package com.Polarice3.TallyMaster.common.network;

import com.Polarice3.TallyMaster.TallyMaster;
import com.Polarice3.TallyMaster.common.capabilities.tally.TallyUpdatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    public static SimpleChannel INSTANCE;
    private static int id = 0;

    public static int nextID() {
        return id++;
    }

    public static void init() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(TallyMaster.MOD_ID, "channel"), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(nextID(), TallyUpdatePacket.class, TallyUpdatePacket::encode, TallyUpdatePacket::decode, TallyUpdatePacket::consume);
    }

    public static <MSG> void sendTo(Player player, MSG msg) {
        ModNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), msg);
    }

    public static <MSG> void sendToServer(MSG msg) {
        ModNetwork.INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sentToTrackingChunk(LevelChunk chunk, MSG msg) {
        ModNetwork.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), msg);
    }

    public static <MSG> void sendToALL(MSG msg) {
        ModNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }
}
