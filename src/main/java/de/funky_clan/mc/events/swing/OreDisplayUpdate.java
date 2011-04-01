package de.funky_clan.mc.events.swing;

import de.funky_clan.mc.model.Ore;

import javax.swing.JComponent;
import java.util.List;

/**
 * @author synopia
 */
public class OreDisplayUpdate extends ComponentEvent {
    private final List<Ore> ore;
    private final int       total;

    public OreDisplayUpdate( JComponent component, List<Ore> ore, int total ) {
        super( component );
        this.ore   = ore;
        this.total = total;
    }

    public List<Ore> getOre() {
        return ore;
    }

    public int getTotal() {
        return total;
    }
}
