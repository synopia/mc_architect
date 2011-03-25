package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class TimeUpdate extends BasePacket {
    public static final int ID = 0x04;
    private long time;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        time = in.readLong();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeLong(time);
    }
}

