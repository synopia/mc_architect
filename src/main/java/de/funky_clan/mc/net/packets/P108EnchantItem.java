package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P108EnchantItem extends BasePacket {
    public static final int ID = 0x6c;

    private byte a;
    private byte b;
    
    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        a = in.readByte();
        b = in.readByte();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.write(a);
        out.write(b);
    }
}
