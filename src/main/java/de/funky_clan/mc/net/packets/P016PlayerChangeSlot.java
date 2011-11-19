package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P016PlayerChangeSlot extends BasePacket {
    public static final int ID = 0x10;
    private short           slotId;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        slotId = in.readShort();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeShort( slotId );
    }
}
