package de.funky_clan.mc.events;

import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.model.Ore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author synopia
 */
public class OreFound implements Event {
    private List<Ore> ores = new ArrayList<Ore>();

    public OreFound(List<Ore> ores) {
        this.ores = ores;
    }

    public List<Ore> getOres() {
        return ores;
    }

    @Override
    public Object getChannel() {
        return null;
    }
}
