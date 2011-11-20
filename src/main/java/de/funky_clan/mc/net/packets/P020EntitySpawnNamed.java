package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P020EntitySpawnNamed extends BasePacket {
    public static final int ID = 0x14;
    private short           currentItem;
    private int             eid;
    private byte            pitch;
    private String          playerName;
    private int             x;
    private int             y;
    private byte            yaw;
    private int             z;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        eid         = in.readInt();
        playerName  = readString(in, 16);
        x           = in.readInt();
        y           = in.readInt();
        z           = in.readInt();
        yaw         = in.readByte();
        pitch       = in.readByte();
        currentItem = in.readShort();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( eid );
        writeString(out, playerName, 16);
        out.writeInt( x );
        out.writeInt( y );
        out.writeInt( z );
        out.writeByte( yaw );
        out.writeByte( pitch );
        out.writeShort( currentItem );
    }

    public int getEid() {
        return eid;
    }

    public byte getPitch() {
        return pitch;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public byte getYaw() {
        return yaw;
    }

    public int getZ() {
        return z;
    }
}
