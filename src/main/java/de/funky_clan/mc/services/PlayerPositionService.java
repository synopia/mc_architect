package de.funky_clan.mc.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.events.model.PlayerPositionUpdate;
import de.funky_clan.mc.model.Chunk;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.net.packets.EntityAttach;
import de.funky_clan.mc.net.packets.EntityLook;
import de.funky_clan.mc.net.packets.EntityRelativeMove;
import de.funky_clan.mc.net.packets.EntityRelativeMoveAndLook;
import de.funky_clan.mc.net.packets.EntityTeleport;
import de.funky_clan.mc.net.packets.LoginRequest;
import de.funky_clan.mc.net.packets.PlayerLook;
import de.funky_clan.mc.net.packets.PlayerPosition;
import de.funky_clan.mc.net.packets.PlayerPositionAndLook;
import de.funky_clan.mc.net.packets.PlayerSpawnPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author synopia
 */
@Singleton
public class PlayerPositionService {
    private int                                   attachedId = -1;
    private int                                   entityId   = -1;
    @SuppressWarnings( {"FieldCanBeLocal"} )
    private final Logger                          logger     = LoggerFactory.getLogger( PlayerPositionService.class );
    private int                                   yShift     = 0;
    @Inject
    private EventDispatcher                       eventDispatcher;
    private double                                lastX;
    private double                                lastY;
    private double                                lastZ;
    @Inject
    private Model                                 model;
    private float                                 pitch;
    private double                                x;
    private double                                y;
    private float                                 yaw;
    private double                                z;

    @Inject
    public PlayerPositionService( final ModelEventBus eventBus ) {
        logger.info( "Starting PlayerPositionService..." );

        eventBus.subscribe(LoginRequest.class, new EventHandler<LoginRequest>() {
            @Override
            public void handleEvent(LoginRequest event) {
                entityId = event.getProtocolVersion();
            }
        });
        eventBus.subscribe(EntityAttach.class, new EventHandler<EntityAttach>() {
            @Override
            public void handleEvent(EntityAttach event) {
                if (event.getEntityId() == entityId) {
                    attachedId = event.getVehicleId();
                }
            }
        });
        eventBus.subscribe(EntityLook.class, new EventHandler<EntityLook>() {
            @Override
            public void handleEvent(EntityLook event) {
            }
        });
        eventBus.subscribe(EntityRelativeMove.class, new EventHandler<EntityRelativeMove>() {
            @Override
            public void handleEvent(EntityRelativeMove event) {
                if (event.getEid() == attachedId) {
                    x += event.getDx() / 32.;
                    y += event.getDy() / 32.;
                    z += event.getDz() / 32.;
                    firePositionUpdate();
                }
            }
        });
        eventBus.subscribe(EntityRelativeMoveAndLook.class, new EventHandler<EntityRelativeMoveAndLook>() {
            @Override
            public void handleEvent(EntityRelativeMoveAndLook event) {
                if (event.getEid() == attachedId) {
                    x += event.getDx() / 32.;
                    y += event.getDy() / 32.;
                    z += event.getDz() / 32.;
                    firePositionUpdate();
                }
            }
        });
        eventBus.subscribe(EntityTeleport.class, new EventHandler<EntityTeleport>() {
            @Override
            public void handleEvent(EntityTeleport event) {
                if (event.getEid() == attachedId) {
                    x = event.getX() / 32.;
                    y = event.getY() / 32.;
                    z = event.getZ() / 32.;
                    firePositionUpdate();
                }
            }
        });
        eventBus.subscribe(PlayerPosition.class, new EventHandler<PlayerPosition>() {
            @Override
            public void handleEvent(PlayerPosition event) {
                if (event.getY() != -999) {
                    x = event.getX();
                    y = event.getY();
                    z = event.getZ();
                }

                firePositionUpdate();
            }
        });
        eventBus.subscribe(PlayerLook.class, new EventHandler<PlayerLook>() {
            @Override
            public void handleEvent(PlayerLook event) {
                yaw = event.getYaw();
                pitch = event.getPitch();
                firePositionUpdate();
            }
        });
        eventBus.subscribe(PlayerPositionAndLook.class, new EventHandler<PlayerPositionAndLook>() {
            @Override
            public void handleEvent(PlayerPositionAndLook event) {
                yaw = event.getYaw();
                pitch = event.getPitch();

                if (event.getY() != -999) {
                    x = event.getX();
                    y = event.getY();
                    z = event.getZ();
                }

                firePositionUpdate();
            }
        });
        eventBus.subscribe(PlayerSpawnPosition.class, new EventHandler<PlayerSpawnPosition>() {
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
        double oldX = lastX;
        double oldY = lastY;
        double oldZ = lastZ;

        lastX = x;
        lastY = y;
        lastZ = z;

        boolean blockChanged = (int) lastX != (int) oldX || (int) lastY != (int) oldY || (int) lastZ != (int) oldZ;
        boolean chunkChanged = Chunk.getChunkId( oldX, oldZ ) != Chunk.getChunkId( lastX, lastZ );

        eventDispatcher.publish(new PlayerPositionUpdate(x, y + yShift, z, yaw, pitch, blockChanged, chunkChanged));
    }

    public void setYShift( int yShift ) {
        this.yShift = yShift;
        firePositionUpdate();
    }
}
