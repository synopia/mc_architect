package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class BlockPlayNote extends BasePacket {
    public static final int ID = 0x36;
    private int x;
    private short y;
    private int z;
    private byte instrumentType;
    private byte pitch;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        x = in.readInt();
        y = in.readShort();
        z = in.readInt();
        instrumentType = in.readByte();
        pitch = in.readByte();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeInt(x);
        out.writeShort(y);
        out.writeInt(z);
        out.writeByte(instrumentType);
        out.writeByte(pitch);
    }
}
