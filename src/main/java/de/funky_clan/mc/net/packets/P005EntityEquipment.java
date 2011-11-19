package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P005EntityEquipment extends BasePacket {
    public static final int ID = 0x05;
    private short           damage;
    private int             eid;
    private short           itemId;
    private short           slot;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        eid    = in.readInt();
        slot   = in.readShort();
        itemId = in.readShort();
        damage = in.readShort();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( eid );
        out.writeShort( slot );
        out.writeShort( itemId );
        out.writeShort( damage );
    }
}
