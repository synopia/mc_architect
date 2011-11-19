package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P104WindowMultiSlotUpdate extends BasePacket {
    public static final int ID = 0x68;
    private short           count;
    private Item[]          items;
    private byte            windowId;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        windowId = in.readByte();
        count    = in.readShort();
        items    = new Item[count];

        for( int i = 0; i < count; i++ ) {
            short itemId = in.readShort();

            if( itemId != -1 ) {
                items[i] = new Item( itemId, in.readByte(), in.readShort() );
            }
        }
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeByte( windowId );
        out.writeShort( count );

        for( Item item : items ) {
            if( item != null ) {
                out.writeShort( item.itemId );
                out.writeByte( item.count );
                out.writeShort( item.uses );
            } else {
                out.writeShort( -1 );
            }
        }
    }

    public static class Item {
        public final byte  count;
        public final short itemId;
        public final short uses;

        public Item( short itemId, byte count, short uses ) {
            this.itemId = itemId;
            this.count  = count;
            this.uses   = uses;
        }
    }
}
