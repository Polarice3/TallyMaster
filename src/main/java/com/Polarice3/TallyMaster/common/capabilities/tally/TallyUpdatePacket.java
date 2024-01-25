package com.Polarice3.TallyMaster.common.capabilities.tally;

import com.Polarice3.TallyMaster.TallyMaster;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class TallyUpdatePacket {
    private final UUID PlayerUUID;
    private CompoundTag tag;

    public TallyUpdatePacket(UUID uuid, CompoundTag tag) {
        this.PlayerUUID = uuid;
        this.tag = tag;
    }

    public TallyUpdatePacket(Player player) {
        this.PlayerUUID = player.getUUID();
        player.getCapability(TallyProvider.CAPABILITY, null).ifPresent((soulEnergy) -> {
            this.tag = (CompoundTag) TallyProvider.save(new CompoundTag(), soulEnergy);
        });
    }

    public static void encode(TallyUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.PlayerUUID);
        buffer.writeNbt(packet.tag);
    }

    public static TallyUpdatePacket decode(FriendlyByteBuf buffer) {
        return new TallyUpdatePacket(buffer.readUUID(), buffer.readNbt());
    }

    public static void consume(TallyUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            assert ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT;

            Player player = TallyMaster.PROXY.getPlayer();
            if (player != null) {
                player.getCapability(TallyProvider.CAPABILITY).ifPresent((soulEnergy) -> {
                    TallyProvider.load(packet.tag, soulEnergy);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
