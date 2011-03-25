package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class BlockUpdate extends BasePacket {
    public static final int ID = 0x35;
    private int x;
    private int y;
    private int z;
    private byte type;
    private byte meta;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        x = in.readInt();
        y = in.readByte();
        z = in.readInt();
        type = in.readByte();
        meta = in.readByte();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeInt(x);
        out.writeByte(y);
        out.writeInt(z);
        out.writeByte(type);
        out.writeByte(meta);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public byte getType() {
        return type;
    }

    public byte getMeta() {
        return meta;
    }
}
