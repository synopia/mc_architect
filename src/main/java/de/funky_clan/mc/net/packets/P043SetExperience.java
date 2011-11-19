package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P043SetExperience extends BasePacket {

    public static final int ID = 0x2b;
    private float a;
    private short b;
    private short c;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        a = in.readFloat();
        b = in.readShort();
        c = in.readShort();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeFloat(a);
        out.writeShort(b);
        out.writeShort(c);
    }
}
