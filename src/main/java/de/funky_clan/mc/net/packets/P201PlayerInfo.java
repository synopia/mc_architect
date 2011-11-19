package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P201PlayerInfo extends BasePacket {

    public static final int ID = 0xc9;
    private String s;
    private boolean b;
    private short c;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        s = readString(in, 16);
        b = in.readByte()!=0;
        c = in.readShort();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        writeString(out, s, 16);
        out.writeByte(b ? 1 : 0);
        out.writeShort(c);
    }
}
