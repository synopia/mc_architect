package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class EntityEffect extends BasePacket {
    public static final int ID = 0x29;
    private int a;
    private byte b;
    private byte c;
    private short d;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        a = in.readInt();
        b = in.readByte();
        c = in.readByte();
        d = in.readShort();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeInt(a);
        out.writeByte(b);
        out.writeByte(c);
        out.writeShort(d);
    }
}
