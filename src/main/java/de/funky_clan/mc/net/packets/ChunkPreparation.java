package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class ChunkPreparation extends BasePacket {
    public static final int ID = 0x32;
    private int x;
    private int y;
    private boolean mode;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        x = in.readInt();
        y = in.readInt();
        mode = in.readBoolean();
    }

    @Override
    public void encode(DataOutputStream out) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isMode() {
        return mode;
    }
}
