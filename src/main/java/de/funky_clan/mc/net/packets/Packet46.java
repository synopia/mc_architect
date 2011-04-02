package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class Packet46 extends BasePacket {
    public static final int ID = 0x46;
    private byte a;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        a = in.readByte();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeByte(a);
    }
}
