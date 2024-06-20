package gardensofthedead.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class NetworkHandler {

    public static void register() {
        if (Platform.getEnvironment() == Env.CLIENT) {
            NetworkManager.registerReceiver(NetworkManager.s2c(), WhistleEffectPacket.TYPE, WhistleEffectPacket.CODEC, WhistleEffectPacket::apply);
        } else {
            NetworkManager.registerS2CPayloadType(WhistleEffectPacket.TYPE, WhistleEffectPacket.CODEC);
        }
    }

    public static <T extends CustomPacketPayload> void sendToTrackingPlayers(ServerLevel level, BlockPos pos, T message) {
        List<ServerPlayer> players = level.getChunkSource().chunkMap.getPlayers(level.getChunkAt(pos).getPos(), false);
        NetworkManager.sendToPlayers(players, message);
    }
}
