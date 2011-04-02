package de.funky_clan.mc.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.eventbus.NetworkEvent;
import de.funky_clan.mc.eventbus.VetoHandler;
import de.funky_clan.mc.events.model.PlayerPositionUpdate;
import de.funky_clan.mc.events.script.SendScriptData;
import de.funky_clan.mc.model.Box;
import de.funky_clan.mc.model.Chunk;
import static de.funky_clan.mc.model.Chunk.CHUNK_ARRAY_SIZE;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.net.MinecraftServer;
import de.funky_clan.mc.net.packets.BlockMultiUpdate;
import de.funky_clan.mc.net.packets.BlockSignUpdate;
import de.funky_clan.mc.net.packets.BlockUpdate;
import de.funky_clan.mc.net.packets.ChunkData;
import de.funky_clan.mc.scripts.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author synopia
 */
@Singleton
public class BlueprintInjectorService {
    private final Logger logger = LoggerFactory.getLogger(BlueprintInjectorService.class);
    private final EventDispatcher eventDispatcher;
    @Inject
    private Model model;
    private final HashMap<Long, BlockMultiUpdate> updates    = new HashMap<Long, BlockMultiUpdate>();
    @Inject @Named("SelectionBox")
    private Box selectionBox;
    private int lastX = Integer.MAX_VALUE;
    private int lastY = Integer.MAX_VALUE;
    private int lastZ = Integer.MAX_VALUE;
    private byte lastBlockId = 0;

    private MinecraftServer server;

