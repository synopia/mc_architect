package de.funky_clan.mc.net.protocol.events;

import de.funky_clan.mc.eventbus.Event;

/**
 * @author synopia
 */
public class PlayerPositionUpdate implements Event {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public PlayerPositionUpdate(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public Object getChannel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
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
}
