package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class EntityMetadata extends BasePacket {
    public static final int ID = 0x28;
    private byte[] meta = new byte[128];
    private int metaLength ;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        metaLength = readMetadata(in, meta);
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        writeMetadata(out, meta, metaLength);
    }
}
