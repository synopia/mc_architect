package de.funky_clan.mc.net;

import com.google.inject.Singleton;
import de.funky_clan.mc.net.packets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author synopia
 */
@Singleton
public class MinecraftServer extends MinecraftNetworkEventBus {
    private final Logger logger = LoggerFactory.getLogger(MinecraftServer.class);

    public MinecraftServer() {
        logger.info("Starting Minecraft Server Network...");
        addPacketType(BlockMultiUpdate.ID, BlockMultiUpdate.class);
        addPacketType(BlockUpdate.ID,      BlockUpdate.class);
        addPacketType(ChunkData.ID,        ChunkData.class);
        addPacketType(ChunkPreparation.ID, ChunkPreparation.class);
    }
}
