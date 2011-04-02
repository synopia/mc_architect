package de.funky_clan.mc.model;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.eventbus.NetworkEvent;
import de.funky_clan.mc.events.model.ModelUpdate;
import de.funky_clan.mc.math.Position;
import static de.funky_clan.mc.model.Chunk.getChunkId;
import de.funky_clan.mc.net.packets.BlockMultiUpdate;
import de.funky_clan.mc.net.packets.BlockUpdate;
import de.funky_clan.mc.net.packets.ChunkData;
import de.funky_clan.mc.net.packets.ChunkPreparation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * <p>The world model works basically the same as in minecraft. There are chunks of blocks of a fixed size (16x128x16).
 * All currently loaded chunks are stored in a hashmap (chunkId => chunk).</p>
 *
 * <p>This class handles all chunk update packets sent from the server and publishes <code>ModelUpdate</code> events,
 * after the data is processed.</p>
 *
 * @author synopia
 */
@Singleton
public class Model {
    private final HashMap<Long, Chunk>            chunks  = new HashMap<Long, Chunk>();
    private final Logger                          log     = LoggerFactory.getLogger( Model.class );
    private final HashMap<Long, BlockMultiUpdate> updates = new HashMap<Long, BlockMultiUpdate>();
    private final EventDispatcher                 eventDispatcher;

    @Inject
    public Model( final EventDispatcher eventDispatcher, final ModelEventBus eventBus) {
        this.eventDispatcher = eventDispatcher;
        eventBus.subscribe(BlockUpdate.class, new EventHandler<BlockUpdate>() {
            @Override
            public void handleEvent(BlockUpdate event) {
                setPixel(event.getX(), event.getY(), event.getZ(), 0, event.getType());
                eventDispatcher.publish(new ModelUpdate(event.getX(), event.getY(), event.getZ(), 1, 1, 1));
            }
        });
        eventBus.subscribe(BlockMultiUpdate.class, new EventHandler<BlockMultiUpdate>() {
            @Override
            public void handleEvent(BlockMultiUpdate event) {
                event.each(new BlockMultiUpdate.Each() {
                    @Override
                    public void update(int x, int y, int z, byte type, byte meta) {
                        setPixel(x, y, z, 0, type);
                        eventDispatcher.publish(new ModelUpdate(x, y, z, 1, 1, 1));
                    }
                });
            }
        });
        eventBus.subscribe(ChunkData.class, new EventHandler<ChunkData>() {
            @Override
            public void handleEvent(ChunkData event) {
                setBlock(event.getX(), event.getY(), event.getZ(), event.getSizeX(), event.getSizeY(),
                        event.getSizeZ(), event.getData());
                eventDispatcher.publish(new ModelUpdate(event.getX(), event.getY(), event.getZ(), event.getSizeX(),
                        event.getSizeY(), event.getSizeZ()));
            }
        });
        eventBus.subscribe(ChunkPreparation.class, new EventHandler<ChunkPreparation>() {
            @Override
            public void handleEvent(ChunkPreparation event) {
                if (!event.isLoad()) {
                    int chunkX = event.getX();
                    int chunkZ = event.getZ();

                    removeChunk(chunkX, chunkZ);
                }
            }
        });
    }

    public int getNumberOfChunks() {
        return chunks.size();
    }

    public void interate( ModelUpdate event , BlockUpdateCallable callable ) {
        interate( event.getStartX(), event.getStartY(), event.getStartZ(), event.getSizeX(), event.getSizeY(), event.getSizeZ(), callable );
    }
    public void interate( ChunkData event , BlockUpdateCallable callable ) {
        interate( event.getX(), event.getY(), event.getZ(), event.getSizeX(), event.getSizeY(), event.getSizeZ(), callable );
    }
    public void interate( int sx, int sy, int sz, int sizeX, int sizeY, int sizeZ, BlockUpdateCallable callable ) {
        if(( sizeX == 16 ) && ( sizeY == 128 ) && ( sizeZ == 16 )) {
            Chunk chunk = getOrCreateChunkByCoords(sx, sy, sz);

            callable.updateChunk( chunk );
        } else {
            for( int x = 0; x < sizeX; x++ ) {
                for( int y = 0; y < sizeY; y++ ) {
                    for( int z = 0; z < sizeZ; z++ ) {
                        int   i     = y + ( z * sizeY ) + x * sizeY * sizeZ;
                        Chunk chunk = getOrCreateChunkByCoords(sx + x, sy + y, sz + z);

                        callable.updateBlock( chunk, sx + x, sy + y, sz + z, i );
                    }
                }
            }
        }
    }

