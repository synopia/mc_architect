package de.funky_clan.mc.events.background;

import de.funky_clan.mc.eventbus.BackgroundEvent;
import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.model.Ore;

import java.util.List;

/**
 * @author synopia
 */
public class OreFound implements BackgroundEvent {
    private long  chunkId;
    private List<Ore> ores;

    public OreFound(long chunkId, List<Ore> ores) {
        this.chunkId = chunkId;
        this.ores = ores;
    }

    public long getChunkId() {
        return chunkId;
    }

    public List<Ore> getOres() {
        return ores;
    }
}
