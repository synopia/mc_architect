package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P008UpdateHealth extends BasePacket {
    public static final int ID = 0x08;
    private short           health;
    private short           b;
    private float           c;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        health = in.readShort();
        b = in.readShort();
        c = in.readFloat();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeShort( health );
        out.writeShort(b);
        out.writeFloat(c);
    }
}
