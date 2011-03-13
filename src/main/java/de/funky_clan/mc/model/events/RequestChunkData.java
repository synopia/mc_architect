package de.funky_clan.mc.model.events;

import de.funky_clan.mc.eventbus.Event;

/**
 * @author synopia
 */
public class RequestChunkData implements Event {
    private int chunkX;
    private int chunkZ;

    public RequestChunkData(int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    @Override
    public Object getChannel() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
