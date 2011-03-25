package de.funky_clan.mc.eventbus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public interface NetworkEvent extends Event {
    byte CLIENT = 1;
    byte SERVER = 2;

    int getPacketId();
    void decode( DataInputStream in ) throws IOException;
    void encode( DataOutputStream out ) throws IOException;

    byte getSource();

    void setSource(byte source);
}
