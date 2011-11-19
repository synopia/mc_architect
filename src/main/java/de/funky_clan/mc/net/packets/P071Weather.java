package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P071Weather extends BasePacket {
    public static final int ID = 0x47;

    private int a;
    private byte e;
    private int b;
    private int c;
    private int d;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        a = in.readInt();
        e = in.readByte();
        b = in.readInt();
        c = in.readInt();
        d = in.readInt();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeInt(a);
        out.writeByte(e);
        out.writeInt(b);
        out.writeInt(c);
        out.writeInt(d);
    }
}
