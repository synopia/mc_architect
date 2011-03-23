package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class EntityRelativeMoveAndLook extends BasePacket {
    public static final int ID = 0x21;
    private int eid;
    private int dx;
    private int dy;
    private int dz;
    private int yaw;
    private int pitch;

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
        yaw = in.readByte();
        pitch = in.readByte();
    }

    @Override
    public void encode(DataOutputStream out) {
        //To change body of implemented methods use File | Settings | File Templates.
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

    public int getYaw() {
        return yaw;
    }

    public int getPitch() {
        return pitch;
    }
}
