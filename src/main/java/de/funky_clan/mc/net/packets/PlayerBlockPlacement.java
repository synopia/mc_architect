package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class PlayerBlockPlacement extends BasePacket {
    public static final int ID = 0x0f;
    private int x;
    private byte y;
    private int z;
    private byte direction;
    private short blockId;
    private byte amount;
    private short damage;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        x = in.readInt();
        y = in.readByte();
        z = in.readInt();
        direction = in.readByte();
        blockId = in.readShort();
        if( blockId>=0 ) {
            amount = in.readByte();
            damage = in.readShort();
        }
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeInt(x);
        out.writeByte(y);
        out.writeInt(z);
        out.writeByte(direction);
        out.writeShort(blockId);
        if( blockId>=0 ) {
            out.writeByte(amount);
            out.writeShort(damage);
        }
    }
}
