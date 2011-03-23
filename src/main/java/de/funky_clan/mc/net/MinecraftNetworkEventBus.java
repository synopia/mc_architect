package de.funky_clan.mc.net;

import com.google.inject.Inject;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.NetworkEvent;
import de.funky_clan.mc.eventbus.NetworkEventBus;
import de.funky_clan.mc.events.network.ConnectionEstablished;
import de.funky_clan.mc.events.network.ConnectionLost;
import de.funky_clan.mc.net.packets.Handshake;
import de.funky_clan.mc.net.packets.LoginRequest;

import java.io.*;
import java.util.HashMap;

/**
 * @author synopia
 */
public abstract class MinecraftNetworkEventBus extends NetworkEventBus {
    private HashMap<Integer, Class<? extends NetworkEvent>> packetTypes = new HashMap<Integer, Class<? extends NetworkEvent>>();
    private DataInputStream in;
    private DataOutputStream out;
    private boolean connected;

    @Inject
    private MinecraftBinding minecraftBinding;

    @Inject
    private EventDispatcher eventDispatcher;

    protected MinecraftNetworkEventBus() {
        addPacketType( LoginRequest.ID, LoginRequest.class );
        addPacketType( Handshake.ID,    Handshake.class );
    }

    public synchronized void connect( InputStream in, OutputStream out ) {
        this.in = new DataInputStream( new MitmInputStream(in, out) );
        this.out = new DataOutputStream(out);
        connected = true;
    }

    protected void addPacketType( int packetId, Class<? extends NetworkEvent> cls ) {
        packetTypes.put(packetId, cls);
    }

    @Override
    protected void processNetwork() {
        try {
            int packetId = in.readByte();
            NetworkEvent packet = createPacket(packetId);
            if( packet!=null ) {
                packet.decode(in);
                eventDispatcher.fire(packet);
            } else {
                handleUnknownPacket( packetId );
            }
            out.flush();
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    protected void handleUnknownPacket(int packetId) {
        minecraftBinding.decode(packetId, in);
    }

    @Override
    protected synchronized void disconnect(NetworkException e) {
        eventDispatcher.fire(new ConnectionLost() );

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
