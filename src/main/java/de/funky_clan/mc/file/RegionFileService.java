package de.funky_clan.mc.file;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.eventbus.NetworkEvent;
import de.funky_clan.mc.events.model.PlayerPositionUpdate;
import static de.funky_clan.mc.model.Chunk.getChunkId;
import static de.funky_clan.mc.model.Chunk.getChunkXForId;
import static de.funky_clan.mc.model.Chunk.getChunkYForId;

import de.funky_clan.mc.net.packets.P051ChunkData;
import de.funky_clan.mc.net.packets.P050ChunkPreparation;
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
@Singleton
public class RegionFileService {
    private static final int      SIZE         = 10;
    @SuppressWarnings( {"FieldCanBeLocal"} )
    private final Logger          logger       = LoggerFactory.getLogger( RegionFileService.class );
    private final ArrayList<Long> loadedChunks = new ArrayList<Long>();
    @Inject
    private EventDispatcher       eventDispatcher;

    @Inject
    public RegionFileService( final ModelEventBus eventBus ) {
        logger.info( "Region File Service started" );
        eventBus.subscribe(PlayerPositionUpdate.class, new EventHandler<PlayerPositionUpdate>() {
            @Override
            public void handleEvent(PlayerPositionUpdate event) {
                updatePlayerPos(event.getBlockX(), event.getBlockZ());
            }
        });
    }

    protected void updatePlayerPos( int x, int z ) {
        int        playerChunkX = x >> 4;
        int        playerChunkZ = z >> 4;
        List<Long> toRemove     = new ArrayList<Long>( loadedChunks );

        for( int chunkX = playerChunkX - SIZE; chunkX <= playerChunkX + SIZE; chunkX++ ) {
            for( int chunkZ = playerChunkZ - SIZE; chunkZ <= playerChunkZ + SIZE; chunkZ++ ) {
                long id = getChunkId( chunkX, chunkZ );

                if( loadedChunks.contains( id )) {
                    toRemove.remove( id );
                } else {
                    load( chunkX, chunkZ );
                    loadedChunks.add( id );
                }
            }
        }

        logger.info( "unloading " + toRemove.size() + " chunks" );

        for( Long id : toRemove ) {
            eventDispatcher.publish(new P050ChunkPreparation(NetworkEvent.SERVER, getChunkXForId(id),
                    getChunkYForId(id), false));
        }

        loadedChunks.removeAll( toRemove );
    }

    public void load( int chunkX, int chunkZ ) {
        DataInputStream inputStream = RegionFileCache.getChunkDataInputStream( new File( "d:/games/minecraft/world" ),
                                          chunkX, chunkZ );

        if( inputStream != null ) {
            try {
                NBTInputStream nbt    = new NBTInputStream( inputStream );
                CompoundTag    root   = (CompoundTag) nbt.readTag();
                CompoundTag    level  = (CompoundTag) root.getValue().get( "Level" );
                ByteArrayTag   blocks = (ByteArrayTag) level.getValue().get( "Blocks" );

                eventDispatcher.publish(new P051ChunkData(P051ChunkData.SERVER, chunkX << 4, 0, chunkZ << 4, 1 << 4, 1 << 7,
                        1 << 4, blocks.getValue()));
            } catch( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    protected String getChunkName( int x, int z ) {
        return x + "," + z;
    }
}
