package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class ChatMessage extends BasePacket {
    public static final int ID = 0x03;
    private String message;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        message = in.readUTF();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeUTF(message);
    }
}
