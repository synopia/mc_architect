package de.funky_clan.mc.net;

import com.google.inject.Singleton;
import de.funky_clan.mc.net.packets.*;

/**
 * @author synopia
 */
@Singleton
public class MinecraftServer extends MinecraftNetworkEventBus {
    public MinecraftServer() {
        addPacketType(BlockMultiUpdate.ID, BlockMultiUpdate.class);
        addPacketType(BlockUpdate.ID,      BlockUpdate.class);
        addPacketType(ChunkData.ID,        ChunkData.class);
        addPacketType(ChunkPreparation.ID, ChunkPreparation.class);
    }
}
