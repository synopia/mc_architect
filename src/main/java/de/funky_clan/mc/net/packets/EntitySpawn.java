package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author synopia
 */
public class EntitySpawn extends BasePacket{
    public static final int ID = 0x18;
    private int eid;
    private byte type;
    private int x;
    private int y;
    private int z;
    private byte pitch;
    private byte yaw;
    private byte[] meta= new byte[128];
    private int metaLength;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        eid = in.readInt();
        type = in.readByte();
        x = in.readInt();
        y = in.readInt();
        z = in.readInt();
        yaw = in.readByte();
        pitch = in.readByte();
        metaLength = readMetadata( in, meta );
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeInt(eid);
        out.writeByte(type);
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(z);
        out.writeByte(yaw);
        out.writeByte(pitch);
        writeMetadata(out, meta, metaLength);
    }

}
