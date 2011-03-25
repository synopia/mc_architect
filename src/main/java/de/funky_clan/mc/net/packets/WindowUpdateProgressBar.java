package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class WindowUpdateProgressBar extends BasePacket {
    public static final int ID = 0x69;
    private byte windowId;
    private short progress;
    private short value;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        windowId = in.readByte();
        progress = in.readShort();
        value = in.readShort();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeByte(windowId);
        out.writeShort(progress);
        out.writeShort(value);
    }
}
