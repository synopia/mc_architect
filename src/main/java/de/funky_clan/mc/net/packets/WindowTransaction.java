package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class WindowTransaction extends BasePacket {
    public static final int ID = 0x6a;
    private boolean         accepted;
    private short           actionNumber;
    private byte            windowId;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        windowId     = in.readByte();
        actionNumber = in.readShort();
        accepted     = in.readByte()!=0;
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeByte( windowId );
        out.writeShort( actionNumber );
        out.writeByte( accepted ? 1 : 0 );
    }
}
