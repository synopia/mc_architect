package de.funky_clan.mc.events.swing;

import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.model.Ore;

import java.util.List;

/**
 * @author synopia
 */
public class OreDisplayUpdate implements Event {
    private List<Ore> ore;

    public OreDisplayUpdate(List<Ore> ore) {
        this.ore = ore;
    }

    public List<Ore> getOre() {
        return ore;
    }
}
