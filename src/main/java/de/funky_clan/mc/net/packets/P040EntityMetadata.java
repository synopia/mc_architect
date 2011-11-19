package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author synopia
 */
public class P040EntityMetadata extends BasePacket {
    public static final int ID = 0x28;
    private int             eid;
    private ArrayList       meta;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        eid  = in.readInt();
        meta = readMetadata( in );
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( eid );
        writeMetadata( out, meta );
    }
}
