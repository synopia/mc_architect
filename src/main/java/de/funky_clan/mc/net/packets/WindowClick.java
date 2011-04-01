package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class WindowClick extends BasePacket {
    public static final int ID = 0x66;
    private short           actionNumber;
    private byte            itemCount;
    private short           itemId;
    private short           itemUses;
    private byte            rightClick;
    private short           slot;
    private byte            windowId;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        windowId     = in.readByte();
        slot         = in.readShort();
        rightClick   = in.readByte();
        actionNumber = in.readShort();
        itemId       = in.readShort();

        if( itemId != -1 ) {
            itemCount = in.readByte();
            itemUses  = in.readShort();
        }
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeByte( windowId );
        out.writeShort( slot );
        out.writeByte( rightClick );
        out.writeShort( actionNumber );
        out.writeShort( itemId );

        if( itemId != -1 ) {
            out.writeByte( itemCount );
            out.writeShort( itemUses );
        }
    }
}
