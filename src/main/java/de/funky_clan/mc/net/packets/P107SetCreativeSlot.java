package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;
import de.funky_clan.mc.net.ItemStack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P107SetCreativeSlot extends BasePacket {

    public static final int ID = 0x6b;
    private short a;
    private ItemStack itemStack;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        a = in.readShort();
        itemStack = readItem(in);
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeShort(a);
        writeItem(out, itemStack);
    }
}
