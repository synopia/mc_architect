package de.funky_clan.mc.events.swing;

import de.funky_clan.mc.events.network.PlayerPositionUpdate;

/**
 * @author synopia
 */
public class PlayerMoved extends PlayerPositionUpdate {
    private int yShift;
    private boolean blockChanged;
    private boolean chunkChanged;

    public PlayerMoved(double x, double y, double z, float yaw, float pitch, int yShift, boolean blockChanged, boolean chunkChanged) {
        super(x, y, z, yaw, pitch);
        this.yShift = yShift;
        this.blockChanged = blockChanged;
        this.chunkChanged = chunkChanged;
    }

    @Override
    public double getY() {
        return super.getY() + yShift;
    }

    public boolean isBlockChanged() {
        return blockChanged;
    }

    public boolean isChunkChanged() {
        return chunkChanged;
    }

    public int getShift() {
        return yShift;
    }
}
