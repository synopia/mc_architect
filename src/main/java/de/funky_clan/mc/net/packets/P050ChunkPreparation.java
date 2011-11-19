package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P050ChunkPreparation extends BasePacket {
    public static final int ID = 0x32;
    private boolean         mode;
    private int             x;
    private int             z;

    public P050ChunkPreparation() {}

    public P050ChunkPreparation(byte source, int x, int z, boolean mode) {
        super( source );
        this.x    = x;
        this.z    = z;
        this.mode = mode;
    }

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        x    = in.readInt();
        z    = in.readInt();
        mode = in.read()!=0;
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( x );
        out.writeInt( z );
        out.write( mode ? 1 : 0 );
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
