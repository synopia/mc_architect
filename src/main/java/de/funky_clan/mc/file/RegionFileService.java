package de.funky_clan.mc.file;

import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.ChunkUpdate;
import de.funky_clan.mc.events.PlayerMoved;
import org.jnbt.ByteArrayTag;
import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author synopia
 */
public class RegionFileService {
    private static final int SIZE = 10;
    private EventBus eventBus = EventDispatcher.getDispatcher().getModelEventBus();
    private int playerX;
    private int playerZ;
    private ArrayList<String> loadedChunks = new ArrayList<String>();
    private Logger log = LoggerFactory.getLogger(RegionFileService.class);

    public RegionFileService() {
        eventBus.registerCallback(PlayerMoved.class, new EventHandler<PlayerMoved>() {
            @Override
            public void handleEvent(PlayerMoved event) {
                updatePlayerPos( (int) event.getX(), (int) event.getZ());
            }
        });
    }

    protected void updatePlayerPos(int x, int z) {
        playerX = x;
        playerZ = z;

        int playerChunkX = x>>4;
        int playerChunkZ = z>>4;

        List<String> toRemove = new ArrayList<String>(loadedChunks);

        for( int chunkX=playerChunkX-SIZE; chunkX<=playerChunkX+SIZE; chunkX++) {
            for( int chunkZ=playerChunkZ-SIZE; chunkZ<=playerChunkZ+SIZE; chunkZ++) {
                String chunkName = getChunkName(chunkX, chunkZ );
                if( loadedChunks.contains(chunkName) ) {
                    toRemove.remove(chunkName);
                } else {
                    load(chunkX, chunkZ);
                    loadedChunks.add(chunkName);
                }
            }
        }
        log.info("removing "+toRemove.size()+" chunks");
        for (String s : toRemove) {

        }
        loadedChunks.removeAll(toRemove);
    }

    public void load( int chunkX, int chunkZ ) {
        DataInputStream inputStream = RegionFileCache.getChunkDataInputStream(new File("d:/games/minecraft/world"), chunkX, chunkZ);
        try {
            NBTInputStream nbt = new NBTInputStream(inputStream);
            CompoundTag root=(CompoundTag) nbt.readTag();
            CompoundTag level = (CompoundTag) root.getValue().get("Level");
            ByteArrayTag blocks = (ByteArrayTag) level.getValue().get("Blocks");

            eventBus.fireEvent(new ChunkUpdate(chunkX << 4, 0, chunkZ << 4, 1 << 4, 1 << 7, 1 << 4, blocks.getValue()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected String getChunkName( int x, int z ) {
        return x+","+z;
    }
}
