package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class PlayerPosition extends BasePacket {
    public static final int ID = 0x0b;
    private boolean         onGround;
    private double          stance;
    private double          x;
    private double          y;
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
        onGround = in.read()!=0;
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeDouble( x );
        out.writeDouble( y );
        out.writeDouble( stance );
        out.writeDouble( z );
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

    public boolean isOnGround() {
        return onGround;
    }
}
