package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P000KeepAlive extends BasePacket {
    public static final int ID = 0;
    private int a;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        a = in.readInt();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt(a);
    }
}
