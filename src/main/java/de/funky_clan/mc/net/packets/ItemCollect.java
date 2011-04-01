package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class ItemCollect extends BasePacket {
    public static final int ID = 0x16;
    private int             collectedId;
    private int             collectorId;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        collectedId = in.readInt();
        collectorId = in.readInt();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( collectedId );
        out.writeInt( collectorId );
    }
}
