package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class WindowMultiSlotUpdate extends BasePacket {
    public static final int ID = 0x68;
    private byte windowId;
    private short count;
    private Item[] items;

    public static class Item {
        public short itemId;
        public byte count;
        public short uses;

        public Item(short itemId, byte count, short uses) {
            this.itemId = itemId;
            this.count = count;
            this.uses = uses;
        }
    }

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        windowId = in.readByte();
        count = in.readShort();
        items = new Item[count];
        for( int i=0; i<count; i++ ) {
            short itemId = in.readShort();
            if( itemId!=-1 ) {
                items[i] = new Item(itemId,  in.readByte(), in.readShort() );
            }
        }
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeByte(windowId);
        out.writeShort(count);
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            if( item!=null ) {
                out.writeShort(item.itemId);
                out.writeByte(item.count);
                out.writeShort(item.uses);
            } else {
                out.writeShort(-1);
            }
        }
    }
}
