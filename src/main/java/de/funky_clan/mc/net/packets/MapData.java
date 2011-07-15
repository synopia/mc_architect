package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class MapData extends BasePacket {
    public static final int ID = 0x83;

    private short a;
    private short b;
    private byte c[];

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        a = in.readShort();
        b = in.readShort();
        c = new byte[in.readByte()&0xff];
        in.readFully(c);
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeShort(a);
        out.writeShort(b);
        out.writeByte(c.length);
        out.write(c);
    }
}
