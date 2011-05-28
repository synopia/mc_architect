package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class EntityCreated extends BasePacket {
    public static final int ID = 0x17;
    private int             eid;
    private byte            type;
    private int             x;
    private int             y;
    private int             z;
    private int             i;
    private short           e;
    private short           f;
    private short           g;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        eid  = in.readInt();
        type = in.readByte();
        x    = in.readInt();
        y    = in.readInt();
        z    = in.readInt();
        i    = in.readInt();
        if( i>0 ) {
            e = in.readShort();
            f = in.readShort();
            g = in.readShort();
        }
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( eid );
        out.writeByte( type );
        out.writeInt( x );
        out.writeInt( y );
        out.writeInt( z );
        out.writeInt( i );
        if( i>0 ) {
            out.writeShort(e);
            out.writeShort(f);
            out.writeShort(g);
        }
    }
}
