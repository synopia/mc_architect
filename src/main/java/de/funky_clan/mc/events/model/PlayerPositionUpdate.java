package de.funky_clan.mc.events.model;

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

    private boolean blockChanged;
    private boolean chunkChanged;

    public PlayerPositionUpdate(double x, double y, double z, float yaw, float pitch, boolean blockChanged, boolean chunkChanged) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.blockChanged = blockChanged;
        this.chunkChanged = chunkChanged;
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

    public boolean isBlockChanged() {
        return blockChanged;
    }

    public boolean isChunkChanged() {
        return chunkChanged;
    }
}
