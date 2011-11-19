package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P014PlayerDigging extends BasePacket {
    public static final int ID = 0x0e;
    private byte            face;
    private byte            status;
    private int             x;
    private byte            y;
    private int             z;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        status = in.readByte();
        x      = in.readInt();
        y      = in.readByte();
        z      = in.readInt();
        face   = in.readByte();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeByte( status );
        out.writeInt( x );
        out.writeByte( y );
        out.writeInt( z );
        out.writeByte( face );
    }
}
