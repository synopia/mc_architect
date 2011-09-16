package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class SetCreativeSlot extends BasePacket {

    public static final int ID = 0x6b;
    private short a;
    private short b;
    private short c;
    private short d;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        a = in.readShort();
        b = in.readShort();
        c = in.readShort();
        d = in.readShort();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeShort(a);
        out.writeShort(b);
        out.writeShort(c);
        out.writeShort(d);
    }
}
