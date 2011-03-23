package de.funky_clan.mc.events.swing;

import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.eventbus.SwingEvent;

import java.awt.*;

/**
 * @author synopia
 */
public final class ColorChanged implements SwingEvent {
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
}