    public void setBlock( int sx, int sy, int sz, int sizeX, int sizeY, int sizeZ, final byte[] data ) {
        interate( sx, sy, sz, sizeX, sizeY, sizeZ, new BlockUpdateCallable() {
            @Override
            public void updateChunk( Chunk chunk ) {
                chunk.updateFullBlock( 0, data );
            }
            @Override
            public void updateBlock( Chunk chunk, int x, int y, int z, int index ) {
                chunk.setPixel( x, y, z, 0, data[index] );
            }
        } );
    }

    public void setPixel( int x, int y, int z, int type, int value ) {
        if(( y < 0 ) || ( y >= 128 )) {
            return;
        }

        if(( type == 1 ) && ( getPixel( x, y, z, 0 ) == 0 )) {
            int              chunkX = x >> 4;
            int              chunkZ = z >> 4;
            long             id     = Chunk.getChunkId( chunkX, chunkZ );
            BlockMultiUpdate update;

            if( updates.containsKey( id )) {
                update = updates.get( id );
            } else {
                update = new BlockMultiUpdate( NetworkEvent.SERVER, chunkX, chunkZ );
                updates.put( id, update );
            }

            update.add( x, y, z, (byte) value, (byte) 0 );
        }

        getOrCreateChunkByCoords(x, y, z).setPixel(x, y, z, type, value);
    }
    public final int getPixelOrBlueprint( int x, int y, int z ) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;

        if(( y < 0 ) || ( y >= 128 )) {
            return -1;
        }

        Chunk chunk = getChunk( chunkX, chunkZ );

        if( chunk != null ) {
            return chunk.getPixelOrBlueprint( x, y, z);
        } else {
            return -1;
        }

    }
    public int getPixel( int x, int y, int z, int type ) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;

        if(( y < 0 ) || ( y >= 128 )) {
            return -1;
        }

        Chunk chunk = getChunk( chunkX, chunkZ );

        if( chunk != null ) {
            return chunk.getPixel( x, y, z, type );
        } else {
            return -1;
        }
    }

    private void removeChunk( int x, int y ) {
        long id = getChunkId(x, y);

        if( chunks.containsKey( id )) {
            chunks.remove( id );
        }
    }

    public Chunk getChunk( int x, int y ) {
        long id = getChunkId( x, y );

        return chunks.get( id );
    }

    public Chunk getChunk( long id ) {
        return chunks.get( id );
    }

    public Chunk getOrCreateChunk( Position pos ) {
        return getOrCreateChunkByCoords((int) pos.getWorldX(), (int) pos.getWorldY(), (int) pos.getWorldZ());
    }

    public Chunk getOrCreateChunkByCoords(int x, int y, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;

        return getOrCreateChunk(chunkX, chunkZ);
    }

    public Chunk getOrCreateChunk( int chunkX, int chunkZ ) {
        Chunk chunk;
        long  id = getChunkId( chunkX, chunkZ );

        if( chunks.containsKey( id )) {
            chunk = chunks.get( id );
        } else {
            chunk = new Chunk( chunkX << 4, chunkZ << 4, 1 << 4, 1 << 7, 1 << 4 );
            chunks.put( id, chunk );
        }

        return chunk;
    }

    public boolean isChunkLoaded( int chunkX, int chunkZ ) {
        return chunks.containsKey(Chunk.getChunkId(chunkX, chunkZ) );
    }

    public boolean isChunkLoaded( long id ) {
        return chunks.containsKey(id);
    }

    public void clearBlueprint() {
        for( Chunk chunk : chunks.values() ) {
            chunk.clearBlueprint();
        }
    }

    public HashMap<Long, BlockMultiUpdate> getUpdates() {
        HashMap<Long, BlockMultiUpdate> result = new HashMap<Long, BlockMultiUpdate>(updates);
        updates.clear();
        return result;
    }

    public interface BlockUpdateCallable {
        void updateChunk( Chunk chunk );

        void updateBlock( Chunk chunk, int x, int y, int z, int index );
    }
}
