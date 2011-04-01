package de.funky_clan.mc.events.swing;

import javax.swing.JComponent;

/**
 * @author synopia
 */
public class OreFilterChanged extends ComponentEvent {
    private boolean[] filter;

    public OreFilterChanged( JComponent component, boolean[] filter ) {
        super( component );
        this.filter = filter;
    }

    public boolean[] getFilter() {
        return filter;
    }
}
