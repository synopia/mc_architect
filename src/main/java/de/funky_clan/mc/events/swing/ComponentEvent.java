package de.funky_clan.mc.events.swing;

import de.funky_clan.mc.eventbus.Event;

import javax.swing.*;

/**
 * @author synopia
 */
public abstract class ComponentEvent implements Event {
    private JComponent component;

    protected ComponentEvent(JComponent component) {
        this.component = component;
    }

    public JComponent getComponent() {
        return component;
    }
}