    @Inject
    public BlueprintInjectorService(final EventDispatcher eventDispatcher, final ModelEventBus eventBus, final MinecraftServer server) {
        this.eventDispatcher = eventDispatcher;
        this.server          = server;

        server.subscribe(PlayerPositionUpdate.class, new EventHandler<PlayerPositionUpdate>() {
            @Override
            public void handleEvent(PlayerPositionUpdate event) {
                double x = event.getX();
                double y = event.getY()+1.8;
                double z = event.getZ();
                double pitch = Math.toRadians(-event.getPitch());
                double yaw   = Math.toRadians(event.getYaw()+90);

                double cosYaw   = Math.cos(yaw);
                double cosPitch = Math.cos(pitch);
                double sinYaw   = Math.sin(yaw);
                double sinPitch = Math.sin(pitch);

                double nx = 0.5*cosYaw*cosPitch;
                double ny = 0.5*sinPitch;
                double nz = 0.5*sinYaw*cosPitch;
                int xx=0;
                int yy=0;
                int zz=0;
                int blueprint = 0;
                boolean found = false;
                boolean valid = false;
                for( int i=0; i<10; i++ ) {
                    x += nx;
                    y += ny;
                    z += nz;
                    int pixel = model.getPixel((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z), 0);
                    blueprint = model.getPixel((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z), 1);
                    if( blueprint > 0 ) {
                        xx = (int)Math.floor(x);
                        yy = (int)Math.floor(y);
                        zz = (int)Math.floor(z);
                        found = true;
                        valid = pixel==0;
                        break;
                    }
                }
                if( xx!=lastX || yy!=lastY || zz!=lastZ ) {
                    if( lastBlockId>0 ) {
                        byte oldPixel = (byte) model.getPixelOrBlueprint(lastX, lastY, lastZ);
//                        logger.info(String.format("reseting old %d, %d, %d, %d", lastX, lastY, lastZ, oldPixel));
                        getUpdate(lastX, lastY, lastZ).add(lastX, lastY, lastZ, oldPixel, (byte) 0);
                        lastBlockId = 0;
                    }
                    if( found ) {
                        if( valid ) {
                            byte pixel = (byte) model.getPixel(xx, yy, zz, 0);
//                            logger.info(String.format("hiding %d, %d, %d, %d", xx, yy, zz, pixel));
                            if( pixel==0 ) {
                                getUpdate(xx, yy, zz).add( xx, yy, zz, pixel, (byte) 0);
                                lastBlockId = 1;
                            }
                        } else {
                            byte oldPixel = (byte) model.getPixelOrBlueprint(xx, yy, zz);
//                            logger.info(String.format("invalid, set %d, %d, %d, %d", xx, yy, zz, oldPixel));
                            getUpdate(xx, yy, zz).add( xx, yy, zz, oldPixel, (byte) 0);
                        }
                    }
                }

                lastX = xx;
                lastY = yy;
                lastZ = zz;

                publishUpdates();
            }
        });
        eventBus.subscribe(SendScriptData.class, new EventHandler<SendScriptData>() {
            @Override
            public void handleEvent(SendScriptData event) {
                Script script = event.getScript();
                HashMap<Long, BlockMultiUpdate> chunkUpdates = script.getChunkUpdates();

                for (BlockMultiUpdate update : chunkUpdates.values()) {
                    server.publish(update);
                }

                script.setSent(true);
            }
        });
        server.subscribe(BlockMultiUpdate.class, new EventHandler<BlockMultiUpdate>() {
            @Override
            public void handleEvent(BlockMultiUpdate event) {
//                logger.info(String.format("MultiUpdate chunk: %d, %d blocks: %d", event.getChunkX(), event.getChunkZ(), event.getSize()));
                event.dropPacket();
                final BlockMultiUpdate update = new BlockMultiUpdate(NetworkEvent.SERVER, event.getChunkX(), event.getChunkZ() );
                event.each(new BlockMultiUpdate.Each() {
                    @Override
                    public void update(int x, int y, int z, byte type, byte meta) {
                        byte pixel;
                        if( type>0 ) {
                            pixel = type;
                        } else {
                            if( lastBlockId>0 && lastX==x && lastY==y && lastZ==z ) {
                                pixel = 0;
//                                logger.info(String.format(" view window: %d, %d, %d, %d, %d ", x,y,z,type,meta));
                            } else {
                                pixel = (byte) model.getPixelOrBlueprint(x, y, z);
//                                logger.info(String.format(" pixel or blueprint: %d, %d, %d, %d, %d -> %d", x,y,z,type,meta, pixel));
                            }
                        }

                        update.add( x, y, z, pixel, meta);
                    }
                });
                server.sendPacket(update);
            }
        });
        server.subscribe(BlockUpdate.class, new EventHandler<BlockUpdate>() {
            @Override
            public void handleEvent(BlockUpdate event) {
//                logger.info(String.format("Update pos: %d, %d, %d, %d, %d ", event.getX(), event.getY(), event.getZ(), event.getType(), event.getMeta()));
                if( event.getType()==0 ) {
                    event.dropPacket();
                    int x = event.getX();
                    int y = event.getY();
                    int z = event.getZ();
                    byte pixel;
                    if( lastBlockId>0 && lastX==x && lastY==y && lastZ==z ) {
                        pixel = 0;
//                        logger.info(String.format(" view window: %d, %d, %d, %d, %d ", x,y,z,event.getType(), event.getMeta()));
                    } else {
                        pixel = (byte) model.getPixelOrBlueprint(x, y, z);
//                        logger.info(String.format(" pixel or blueprint: %d, %d, %d, %d, %d -> %d", x,y,z,event.getType(), event.getMeta(), pixel));
                    }
                    server.sendPacket(new BlockUpdate(NetworkEvent.SERVER, x, y, z, pixel, event.getMeta() ));
                }
            }
        });
        server.subscribe(ChunkData.class, new EventHandler<ChunkData>() {
            @Override
            public void handleEvent(final ChunkData event) {
//                logger.info("Chunk Data");
                final byte[] data = event.getData();
                model.interate(event,  new Model.BlockUpdateCallable() {
                    @Override
                    public void updateChunk(Chunk chunk) {
                        for (int i = 0; i < CHUNK_ARRAY_SIZE; i++) {
                            if( data[i]==0 ) {
                                data[i] = (byte) chunk.getPixelOrBlueprint(i);
                            }
                        }
                    }

                    @Override
                    public void updateBlock(Chunk chunk, int x, int y, int z, int index) {
                        if( data[index]==0 ) {
                            byte pixel;
                            if( lastBlockId>0 && lastX==x && lastY==y && lastZ==z ) {
                                pixel = 0;
//                                logger.info(String.format(" view window: %d, %d, %d, %d, %d ", x,y,z,data[index], -1));
                            } else {
                                pixel = (byte) model.getPixelOrBlueprint(x, y, z);
//                                logger.info(String.format(" pixel or blueprint: %d, %d, %d, %d, %d -> %d", x,y,z,data[index], -1, pixel));
                            }
                            data[index] = pixel;
                        }
                    }
                });
            }
        });
/*
        eventDispatcher.subscribeVeto(eventBus, BlockSignUpdate.class, new VetoHandler<BlockSignUpdate>() {
            @Override
            public boolean isVeto(BlockSignUpdate event) {
                return true;
            }

            @Override
            public void handleVeto(BlockSignUpdate event) {
                eventDispatcher.publish(event, true);
            }
        });
        eventDispatcher.subscribeVeto(eventBus, ChunkData.class, new VetoHandler<ChunkData>() {
            @Override
            public boolean isVeto(ChunkData event) {
                return true;
            }

            @Override
            public void handleVeto(final ChunkData event) {
                final byte newMap[] = event.getData();

                model.interate(event.getX(), event.getY(), event.getZ(), event.getSizeX(), event.getSizeY(),
                        event.getSizeZ(), new Model.BlockUpdateCallable() {
                            @Override
                            public void updateChunk(Chunk chunk) {
                                byte[] map = chunk.getMap();

                                for (int i = 0; i < CHUNK_ARRAY_SIZE; i++) {
                                    byte value = newMap[i];
                                    byte blueprint = map[i + CHUNK_ARRAY_SIZE];

                                    if ((blueprint > 0) && (value == DataValues.AIR.getId())) {
                                        value = blueprint;
                                    }

                                    newMap[i] = value;
                                }
                            }

                            @Override
                            public void updateBlock(Chunk chunk, int x, int y, int z, int index) {
                                byte value = newMap[index];
                                int blueprint = chunk.getPixel(x, y, z, 1);

                                if ((blueprint > 0) && (value == DataValues.AIR.getId())) {
                                    value = (byte) blueprint;
                                }

                                newMap[index] = value;
                            }
                        });
                eventDispatcher.publish(new ChunkData(event.getSource(), event.getX(), event.getY(), event.getZ(),
                        event.getSizeX(), event.getSizeY(), event.getSizeZ(), newMap), true);
            }
        });

*/
    }

