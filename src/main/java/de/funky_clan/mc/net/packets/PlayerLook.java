package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class PlayerLook extends BasePacket {
    public static final int ID = 0x0c;
    private boolean         onGround;
    private float           pitch;
    private float           yaw;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        yaw      = in.readFloat();
        pitch    = in.readFloat();
        onGround = in.readBoolean();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeFloat( yaw );
        out.writeFloat( pitch );
        out.writeBoolean( onGround );
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
