package de.funky_clan.mc.net;

import com.google.inject.Singleton;
import de.funky_clan.mc.net.packets.*;

/**
 * @author synopia
 */
@Singleton
public class MinecraftClient extends MinecraftNetworkEventBus {
    public MinecraftClient() {
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
