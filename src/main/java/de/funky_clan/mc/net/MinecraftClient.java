package de.funky_clan.mc.net;

import com.google.inject.Singleton;
import de.funky_clan.mc.net.packets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author synopia
 */
@Singleton
public class MinecraftClient extends MinecraftNetworkEventBus {
    private final Logger logger = LoggerFactory.getLogger(MinecraftClient.class);

    public MinecraftClient() {
        logger.info("Starting Minecraft Client Network...");
        addPacketType( Handshake.ID,    Handshake.class );

        addPacketType(EntityAttach.ID,              EntityAttach.class);
        addPacketType(EntityLook.ID,                EntityLook.class);
        addPacketType(EntityRelativeMove.ID,        EntityRelativeMove.class);
        addPacketType(EntityRelativeMoveAndLook.ID, EntityRelativeMoveAndLook.class);
        addPacketType(EntityTeleport.ID,            EntityTeleport.class);
        addPacketType(PlayerLook.ID,                PlayerLook.class);
        addPacketType(PlayerPosition.ID,            PlayerPosition.class);
        addPacketType(PlayerPositionAndLook.ID,     PlayerPositionAndLook.class);
    }


}
