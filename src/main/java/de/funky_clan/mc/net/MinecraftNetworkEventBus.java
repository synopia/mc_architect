package de.funky_clan.mc.net;

import com.google.inject.Inject;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.eventbus.NetworkEvent;
import de.funky_clan.mc.eventbus.NetworkEventBus;
import de.funky_clan.mc.events.network.ConnectionLost;
import de.funky_clan.mc.net.packets.BlockExplosion;
import de.funky_clan.mc.net.packets.BlockMultiUpdate;
import de.funky_clan.mc.net.packets.BlockPlayNote;
import de.funky_clan.mc.net.packets.BlockSignUpdate;
import de.funky_clan.mc.net.packets.BlockUpdate;
import de.funky_clan.mc.net.packets.ChatMessage;
import de.funky_clan.mc.net.packets.ChunkData;
import de.funky_clan.mc.net.packets.ChunkPreparation;
import de.funky_clan.mc.net.packets.Disconnect;
import de.funky_clan.mc.net.packets.EntityAction;
import de.funky_clan.mc.net.packets.EntityAnimation;
import de.funky_clan.mc.net.packets.EntityAttach;
import de.funky_clan.mc.net.packets.EntityCreated;
import de.funky_clan.mc.net.packets.EntityDestroy;
import de.funky_clan.mc.net.packets.EntityEquipment;
import de.funky_clan.mc.net.packets.EntityLook;
import de.funky_clan.mc.net.packets.EntityMetadata;
import de.funky_clan.mc.net.packets.EntityPainting;
import de.funky_clan.mc.net.packets.EntityRelativeMove;
import de.funky_clan.mc.net.packets.EntityRelativeMoveAndLook;
import de.funky_clan.mc.net.packets.EntitySpawn;
import de.funky_clan.mc.net.packets.EntitySpawnNamed;
import de.funky_clan.mc.net.packets.EntityStatus;
import de.funky_clan.mc.net.packets.EntityTeleport;
import de.funky_clan.mc.net.packets.EntityUpdate;
import de.funky_clan.mc.net.packets.EntityUse;
import de.funky_clan.mc.net.packets.EntityVelocity;
import de.funky_clan.mc.net.packets.Handshake;
import de.funky_clan.mc.net.packets.ItemCollect;
import de.funky_clan.mc.net.packets.ItemSpawn;
import de.funky_clan.mc.net.packets.KeepAlive;
import de.funky_clan.mc.net.packets.LoginRequest;
import de.funky_clan.mc.net.packets.Packet1B;
import de.funky_clan.mc.net.packets.PlayerBlockPlacement;
import de.funky_clan.mc.net.packets.PlayerChangeSlot;
import de.funky_clan.mc.net.packets.PlayerDigging;
import de.funky_clan.mc.net.packets.PlayerLook;
import de.funky_clan.mc.net.packets.PlayerOnGround;
import de.funky_clan.mc.net.packets.PlayerPosition;
import de.funky_clan.mc.net.packets.PlayerPositionAndLook;
import de.funky_clan.mc.net.packets.PlayerRespawn;
import de.funky_clan.mc.net.packets.PlayerSpawnPosition;
import de.funky_clan.mc.net.packets.PlayerUseBed;
import de.funky_clan.mc.net.packets.TimeUpdate;
import de.funky_clan.mc.net.packets.UpdateHealth;
import de.funky_clan.mc.net.packets.WindowClick;
import de.funky_clan.mc.net.packets.WindowClose;
import de.funky_clan.mc.net.packets.WindowMultiSlotUpdate;
import de.funky_clan.mc.net.packets.WindowOpen;
import de.funky_clan.mc.net.packets.WindowSlotUpdate;
import de.funky_clan.mc.net.packets.WindowTransaction;
import de.funky_clan.mc.net.packets.WindowUpdateProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @author synopia
 */
