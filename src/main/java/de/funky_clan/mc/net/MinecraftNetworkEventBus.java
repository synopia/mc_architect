package de.funky_clan.mc.net;

import com.google.inject.Inject;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.eventbus.NetworkEvent;
import de.funky_clan.mc.eventbus.NetworkEventBus;
import de.funky_clan.mc.events.network.ConnectionEstablished;
import de.funky_clan.mc.events.network.ConnectionLost;
import de.funky_clan.mc.net.packets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;

/**
 * @author synopia
 */
public abstract class MinecraftNetworkEventBus extends NetworkEventBus {
    private final Logger logger = LoggerFactory.getLogger(MinecraftNetworkEventBus.class);
    private HashMap<Integer, Class<? extends NetworkEvent>> packetTypes = new HashMap<Integer, Class<? extends NetworkEvent>>();
    private DataInputStream in;
    private DataInputStream realIn;
    private DataOutputStream out;
    private boolean connected;

    @Inject
    private MinecraftBinding minecraftBinding;

    @Inject
    private EventDispatcher eventDispatcher;

    protected MinecraftNetworkEventBus() {
        addPacketType( KeepAlive.class );
        addPacketType( LoginRequest.class );
        addPacketType( Handshake.class );
        addPacketType( ChatMessage.class );
        addPacketType( TimeUpdate.class );

        addPacketType( EntityEquipment.class );
        addPacketType( PlayerSpawnPosition.class );

        addPacketType( EntityUse.class );
        addPacketType( UpdateHealth.class );
        addPacketType( PlayerRespawn.class );
        addPacketType( PlayerOnGround.class );
        addPacketType( PlayerPosition.class );
        addPacketType( PlayerLook.class );
        addPacketType( PlayerPositionAndLook.class );

        addPacketType( PlayerDigging.class );
        addPacketType( PlayerBlockPlacement.class );
        addPacketType( PlayerChangeSlot.class );
        addPacketType( PlayerUseBed.class );
        addPacketType( EntityAnimation.class );
        addPacketType( EntityAction.class );
        addPacketType( EntitySpawnNamed.class );
        addPacketType( ItemSpawn.class );
        addPacketType( ItemCollect.class );
        addPacketType( EntityCreated.class );
        addPacketType( EntitySpawn.class );
        addPacketType( EntityPainting.class );
        addPacketType( Packet1B.class );
        addPacketType( EntityVelocity.class );
        addPacketType( EntityDestroy.class );

        addPacketType( EntityUpdate.class );
        addPacketType( EntityRelativeMove.class );
        addPacketType( EntityLook.class );
        addPacketType( EntityRelativeMoveAndLook.class );

        addPacketType( EntityTeleport.class );
        addPacketType( EntityStatus.class );
        addPacketType( EntityAttach.class );
        addPacketType( EntityMetadata.class );

        addPacketType( ChunkPreparation.class);
        addPacketType( ChunkData.class);

        addPacketType( BlockMultiUpdate.class);
        addPacketType( BlockUpdate.class);

        addPacketType( BlockPlayNote.class);
        addPacketType( BlockExplosion.class);

        addPacketType( WindowOpen.class);
        addPacketType( WindowClose.class);
        addPacketType( WindowClick.class);
        addPacketType( WindowMultiSlotUpdate.class);
        addPacketType( WindowSlotUpdate.class);
        addPacketType( WindowUpdateProgressBar.class);
        addPacketType( WindowTransaction.class);
        addPacketType( BlockSignUpdate.class);
        addPacketType( Disconnect.class);
    }

    public synchronized void connect( InputStream in, OutputStream out ) {
        this.in = new DataInputStream( in );
        this.out = new DataOutputStream(out);
        connected = true;
    }

    protected void addPacketType( int packetId, Class<? extends NetworkEvent> cls ) {
        packetTypes.put(packetId, cls);
    }

    protected void addPacketType( Class<? extends NetworkEvent> cls ) {
        int packetId = -1;
        try {
            packetId = cls.getField("ID").getInt(null);
            packetTypes.put(packetId, cls);
        } catch (IllegalAccessException e) {
            throw new NetworkException(e);
        } catch (NoSuchFieldException e) {
            throw new NetworkException(e);
        }
    }

    @Override
    protected void handleEvent(Event event) {
        if (event instanceof NetworkEvent) {
            NetworkEvent networkEvent = (NetworkEvent) event;
            try {
                out.writeByte(networkEvent.getPacketId());
                networkEvent.encode(out);
            } catch (IOException e) {
                throw new NetworkException(e);
            }
        }
        super.handleEvent(event);
    }

    @Override
    protected void processNetwork() {
        try {
            int packetId = in.readByte() & 0xff;
            NetworkEvent packet = createPacket(packetId);
            if( packet!=null ) {
                packet.decode(in);
                dispatchIncomingPacket(packet);
            } else {
                throw new NetworkException("Unknown packet id 0x"+Integer.toHexString(packetId));
            }
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    protected abstract void dispatchIncomingPacket(NetworkEvent packet);

    protected void handleUnknownPacket(int packetId) throws IOException {
        minecraftBinding.decode(packetId, in);
        out.writeByte(packetId);
        minecraftBinding.encode(packetId, out);
    }

    @Override
    protected synchronized void disconnect(NetworkException e) {
        e.printStackTrace();
        eventDispatcher.fire(new ConnectionLost());

        connected = false;
    }

    protected synchronized void disconnect() {
        connected = false;
    }

    @Override
    protected synchronized boolean isConnected() {
        return connected;
    }

    @Override
    protected DataInputStream getInputStream() {
        return in;
    }

    @Override
    protected DataOutputStream getOutputStream() {
        return out;
    }

    protected NetworkEvent createPacket( int packetId ) {
        if( !packetTypes.containsKey(packetId) ) {
            return null;
        }
        try {
            Class<? extends NetworkEvent> eventClass = packetTypes.get(packetId);
            return eventClass.newInstance();
        } catch (InstantiationException e) {
            throw new NetworkException(e);
        } catch (IllegalAccessException e) {
            throw new NetworkException(e);
        }
    }

}
