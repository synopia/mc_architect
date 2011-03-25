package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class EntityRelativeMove extends BasePacket {
    public static final int ID = 0x01f;
    private int eid;
    private int dx;
    private int dy;
    private int dz;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        eid = in.readInt();
        dx = in.readByte();
        dy = in.readByte();
        dz = in.readByte();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeInt(eid);
        out.writeByte(dx);
        out.writeByte(dy);
        out.writeByte(dz);
    }

    public int getEid() {
        return eid;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int getDz() {
        return dz;
    }
}
