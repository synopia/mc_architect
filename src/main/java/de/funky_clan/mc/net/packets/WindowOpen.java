package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class WindowOpen extends BasePacket {
    public static final int ID = 0x64;
    private byte            invType;
    private byte            numberOfSlots;
    private byte            windowId;
    private String          windowTitle;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        windowId      = in.readByte();
        invType       = in.readByte();
        windowTitle   = in.readUTF();
        numberOfSlots = in.readByte();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeByte( windowId );
        out.writeByte( invType );
        out.writeUTF( windowTitle );
        out.writeByte( numberOfSlots );
    }
}