public abstract class MinecraftNetworkEventBus extends NetworkEventBus {
    private final Logger                                    logger      =
        LoggerFactory.getLogger( MinecraftNetworkEventBus.class );
    private HashMap<Integer, Class<? extends NetworkEvent>> packetTypes = new HashMap<Integer,
                                                                              Class<? extends NetworkEvent>>();
    private boolean          connected;
    @Inject
    private EventDispatcher  eventDispatcher;
    private DataInputStream  in;
    private int              lastPacketId;
    private DataOutputStream out;
    private DataInputStream  realIn;

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
        addPacketType( ChunkPreparation.class );
        addPacketType( ChunkData.class );
        addPacketType( BlockMultiUpdate.class );
        addPacketType( BlockUpdate.class );
        addPacketType( BlockPlayNote.class );
        addPacketType( BlockExplosion.class );
        addPacketType( WindowOpen.class );
        addPacketType( WindowClose.class );
        addPacketType( WindowClick.class );
        addPacketType( WindowMultiSlotUpdate.class );
        addPacketType( WindowSlotUpdate.class );
        addPacketType( WindowUpdateProgressBar.class );
        addPacketType( WindowTransaction.class );
        addPacketType( BlockSignUpdate.class );
        addPacketType( Disconnect.class );
    }

    public synchronized void connect( InputStream in, OutputStream out ) {
        logger.info( this + " connected" );
        this.in   = new DataInputStream( in );
        this.out  = new DataOutputStream( out );
        connected = true;
    }

    protected void addPacketType( int packetId, Class<? extends NetworkEvent> cls ) {
        packetTypes.put( packetId, cls );
    }

    protected void addPacketType( Class<? extends NetworkEvent> cls ) {
        int packetId;

        try {
            packetId = cls.getField( "ID" ).getInt( null );
            packetTypes.put( packetId, cls );
        } catch( IllegalAccessException e ) {
            throw new NetworkException( e );
        } catch( NoSuchFieldException e ) {
            throw new NetworkException( e );
        }
    }

    @Override
    protected void handleEvent( Event event ) {
        if(( event instanceof NetworkEvent ) && ( out != null )) {
            NetworkEvent networkEvent = (NetworkEvent) event;

            if( networkEvent.getSource() == getNetworkType() ) {
                try {
                    out.writeByte( networkEvent.getPacketId() );
                    networkEvent.encode( out );
                } catch( IOException e ) {
                    throw new NetworkException( e );
                }
            }
        }

        super.handleEvent( event );
    }

    @Override
    protected void processNetwork() {
        try {
            int          packetId = in.readByte() & 0xff;
            NetworkEvent packet   = createPacket( packetId );

            if( packet != null ) {
                packet.decode( in );

                if(( packetId == 0x0d ) && ( getNetworkType() == NetworkEvent.SERVER )) {
                    System.out.println( "pos update from server" );
                }

                eventDispatcher.fire( packet );
                lastPacketId = packetId;
            } else {
                throw new NetworkException( "Unknown packet id 0x" + Integer.toHexString( packetId )
                                            + ", last packet id: 0x" + Integer.toHexString( lastPacketId ));
            }
        } catch( IOException e ) {
            throw new NetworkException( e );
        }
    }

    @Override
    protected synchronized void disconnect( NetworkException e ) {
        e.printStackTrace();
        eventDispatcher.fire( new ConnectionLost() );
        disconnect();
    }

    protected synchronized void disconnect() {
        try {
            if( in != null ) {
                in.close();
            }

            if( out != null ) {
                out.close();
            }
        } catch( IOException e ) {

            // ignore
        }

        connected = false;
        super.disconnect();
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
        if( !packetTypes.containsKey( packetId )) {
            return null;
        }

        Class<? extends NetworkEvent> eventClass = packetTypes.get( packetId );

        return preparePacket( eventClass );
    }

    protected NetworkEvent preparePacket( Class<? extends NetworkEvent> eventClass ) {
        NetworkEvent packet;

        try {
            packet = eventClass.newInstance();
            packet.setSource( getNetworkType() );

            return packet;
        } catch( InstantiationException e ) {
            throw new NetworkException( e );
        } catch( IllegalAccessException e ) {
            throw new NetworkException( e );
        }
    }

    protected abstract byte getNetworkType();
}
