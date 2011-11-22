package de.funky_clan.mc.events.swing;

import javax.swing.*;

/**
 * @author synopia
 */
public class EntityFilterChanged extends ComponentEvent {
    private final boolean []filter;

    public EntityFilterChanged(JComponent component, boolean[] filter) {
        super(component);
        this.filter = filter;
    }

    public boolean[] getFilter() {
        return filter;
    }
}
