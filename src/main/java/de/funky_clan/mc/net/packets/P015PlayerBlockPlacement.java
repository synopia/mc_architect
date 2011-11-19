package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;
import de.funky_clan.mc.net.ItemStack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P015PlayerBlockPlacement extends BasePacket {
    public static final int ID = 0x0f;
    private byte            direction;
    private int             x;
    private byte            y;
    private int             z;
    private ItemStack       itemStack;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        x         = in.readInt();
        y         = in.readByte();
        z         = in.readInt();
        direction = in.readByte();
        itemStack = readItem(in);
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( x );
        out.writeByte( y );
        out.writeInt( z );
        out.writeByte( direction );
        writeItem(out, itemStack);
    }
}
