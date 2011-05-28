package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class EntityPainting extends BasePacket {
    public static final int ID = 0x19;
    private int             direction;
    private int             eid;
    private String          title;
    private int             x;
    private int             y;
    private int             z;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        eid       = in.readInt();
        title     = readString(in, 119);
        x         = in.readInt();
        y         = in.readInt();
        z         = in.readInt();
        direction = in.readInt();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( eid );
        writeString(title, out);
        out.writeInt( x );
        out.writeInt( y );
        out.writeInt( z );
        out.writeInt( direction );
    }
}
