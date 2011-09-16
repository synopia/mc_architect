package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class Bed extends BasePacket {

    public static final int ID = 0x46;
    private byte a;
    private byte b;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        a = in.readByte();
        b = in.readByte();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeByte(a);
        out.writeByte(b);
    }
}
