package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public final class P001LoginRequest extends BasePacket {
    public static final int ID = 0x01;
    private int             dimension;
    private int             protocolVersion;
    private long            seed;
    private String          username;
    private byte e;
    private byte f;
    private byte g;
    private byte h;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        protocolVersion = in.readInt();
        username  = readString(in, 16);
        seed      = in.readLong();
        dimension = in.readInt();
        e = in.readByte();
        f = in.readByte();
        g = in.readByte();
        h = in.readByte();

    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt(protocolVersion);
        writeString(out, username, 16);
        out.writeLong(seed);
        out.writeInt(dimension);
        out.writeByte(e);
        out.writeByte(f);
        out.writeByte(g);
        out.writeByte(h);
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
