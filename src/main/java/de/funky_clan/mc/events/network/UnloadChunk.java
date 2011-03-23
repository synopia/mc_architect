package de.funky_clan.mc.events.network;

import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.eventbus.NetworkEvent;

/**
 * @author synopia
 */
public class UnloadChunk implements NetworkEvent {
    private int chunkX;
    private int chunkZ;

    public UnloadChunk(int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }
}
