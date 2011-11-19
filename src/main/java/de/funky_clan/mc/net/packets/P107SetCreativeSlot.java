package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P107SetCreativeSlot extends BasePacket {

    public static final int ID = 0x6b;
    private short a;
    private short itemId;
    private short itemCount;
    private short itemUses;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        a = in.readShort();
        itemId   = in.readShort();
        if( itemId != -1 ) {
            itemCount = in.readByte();
            itemUses  = in.readShort();
        }
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeShort(a);
        out.writeShort(itemId);
        if( itemId!=-1 ) {
            out.writeShort(itemCount);
            out.writeShort(itemUses);
        }
    }
}
