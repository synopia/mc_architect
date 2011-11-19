package de.funky_clan.mc.net;

import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.eventbus.NetworkEvent;
import de.funky_clan.mc.eventbus.NetworkEventBus;
import de.funky_clan.mc.events.network.ConnectionLost;
import de.funky_clan.mc.net.packets.*;
import de.funky_clan.mc.net.packets.P130BlockSignUpdate;
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
    private final Logger                                          logger      =
        LoggerFactory.getLogger( MinecraftNetworkEventBus.class );
    private final HashMap<Integer, Class<? extends NetworkEvent>> packetTypes = new HashMap<Integer,
                                                                                    Class<? extends NetworkEvent>>();
    private boolean          connected;
    @Inject
    private EventDispatcher  eventDispatcher;
    private DataInputStream  in;
    private int              lastPacketId;
    private DataOutputStream out;

    protected MinecraftNetworkEventBus() {
        addPacketType( P000KeepAlive.class );
        addPacketType( P001LoginRequest.class );
        addPacketType( P002Handshake.class );
        addPacketType( P003ChatMessage.class );
        addPacketType( P004TimeUpdate.class );
        addPacketType( P005EntityEquipment.class );
        addPacketType( P006PlayerSpawnPosition.class );
        addPacketType( P007EntityUse.class );
        addPacketType( P008UpdateHealth.class );
        addPacketType( P009PlayerRespawn.class );
        addPacketType( P010PlayerOnGround.class );
        addPacketType( P011PlayerPosition.class );
        addPacketType( P012PlayerLook.class );
        addPacketType( P013PlayerPositionAndLook.class );
        addPacketType( P014PlayerDigging.class );
        addPacketType( P015PlayerBlockPlacement.class );
        addPacketType( P016PlayerChangeSlot.class );
        addPacketType( P017PlayerUseBed.class );
        addPacketType( P018EntityAnimation.class );
        addPacketType( P019EntityAction.class );
        addPacketType( P020EntitySpawnNamed.class );
        addPacketType( P021ItemSpawn.class );
        addPacketType( P022ItemCollect.class );
        addPacketType( P023EntityCreated.class );
        addPacketType( P024EntitySpawn.class );
        addPacketType( P025EntityPainting.class );
        addPacketType( P026AddExpOrb.class );
        addPacketType( P027Packet1B.class );
        addPacketType( P028EntityVelocity.class );
        addPacketType( P029EntityDestroy.class );
        addPacketType( P030EntityUpdate.class );
        addPacketType( P031EntityRelativeMove.class );
        addPacketType( P032EntityLook.class );
        addPacketType( P033EntityRelativeMoveAndLook.class );
        addPacketType( P034EntityTeleport.class );
        addPacketType( P038EntityStatus.class );
        addPacketType( P039EntityAttach.class );
        addPacketType( P040EntityMetadata.class );
        addPacketType( P041EntityEffect.class );
        addPacketType( P042EntityRemoveEffect.class );
        addPacketType( P043SetExperience.class );
        addPacketType( P050ChunkPreparation.class );
        addPacketType( P051ChunkData.class );
        addPacketType( P052BlockMultiUpdate.class );
        addPacketType( P053BlockUpdate.class );
        addPacketType( P054BlockPlayNote.class );
        addPacketType( P060BlockExplosion.class );
        addPacketType( P061DoorChange.class );
        addPacketType( P070Bed.class );
        addPacketType( P071Weather.class );
        addPacketType( P100WindowOpen.class );
        addPacketType( P101WindowClose.class );
        addPacketType( P102WindowClick.class );
        addPacketType( P103WindowSlotUpdate.class );
        addPacketType( P104WindowMultiSlotUpdate.class );
        addPacketType( P105WindowUpdateProgressBar.class );
        addPacketType( P106WindowTransaction.class );
        addPacketType( P107SetCreativeSlot.class );
        addPacketType( P108EnchantItem.class );
        addPacketType( P130BlockSignUpdate.class );
        addPacketType( P131MapData.class );
        addPacketType( P200Statistic.class );
        addPacketType( P201PlayerInfo.class );
        addPacketType( P254GetInfo.class );
        addPacketType( P255Disconnect.class );
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
        super.handleEvent( event );
        if(( event instanceof NetworkEvent ) && ( out != null )) {
            NetworkEvent networkEvent = (NetworkEvent) event;
            sendPacket(networkEvent);
        }

    }

    public void sendPacket(NetworkEvent networkEvent) {
        if( networkEvent.getSource() == getNetworkType() ) {
            try {
                out.writeByte( networkEvent.getPacketId() );
//                logger.info("Packet id "+networkEvent.getPacketId()+" dispatching");
                networkEvent.encode( out );
//                logger.info(" Packet id "+networkEvent.getPacketId()+" encoded");
            } catch( IOException e ) {
                throw new NetworkException( e );
            }
        }
    }

    @Override
    protected void processNetwork() {
        try {
//            logger.info("receiving");
            int          packetId = in.readByte() & 0xff;
//            logger.info("Packet id "+packetId+" received");
            NetworkEvent packet   = createPacket( packetId );

            if( packet != null ) {
                packet.decode( in );
//                logger.info(" Packet id "+packetId+" decoded");
                eventDispatcher.publish(packet);
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
        eventDispatcher.publish(new ConnectionLost());
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
