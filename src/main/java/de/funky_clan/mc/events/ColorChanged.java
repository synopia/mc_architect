package de.funky_clan.mc.events;

import de.funky_clan.mc.eventbus.Event;

import java.awt.*;

/**
 * @author synopia
 */
public final class ColorChanged implements Event {
    private final int blockId;
    private final Color color;

    public ColorChanged(int blockId, Color color) {
        this.blockId = blockId;
        this.color = color;
    }

    public int getBlockId() {
        return blockId;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public Object getChannel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
