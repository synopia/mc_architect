package de.funky_clan.mc.eventbus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public interface NetworkEvent extends Event {
    int getPacketId();
    void decode( DataInputStream in ) throws IOException;
    void encode( DataOutputStream out ) throws IOException;
}
