package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.ChunkUpdate;
import de.funky_clan.mc.events.UnloadChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author synopia
 */
public class Model {
    private HashMap<Integer, BackgroundImage> zSliceImages = new HashMap<Integer, BackgroundImage>();
    private HashMap<Integer, HashMap<Integer, Chunk>> chunks = new HashMap<Integer, HashMap<Integer, Chunk>>();
    private final Logger log = LoggerFactory.getLogger(Model.class);
    private EventBus eventBus = EventDispatcher.getDispatcher().getModelEventBus();

    public Model() {
        eventBus.registerCallback(ChunkUpdate.class, new EventHandler<ChunkUpdate>() {
            @Override
            public void handleEvent(ChunkUpdate event) {
                setBlock(event.getSx(), event.getSy(), event.getSz(), event.getSizeX(), event.getSizeY(), event.getSizeZ(), event.getData());
            }
        });
        eventBus.registerCallback(UnloadChunk.class, new EventHandler<UnloadChunk>() {
            @Override
            public void handleEvent(UnloadChunk event) {
                int chunkX = event.getChunkX();
                int chunkZ = event.getChunkZ();
                removeChunk(chunkX,chunkZ);

            }
        });
    }

    public void setBlock( int sx, int sy, int sz, int sizeX, int sizeY, int sizeZ, byte[] data ) {
        if( sizeX==16 && sizeY==128 && sizeZ==16 ) {
            Chunk chunk = getOrCreateChunk(sx, sy, sz);
            int len = 16*128*16;
            for (int i = 0; i < len; i++) {
                int x = sx + (i>>11);
                int y = i & 0x7f;
                int z = sz + ((i&0x780)>>7);
                chunk.setPixelGlobal(x,y,z, data[i] );
            }
        } else {
            for( int x=0; x<sizeX; x++ ) {
                for( int y=0; y<sizeY; y++ ) {
                    for( int z=0; z<sizeZ; z++ ) {
                        int i = y + (z*sizeY) + x * sizeY * sizeZ;
                        setPixel(sx + x, sy + y, sz + z, data[i]);
                    }
                }
            }
        }
    }

    public void setPixel( int x, int y, int z, int value ) {
        getOrCreateChunk(x, y, z).setPixelGlobal(x, y, z, value);
    }

    public int getPixel( int x, int y, int z ) {
        int chunkX = x>>4;
        int chunkZ = z>>4;

        return getChunk(chunkX, chunkZ).getPixelGlobal(x, y, z);
    }

    private void removeChunk( int chunkX, int chunkZ ) {
        HashMap<Integer, Chunk> zChunks;
        if(chunks.containsKey(chunkZ)) {
            zChunks = chunks.get(chunkZ);
            if( zChunks.containsKey(chunkX) ) {
                zChunks.remove(chunkX);
            }
        }
    }

    private Chunk getChunk(int chunkX, int chunkZ) {
        HashMap<Integer, Chunk> zChunks;
        if(chunks.containsKey(chunkZ)) {
            zChunks = chunks.get(chunkZ);
        } else {
            return Chunk.EMPTY;
        }
        if( zChunks.containsKey(chunkX) ) {
            return zChunks.get(chunkX);
        } else {
            return Chunk.EMPTY;
        }
    }

    public Chunk getOrCreateChunk(int x, int y, int z) {
        int chunkX = x>>4;
        int chunkY = y>>7;
        int chunkZ = z>>4;
        return getOrCreateChunk(chunkX, chunkZ);
    }

    public Chunk getOrCreateChunk(int chunkX, int chunkZ) {
        Chunk chunk;
        HashMap<Integer, Chunk> zChunks;

        if( chunks.containsKey(chunkZ) ) {
            zChunks = chunks.get(chunkZ);
        } else {
            zChunks = new HashMap<Integer, Chunk>();
            chunks.put(chunkZ, zChunks);
        }

        if( zChunks.containsKey(chunkX) ) {
            chunk = zChunks.get(chunkX);
        } else {
            chunk = new Chunk(chunkX<<4, 0, chunkZ<<4, 1<<4, 1<<7, 1<<4 );
            zChunks.put(chunkX, chunk);
        }

        return chunk;
    }

    public void addImage( SliceType type, int slice, BackgroundImage image ) {
        zSliceImages.put( slice, image );
    }

    public BackgroundImage getImage( SliceType type, int slice ) {
        BackgroundImage result;

        switch( type ) {
            case Z:
                result = zSliceImages.get( slice );

                break;

            default:
                result = null;
        }

        return result;
    }
}
