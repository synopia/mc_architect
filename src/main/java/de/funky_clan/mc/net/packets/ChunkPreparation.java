package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class ChunkPreparation extends BasePacket {
    public static final int ID = 0x32;
    private int x;
    private int z;
    private boolean mode;

    public ChunkPreparation() {
    }

    public ChunkPreparation(byte source, int x, int z, boolean mode) {
        super(source);
        this.x = x;
        this.z = z;
        this.mode = mode;
    }

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        x = in.readInt();
        z = in.readInt();
        mode = in.readBoolean();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeInt(x);
        out.writeInt(z);
        out.writeBoolean(mode);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public boolean isLoad() {
        return mode;
    }
}
