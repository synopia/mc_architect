package de.funky_clan.mc.ui.events;

import de.funky_clan.mc.net.protocol.events.PlayerPositionUpdate;

/**
 * @author synopia
 */
public class PlayerMoved extends PlayerPositionUpdate {
    private int yShift;

    public PlayerMoved(double x, double y, double z, float yaw, float pitch, int yShift) {
        super(x, y, z, yaw, pitch);
        this.yShift = yShift;
    }

    @Override
    public double getY() {
        return super.getY() + yShift;
    }

    public int getShift() {
        return yShift;
    }
}
