package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class ItemSpawn extends BasePacket {
    public static final int ID = 0x15;
    private int eid;
    private short itemId;
    private byte count;
    private short damage;
    private int x;
    private int y;
    private int z;
    private byte yaw;
    private byte pitch;
    private byte roll;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        eid = in.readInt();
        itemId = in.readShort();
        count = in.readByte();
        damage = in.readShort();
        x = in.readInt();
        y = in.readInt();
        z = in.readInt();
        yaw = in.readByte();
        pitch = in.readByte();
        roll = in.readByte();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeInt(eid);
        out.writeShort(itemId);
        out.writeByte(count);
        out.writeShort(damage);
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(z);
        out.writeByte(yaw);
        out.writeByte(pitch);
        out.writeByte(roll);
    }
}
