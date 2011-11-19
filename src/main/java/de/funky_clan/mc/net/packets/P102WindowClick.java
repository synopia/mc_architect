package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;
import de.funky_clan.mc.net.ItemStack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P102WindowClick extends BasePacket {
    public static final int ID = 0x66;
    private short           actionNumber;
    private byte            rightClick;
    private short           slot;
    private byte            windowId;
    private boolean         f;
    
    private ItemStack       itemStack;

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
        f            = in.readBoolean();
        itemStack    = readItem(in);
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeByte( windowId );
        out.writeShort( slot );
        out.writeByte( rightClick );
        out.writeShort( actionNumber );
        out.writeBoolean( f );
        writeItem( out, itemStack );
    }
}
