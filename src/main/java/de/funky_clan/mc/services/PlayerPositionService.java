package de.funky_clan.mc.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.eventbus.NetworkEvent;
import de.funky_clan.mc.events.model.PlayerPositionUpdate;
import de.funky_clan.mc.model.Chunk;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.net.packets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author synopia
 */
@Singleton
public class PlayerPositionService {
    private final Logger logger = LoggerFactory.getLogger(PlayerPositionService.class);
    private double x;
    private double y;
    private double z;

    private double lastX;
    private double lastY;
    private double lastZ;

    private float yaw;
    private float pitch;

    private int entityId = -1;
    private int attachedId = -1;

    private int yShift = 0;
    @Inject
    private Model model;

    @Inject
    private EventDispatcher eventDispatcher;
    private HashMap<Long, BlockMultiUpdate> updates = new HashMap<Long, BlockMultiUpdate>();

    @Inject
    public PlayerPositionService(final ModelEventBus eventBus) {
        logger.info("Starting PlayerPositionService...");
        eventBus.registerCallback(PlayerPositionUpdate.class, new EventHandler<PlayerPositionUpdate>() {
            @Override
            public void handleEvent(PlayerPositionUpdate event) {

                if( event.isBlockChanged() ) {
                    removeBlueprintAroundPlayer(event);
                }
            }
        });
        eventBus.registerCallback(LoginRequest.class, new EventHandler<LoginRequest>() {
            @Override
            public void handleEvent(LoginRequest event) {
                entityId = event.getEntityId();
            }
        });
        eventBus.registerCallback(EntityAttach.class, new EventHandler<EntityAttach>() {
            @Override
            public void handleEvent(EntityAttach event) {
                if (event.getEntityId() == entityId) {
                    attachedId = event.getVehicleId();
                }
            }
        });
        eventBus.registerCallback(EntityLook.class, new EventHandler<EntityLook>() {
            @Override
            public void handleEvent(EntityLook event) {

            }
        });
        eventBus.registerCallback(EntityRelativeMove.class, new EventHandler<EntityRelativeMove>() {
            @Override
            public void handleEvent(EntityRelativeMove event) {
                if( event.getEid()==attachedId ) {
                    x += event.getDx()/32.;
                    y += event.getDy()/32.;
                    z += event.getDz()/32.;
                    firePositionUpdate();
                }
            }
        });
        eventBus.registerCallback(EntityRelativeMoveAndLook.class, new EventHandler<EntityRelativeMoveAndLook>() {
            @Override
            public void handleEvent(EntityRelativeMoveAndLook event) {
                if( event.getEid()==attachedId ) {
                    x += event.getDx()/32.;
                    y += event.getDy()/32.;
                    z += event.getDz()/32.;
                    firePositionUpdate();
                }
            }
        });
        eventBus.registerCallback(EntityTeleport.class, new EventHandler<EntityTeleport>() {
            @Override
            public void handleEvent(EntityTeleport event) {
                if( event.getEid()==attachedId ) {
                    x = event.getX()/32.;
                    y = event.getY()/32.;
                    z = event.getZ()/32.;
                    firePositionUpdate();
                }
            }
        });

        eventBus.registerCallback(PlayerPosition.class, new EventHandler<PlayerPosition>() {
            @Override
            public void handleEvent(PlayerPosition event) {
                if( event.getY()!=-999 ) {
                    x = event.getX();
                    y = event.getY();
                    z = event.getZ();
                }
                firePositionUpdate();
            }
        });
        eventBus.registerCallback(PlayerLook.class, new EventHandler<PlayerLook>() {
            @Override
            public void handleEvent(PlayerLook event) {
                yaw = event.getYaw();
                pitch = event.getPitch();

                firePositionUpdate();
            }
        });
        eventBus.registerCallback(PlayerPositionAndLook.class, new EventHandler<PlayerPositionAndLook>() {
            @Override
            public void handleEvent(PlayerPositionAndLook event) {
                yaw = event.getYaw();
                pitch = event.getPitch();

                if( event.getY()!=-999 ) {
                    x = event.getX();
                    y = event.getY();
                    z = event.getZ();
                }

                firePositionUpdate();

            }
        });
        eventBus.registerCallback(PlayerSpawnPosition.class, new EventHandler<PlayerSpawnPosition>() {
            @Override
            public void handleEvent(PlayerSpawnPosition event) {
                x = event.getX();
                y = event.getY();
                z = event.getZ();
                firePositionUpdate();

            }
        });
    }

    private void removeBlueprintAroundPlayer(PlayerPositionUpdate event) {
        int px = (int) Math.floor(event.getX());
        int py = (int) Math.floor(event.getY());
        int pz = (int) Math.floor(event.getZ());

        for( int x=-3; x<=3; x++ ) {
            for( int y=-3; y<=3; y++ ) {
                for( int z=-3; z<=3; z++ ) {
                    int rx = x+px;
                    int ry = y+py;
                    int rz = z+pz;
                    if( ry<0 || ry>127 ) {
                        continue;
                    }

                    int pixel     = model.getPixel(rx, ry, rz, 0);
                    int blueprint = model.getPixel(rx, ry, rz, 1);
                    if( blueprint<1 || pixel<0 ) {
                        continue;
                    }

                    BlockMultiUpdate update = getUpdate(rx,ry,rz);
                    if( x==-3 || x==3 || y==-3 || y==3 || z==-3 || z==3 ) {
                        update.add(rx, ry, rz, (byte) (pixel>0?pixel:blueprint), (byte) 0);
                    } else {
                        update.add(rx, ry, rz, (byte) pixel, (byte) 0);
                    }
                }
            }
        }
        for (BlockMultiUpdate update : updates.values()) {
            if( update.getSize()>0 ) {
                eventDispatcher.fire(update);
            }
        }
        updates.clear();
    }

    private BlockMultiUpdate getUpdate(int rx, int ry, int rz) {
        int chunkX = rx >> 4;
        int chunkZ = rz >> 4;
        long id = Chunk.getChunkId(chunkX, chunkZ);
        BlockMultiUpdate update;
        if( updates.containsKey(id) ) {
            update = updates.get(id);
        } else {
            update = new BlockMultiUpdate(NetworkEvent.SERVER, chunkX, chunkZ );
            updates.put(id, update);
        }
        return update;
    }

    private void firePositionUpdate() {
        double oldX = lastX;
        double oldY = lastY;
        double oldZ = lastZ;
        lastX = x;
        lastY = y;
        lastZ = z;

        boolean blockChanged = (int)lastX!=(int)oldX || (int)lastY!=(int)oldY || (int)lastZ!=(int)oldZ;
        boolean chunkChanged = Chunk.getChunkId(oldX, oldZ)!=Chunk.getChunkId(lastX, lastZ);

        eventDispatcher.fire(new PlayerPositionUpdate(x, y+ yShift, z, yaw, pitch, blockChanged, chunkChanged));
    }

    public void setYShift(int yShift) {
        this.yShift = yShift;
        firePositionUpdate();
    }
}
