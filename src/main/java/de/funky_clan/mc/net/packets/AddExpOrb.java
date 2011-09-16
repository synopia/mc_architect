package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class AddExpOrb extends BasePacket {
    public static final int ID = 0x1a;
    private int a;
    private int b;
    private int c;
    private int d;
    private short e;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        a = in.readInt();
        b = in.readInt();
        c = in.readInt();
        d = in.readInt();
        e = in.readShort();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeInt(a);
        out.writeInt(b);
        out.writeInt(c);
        out.writeInt(d);
        out.writeShort(e);
    }
}
