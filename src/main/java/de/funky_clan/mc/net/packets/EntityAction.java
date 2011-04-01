package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class EntityAction extends BasePacket {
    public static final int ID = 0x13;
    private byte            action;
    private int             eid;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        eid    = in.readInt();
        action = in.readByte();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( eid );
        out.writeByte( action );
    }
}
