package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class BlockMultiUpdate extends BasePacket {
    public static final int ID = 0x34;
    private int x;
    private int y;
    private short size;
    private short[] coords;
    private byte[] type;
    private byte[] meta;

    public interface Each {
        public void update(int x, int y, int z, int type, int meta);
    }

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        x = in.readInt();
        y = in.readInt();
        size = in.readShort();
        coords = new short[size];
        for (int i = 0; i < size; i++) {
            coords[i] = in.readShort();
        }
        type = new byte[size];
        meta = new byte[size];
        in.readFully(type);
        in.readFully(meta);
    }

    @Override
    public void encode(DataOutputStream out) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void each( Each block ) {
        for (int i = 0; i < size; i++) {
            int x = (this.x<<4) + (coords[i]>>12);
            int z = (this.y<<4) + ((coords[i]>>8) & ((1<<4)-1));
            int y = coords[i] & ((1<<8)-1);
            block.update(x, y, z, type[i], meta[i]);
        }

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public short getSize() {
        return size;
    }

    public short[] getCoords() {
        return coords;
    }

    public byte[] getType() {
        return type;
    }

    public byte[] getMeta() {
        return meta;
    }
}
