package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class PlayerPositionAndLook extends BasePacket {
    public static final int ID = 0x0d;
    private boolean         onGround;
    private float           pitch;
    private double          stance;
    private double          x;
    private double          y;
    private float           yaw;
    private double          z;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        x        = in.readDouble();
        y        = in.readDouble();
        stance   = in.readDouble();
        z        = in.readDouble();
        yaw      = in.readFloat();
        pitch    = in.readFloat();
        onGround = in.readByte()!=0;
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeDouble( x );
        out.writeDouble( y );
        out.writeDouble( stance );
        out.writeDouble( z );
        out.writeFloat( yaw );
        out.writeFloat( pitch );
        out.write( onGround ? 1 : 0);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getStance() {
        return stance;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public boolean isOnGround() {
        return onGround;
    }
}
