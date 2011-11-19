package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;
import de.funky_clan.mc.net.ItemStack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P104WindowMultiSlotUpdate extends BasePacket {
    public static final int ID = 0x68;
    private short           count;
    private ItemStack[]          items;
    private byte            windowId;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        windowId = in.readByte();
        count    = in.readShort();
        items    = new ItemStack[count];

        for( int i = 0; i < count; i++ ) {
            items[i] = readItem(in);
        }
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeByte( windowId );
        out.writeShort( count );

        for( ItemStack item : items ) {
            writeItem(out, item);
        }
    }

}
