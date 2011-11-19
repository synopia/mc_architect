package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P009PlayerRespawn extends BasePacket {
    public static final int ID = 0x09;
    private byte b;
    private byte c;
    private byte e;
    private short d;
    private long a;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        b = in.readByte();
        c = in.readByte();
        e = in.readByte();
        d = in.readShort();
        a = in.readLong();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeByte(b);
        out.writeByte(c);
        out.writeByte(e);
        out.writeShort(d);
        out.writeLong(a);
    }
}
