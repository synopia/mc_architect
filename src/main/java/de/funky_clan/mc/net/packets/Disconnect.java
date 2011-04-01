package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class Disconnect extends BasePacket {
    public static final int ID = 0xff;
    private String          reason;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        reason = in.readUTF();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeUTF( reason );
    }
}
