package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class EntityStatus extends BasePacket {
    public static final int ID = 0x26;
    private int             eid;
    private byte            status;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        eid    = in.readInt();
        status = in.readByte();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( eid );
        out.writeByte( status );
    }
}
