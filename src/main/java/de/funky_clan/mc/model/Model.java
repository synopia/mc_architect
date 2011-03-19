package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.BlockUpdate;
import de.funky_clan.mc.events.ChunkUpdate;
import de.funky_clan.mc.events.UnloadChunk;
import de.funky_clan.mc.math.Position;
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
    private EventBus eventBus;

    public interface BlockUpdateCallable {
        void updateBlock( Chunk chunk, int x, int y, int z, int value );
    }

    @Inject
    public Model(final EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.registerCallback(BlockUpdate.class, new EventHandler<BlockUpdate>() {
            @Override
            public void handleEvent(BlockUpdate event) {
                setPixel(event.getX(), event.getY(), event.getZ(), PixelType.BLOCK_ID, event.getType() );
            }
        });
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

    public void interate( ChunkUpdate event, BlockUpdateCallable callable ) {
        interate(event.getSx(), event.getSy(), event.getSz(), event.getSizeX(), event.getSizeY(), event.getSizeZ(), event.getData(), callable);
    }

    public void interate( int sx, int sy, int sz, int sizeX, int sizeY, int sizeZ, byte[] data, BlockUpdateCallable callable ) {
        if( sizeX==16 && sizeY==128 && sizeZ==16 ) {
            Chunk chunk = getOrCreateChunk(sx, sy, sz);
            int len = 16*128*16;
            for (int i = 0; i < len; i++) {
                int x = sx + (i>>11);
                int y = i & 0x7f;
                int z = sz + ((i&0x780)>>7);
                callable.updateBlock(chunk, x, y, z, data[i]);
            }
        } else {
            for( int x=0; x<sizeX; x++ ) {
                for( int y=0; y<sizeY; y++ ) {
                    for( int z=0; z<sizeZ; z++ ) {
                        int i = y + (z*sizeY) + x * sizeY * sizeZ;
                        Chunk chunk = getOrCreateChunk(sx + x, sy + y, sz + z);
                        callable.updateBlock(chunk, sx + x, sy + y, sz + z, data[i]);
                    }
                }
            }
        }

    }
    public void setBlock( int sx, int sy, int sz, int sizeX, int sizeY, int sizeZ, byte[] data ) {
        interate(sx, sy, sz, sizeX, sizeY, sizeZ, data, new BlockUpdateCallable() {
            @Override
            public void updateBlock(Chunk chunk, int x, int y, int z, int value) {
                chunk.setPixelGlobal(x,y,z, PixelType.BLOCK_ID, value );
            }
        });

    }

    public void setPixel( int x, int y, int z, PixelType type, int value ) {
        getOrCreateChunk(x,y,z).setPixelGlobal(x,y,z, type, value);
    }
    public int getPixel( int x, int y, int z, PixelType type ) {
        int chunkX = x>>4;
        int chunkZ = z>>4;

        return getChunk(chunkX, chunkZ).getPixelGlobal(x,y,z, type);
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

    public Chunk getOrCreateChunk(Position pos) {
        return getOrCreateChunk((int) pos.getWorldX(), (int)pos.getWorldY(), (int)pos.getWorldZ() );
    }
    public Chunk getOrCreateChunk(int x, int y, int z) {
        int chunkX = x>>4;
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
            chunk = new Chunk( chunkX<<4, 0, chunkZ<<4, 1<<4, 1<<7, 1<<4 );
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
