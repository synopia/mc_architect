package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P006PlayerSpawnPosition extends BasePacket {
    public static final int ID = 0x06;
    private int             x;
    private int             y;
    private int             z;

    public P006PlayerSpawnPosition() {}

    public P006PlayerSpawnPosition(int z, int y, int x) {
        this.z = z;
        this.y = y;
        this.x = x;
    }

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        x = in.readInt();
        y = in.readInt();
        z = in.readInt();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( x );
        out.writeInt( y );
        out.writeInt( z );
    }

    public int getZ() {
        return z;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
