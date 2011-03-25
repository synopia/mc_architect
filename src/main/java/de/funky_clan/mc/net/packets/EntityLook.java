package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class EntityLook extends BasePacket {
    public static final int ID = 0x20;
    private int eid;
    private int yaw;
    private int pitch;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        eid = in.readInt();
        yaw = in.readByte();
        pitch = in.readByte();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeInt(eid);
        out.writeByte(yaw);
        out.writeByte(pitch);
    }

    public int getEid() {
        return eid;
    }

    public int getYaw() {
        return yaw;
    }

    public int getPitch() {
        return pitch;
    }
}
