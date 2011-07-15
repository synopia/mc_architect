package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class DoorChange extends BasePacket {
    public static final int ID = 0x3d;

    private int a;
    private int b;
    private int c;
    private byte d;
    private int e;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        a = in.readInt();
        c = in.readInt();
        d = in.readByte();
        e = in.readInt();
        b = in.readInt();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeInt(a);
        out.writeInt(c);
        out.writeByte(d);
        out.writeInt(e);
        out.writeInt(b);
    }
}
