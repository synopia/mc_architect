package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public final class  LoginRequest extends BasePacket {
    public static final int ID = 0x01;
    private int             dimension;
    private int             protocolVersion;
    private long            seed;
    private String          username;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        protocolVersion = in.readInt();
        username  = readString(in, 16);
        seed      = in.readLong();
        dimension = in.readByte();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt(protocolVersion);
        writeString( username, out );
        out.writeLong( seed );
        out.writeByte( dimension );
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public String getUsername() {
        return username;
    }

    public long getSeed() {
        return seed;
    }

    public int getDimension() {
        return dimension;
    }
}
