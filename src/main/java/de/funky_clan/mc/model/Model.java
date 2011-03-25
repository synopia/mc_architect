package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.eventbus.VetoHandler;
import de.funky_clan.mc.events.model.ModelUpdate;
import de.funky_clan.mc.math.Position;
import de.funky_clan.mc.net.MinecraftServer;
import de.funky_clan.mc.net.packets.BlockMultiUpdate;
import de.funky_clan.mc.net.packets.BlockUpdate;
import de.funky_clan.mc.net.packets.ChunkData;
import de.funky_clan.mc.net.packets.ChunkPreparation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static de.funky_clan.mc.model.Chunk.CHUNK_ARRAY_SIZE;
import static de.funky_clan.mc.model.Chunk.getChunkId;

/**
 * @author synopia
 */
@Singleton
public class Model {
    private HashMap<Integer, BackgroundImage> zSliceImages = new HashMap<Integer, BackgroundImage>();
    private HashMap<Long, Chunk> chunks = new HashMap<Long, Chunk>();
    private final Logger log = LoggerFactory.getLogger(Model.class);

    private EventDispatcher eventDispatcher;

    public interface BlockUpdateCallable {
        void updateChunk( Chunk chunk );
        void updateBlock( Chunk chunk, int x, int y, int z, int index );
    }

    @Inject
    public Model(final EventDispatcher eventDispatcher, final ModelEventBus eventBus, final MinecraftServer server) {
        this.eventDispatcher = eventDispatcher;

        eventBus.registerCallback(BlockUpdate.class, new EventHandler<BlockUpdate>() {
            @Override
            public void handleEvent(BlockUpdate event) {
                setPixel(event.getX(), event.getY(), event.getZ(), 0, event.getType());
                eventDispatcher.fire(new ModelUpdate(event.getX(), event.getY(), event.getZ(), 1, 1, 1));
            }
        });
        eventBus.registerCallback(BlockMultiUpdate.class, new EventHandler<BlockMultiUpdate>() {
            @Override
            public void handleEvent(BlockMultiUpdate event) {
                event.each(new BlockMultiUpdate.Each() {
                    @Override
                    public void update(int x, int y, int z, int type, int meta) {
                        setPixel(x, y, z, 0, type);
                        eventDispatcher.fire(new ModelUpdate(x, y, z, 1, 1, 1));
                    }
                });
            }
        });
        eventBus.registerCallback(ChunkData.class, new EventHandler<ChunkData>() {
            @Override
            public void handleEvent(ChunkData event) {
                setBlock(event.getX(), event.getY(), event.getZ(), event.getSizeX(), event.getSizeY(), event.getSizeZ(), event.getData());
                eventDispatcher.fire(new ModelUpdate(event.getX(), event.getY(), event.getZ(), event.getSizeX(), event.getSizeY(), event.getSizeZ()));
            }
        });
        eventBus.registerCallback(ChunkPreparation.class, new EventHandler<ChunkPreparation>() {
            @Override
            public void handleEvent(ChunkPreparation event) {
                if( !event.isLoad() ) {
                    int chunkX = event.getX();
                    int chunkZ = event.getZ();
                    removeChunk(chunkX,chunkZ);
                }
            }
        });

        eventDispatcher.registerVetoHandler(eventBus, ChunkData.class, new VetoHandler<ChunkData>() {
            @Override
            public boolean isVeto(ChunkData event) {
                return true;
            }

            @Override
            public void handleVeto(final ChunkData event) {

                final byte newMap[] = event.getData();

                interate(event.getX(), event.getY(), event.getZ(), event.getSizeX(), event.getSizeY(), event.getSizeZ(), new BlockUpdateCallable() {
                    @Override
                    public void updateChunk(Chunk chunk) {
                        byte[] map = chunk.getMap();
                        for (int i = 0; i < CHUNK_ARRAY_SIZE; i++) {
                            byte value = newMap[i];
                            byte blueprint = map[i + CHUNK_ARRAY_SIZE];
                            if (blueprint > 0 && value == DataValues.AIR.getId()) {
                                value = blueprint;
                            }
                            newMap[i] = value;
                        }
                    }

                    @Override
                    public void updateBlock(Chunk chunk, int x, int y, int z, int index) {
                        byte value = newMap[index];
                        int blueprint = chunk.getPixel(x, y, z, 1);
                        if (blueprint > 0 && value == DataValues.AIR.getId()) {
                            value = (byte) blueprint;
                        }
                        newMap[index] = value;
                    }
                });

                eventDispatcher.fire(new ChunkData(event.getSource(), event.getX(), event.getY(), event.getZ(), event.getSizeX(), event.getSizeY(), event.getSizeZ(), newMap), true);
            }
        });
    }

