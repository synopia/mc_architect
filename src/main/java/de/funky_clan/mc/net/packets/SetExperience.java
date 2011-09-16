package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class SetExperience extends BasePacket {

    public static final int ID = 0x2b;
    private byte a;
    private byte b;
    private short c;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        a = in.readByte();
        b = in.readByte();
        c = in.readShort();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeByte(a);
        out.writeByte(b);
        out.writeShort(c);
    }
}
