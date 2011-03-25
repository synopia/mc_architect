package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class Packet1B extends BasePacket {
    public static final int ID = 0x1b;
    private float a;
    private float b;
    private float c;
    private float d;
    private boolean e;
    private boolean f;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        a = in.readFloat();
        b = in.readFloat();
        c = in.readFloat();
        d = in.readFloat();
        e = in.readBoolean();
        f = in.readBoolean();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeFloat(a);
        out.writeFloat(b);
        out.writeFloat(c);
        out.writeFloat(d);
        out.writeBoolean(e);
        out.writeBoolean(f);
    }
}
