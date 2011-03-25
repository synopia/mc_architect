package de.funky_clan.mc.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.events.model.PlayerPositionUpdate;
import de.funky_clan.mc.model.Chunk;
import de.funky_clan.mc.net.MinecraftClient;
import de.funky_clan.mc.net.MinecraftServer;
import de.funky_clan.mc.net.packets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private int zShift = 0;

    @Inject
    private EventDispatcher eventDispatcher;

    @Inject
    public PlayerPositionService(final ModelEventBus eventBus) {
        logger.info("Starting PlayerPositionService...");
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

    private void firePositionUpdate() {
        z += zShift;
        double oldX = lastX;
        double oldY = lastY;
        double oldZ = lastZ;
        lastX = x;
        lastY = y;
        lastZ = z;

        boolean blockChanged = (int)lastX!=(int)oldX || (int)lastY!=(int)oldY || (int)lastZ!=(int)oldZ;
        boolean chunkChanged = Chunk.getChunkId(oldX, oldZ)!=Chunk.getChunkId(lastX, lastZ);

        eventDispatcher.fire(new PlayerPositionUpdate(x, y, z, yaw, pitch, blockChanged, chunkChanged));
    }

    public void setZShift(int zShift) {
        this.zShift = zShift;
        firePositionUpdate();
    }
}
