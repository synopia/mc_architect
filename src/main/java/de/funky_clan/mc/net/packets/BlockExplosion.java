package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class BlockExplosion extends BasePacket {
    public static final int ID = 0x3c;
    private float           radius;
    private int             recordCount;
    private byte[]          records;
    private double          x;
    private double          y;
    private double          z;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        x           = in.readDouble();
        y           = in.readDouble();
        z           = in.readDouble();
        radius      = in.readFloat();
        recordCount = in.readInt();
        records     = new byte[recordCount * 3];
        in.readFully( records );
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeDouble( x );
        out.writeDouble( y );
        out.writeDouble( z );
        out.writeFloat( radius );
        out.writeInt( recordCount );
        out.write( records, 0, recordCount * 3 );
    }
}