    public void interate( int sx, int sy, int sz, int sizeX, int sizeY, int sizeZ, BlockUpdateCallable callable ) {
        if( sizeX==16 && sizeY==128 && sizeZ==16 ) {
            Chunk chunk = getOrCreateChunk(sx, sy, sz);
            callable.updateChunk(chunk);
        } else {
            for( int x=0; x<sizeX; x++ ) {
                for( int y=0; y<sizeY; y++ ) {
                    for( int z=0; z<sizeZ; z++ ) {
                        int i = y + (z*sizeY) + x * sizeY * sizeZ;
                        Chunk chunk = getOrCreateChunk(sx + x, sy + y, sz + z);
                        callable.updateBlock(chunk, sx + x, sy + y, sz + z, i);
                    }
                }
            }
        }
    }

    public void setBlock( int sx, int sy, int sz, int sizeX, int sizeY, int sizeZ, final byte[] data ) {
        interate(sx, sy, sz, sizeX, sizeY, sizeZ, new BlockUpdateCallable() {
            @Override
            public void updateChunk(Chunk chunk) {
                chunk.updateFullBlock(0, data);
            }

            @Override
            public void updateBlock(Chunk chunk, int x, int y, int z, int index) {
                chunk.setPixel(x, y, z, 0, data[index]);
            }
        });

    }

    public void setPixel(int x, int y, int z, int type, int value) {
        if( y<0 || y>=128 ) return;
        getOrCreateChunk(x,y,z).setPixel(x, y, z, type, value);
    }
    public int getPixel(int x, int y, int z, int type) {
        int chunkX = x>>4;
        int chunkZ = z>>4;
        if( y<0 || y>=128 ) return -1;

        Chunk chunk = getChunk(chunkX, chunkZ);
        if( chunk!=null ) {
            return chunk.getPixel(x, y, z, type);
        } else {
            return -1;
        }
    }

    private void removeChunk( int x, int y ) {
        long id = getChunkId(x, y);

        if(chunks.containsKey(id)) {
            chunks.remove(id);
        }
    }

    public Chunk getChunk(int x, int y) {
        long id = getChunkId(x, y);
        return chunks.get(id);
    }

    public Chunk getChunk(long id) {
        return chunks.get(id);
    }

    public Chunk getOrCreateChunk(Position pos) {
        return getOrCreateChunk((int) pos.getWorldX(), (int)pos.getWorldY(), (int)pos.getWorldZ() );
    }
    public Chunk getOrCreateChunk(int x, int y, int z) {
        int chunkX = x>>4;
        int chunkZ = z>>4;
        return getOrCreateChunk(chunkX, chunkZ);
    }

    public Chunk getOrCreateChunk(int x, int y) {
        Chunk chunk;
        long id = getChunkId(x, y);

        if( chunks.containsKey(id) ) {
            chunk = chunks.get(id);
        } else {
            chunk = new Chunk( x<<4, 0, y<<4, 1<<4, 1<<7, 1<<4 );
            chunks.put(id, chunk);
        }

        return chunk;
    }

    public void addImage( SliceType type, int slice, BackgroundImage image ) {
        zSliceImages.put( slice, image );
    }

    public BackgroundImage getImage( SliceType type, int slice ) {
        BackgroundImage result;

        switch( type ) {
            case Y:
                result = zSliceImages.get( slice );

                break;

            default:
                result = null;
        }

        return result;
    }

    public void clearBlueprint() {
        for (Chunk chunk : chunks.values()) {
            chunk.clearBlueprint();
        }
    }
}
