package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class PlayerUseBed extends BasePacket {
    public static final int ID = 0x11;
    private int z;
    private byte y;
    private int x;
    private byte inBed;
    private int eid;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        eid = in.readInt();
        inBed = in.readByte();
        x = in.readInt();
        y = in.readByte();
        z = in.readInt();

    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeInt(eid);
        out.writeByte(inBed);
        out.writeInt(x);
        out.writeByte(y);
        out.writeInt(z);
    }
}