    private void removeBlueprintAroundPlayer( PlayerPositionUpdate event ) {
        int px = event.getBlockX();
        int py = event.getBlockY();
        int pz = event.getBlockZ();

        for( int x = -3; x <= 3; x++ ) {
            for( int y = -3; y <= 3; y++ ) {
                for( int z = -3; z <= 3; z++ ) {
                    int rx = x + px;
                    int ry = y + py;
                    int rz = z + pz;

                    if(( ry < 0 ) || ( ry > 127 )) {
                        continue;
                    }

                    int pixel     = model.getPixel( rx, ry, rz, 0 );
                    int blueprint = model.getPixel( rx, ry, rz, 1 );

                    if(( blueprint < 1 ) || ( pixel < 0 )) {
                        continue;
                    }

                    BlockMultiUpdate update = getUpdate( rx, ry, rz );

                    if(( x == -3 ) || ( x == 3 ) || ( y == -3 ) || ( y == 3 ) || ( z == -3 ) || ( z == 3 )) {
                        update.add( rx, ry, rz, (byte) (( pixel > 0 )
                                ? pixel
                                : blueprint ), (byte) 0 );
                    } else {
                        update.add( rx, ry, rz, (byte) pixel, (byte) 0 );
                    }
                }
            }
        }

        publishUpdates();
    }

    private void publishUpdates() {
        for( BlockMultiUpdate update : updates.values() ) {
            if( update.getSize() > 1 ) {
                server.sendPacket(update);
            } else if( update.getSize()>0 ) {
                update.each(new BlockMultiUpdate.Each() {
                    @Override
                    public void update(int x, int y, int z, byte type, byte meta) {
                        server.sendPacket(new BlockUpdate(NetworkEvent.SERVER, x, y, z, type, meta));

                    }
                });
            }
        }

        updates.clear();
    }

    private BlockMultiUpdate getUpdate( int rx, int ry, int rz ) {
        int              chunkX = rx >> 4;
        int              chunkZ = rz >> 4;
        long             id     = Chunk.getChunkId(chunkX, chunkZ);
        BlockMultiUpdate update;

        if( updates.containsKey( id )) {
            update = updates.get( id );
        } else {
            update = new BlockMultiUpdate( NetworkEvent.SERVER, chunkX, chunkZ );
            updates.put( id, update );
        }

        return update;
    }
}
