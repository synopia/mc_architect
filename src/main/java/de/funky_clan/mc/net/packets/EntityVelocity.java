package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class EntityVelocity extends BasePacket {
    public static final int ID = 0x1c;
    private int             eid;
    private short           velocityX;
    private short           velocityY;
    private short           velocityZ;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        eid       = in.readInt();
        velocityX = in.readShort();
        velocityY = in.readShort();
        velocityZ = in.readShort();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( eid );
        out.writeShort( velocityX );
        out.writeShort( velocityY );
        out.writeShort( velocityZ );
    }
}
