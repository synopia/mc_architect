package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class Handshake extends BasePacket {
    public static final int ID = 0x02;
    private String          username;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        username = readString(in, 32);
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        writeString(out, username, 32);
    }

    public String getConnectionHash() {
        return username;
    }

    public String getUsername() {
        return username;
    }
}
