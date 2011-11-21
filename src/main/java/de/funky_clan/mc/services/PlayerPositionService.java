package de.funky_clan.mc.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.events.model.PlayerPositionUpdate;
import de.funky_clan.mc.model.Chunk;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.net.packets.P039EntityAttach;
import de.funky_clan.mc.net.packets.P032EntityLook;
import de.funky_clan.mc.net.packets.P031EntityRelativeMove;
import de.funky_clan.mc.net.packets.P033EntityRelativeMoveAndLook;
import de.funky_clan.mc.net.packets.P034EntityTeleport;
import de.funky_clan.mc.net.packets.P001LoginRequest;
import de.funky_clan.mc.net.packets.P012PlayerLook;
import de.funky_clan.mc.net.packets.P011PlayerPosition;
import de.funky_clan.mc.net.packets.P013PlayerPositionAndLook;
import de.funky_clan.mc.net.packets.P006PlayerSpawnPosition;
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

        eventBus.subscribe(P001LoginRequest.class, new EventHandler<P001LoginRequest>() {
            @Override
            public void handleEvent(P001LoginRequest event) {
                entityId = event.getProtocolVersion();
            }
        });
        eventBus.subscribe(P039EntityAttach.class, new EventHandler<P039EntityAttach>() {
            @Override
            public void handleEvent(P039EntityAttach event) {
                if (event.getEntityId() == entityId) {
                    attachedId = event.getVehicleId();
                }
            }
        });
        eventBus.subscribe(P032EntityLook.class, new EventHandler<P032EntityLook>() {
            @Override
            public void handleEvent(P032EntityLook event) {
            }
        });
        eventBus.subscribe(P031EntityRelativeMove.class, new EventHandler<P031EntityRelativeMove>() {
            @Override
            public void handleEvent(P031EntityRelativeMove event) {
                if (event.getEid() == attachedId) {
                    x += event.getDx() / 32.;
                    y += event.getDy() / 32.;
                    z += event.getDz() / 32.;
                    firePositionUpdate();
                }
            }
        });
        eventBus.subscribe(P033EntityRelativeMoveAndLook.class, new EventHandler<P033EntityRelativeMoveAndLook>() {
            @Override
            public void handleEvent(P033EntityRelativeMoveAndLook event) {
                if (event.getEid() == attachedId) {
                    x += event.getDx() / 32.;
                    y += event.getDy() / 32.;
                    z += event.getDz() / 32.;
                    firePositionUpdate();
                }
            }
        });
        eventBus.subscribe(P034EntityTeleport.class, new EventHandler<P034EntityTeleport>() {
            @Override
            public void handleEvent(P034EntityTeleport event) {
                if (event.getEid() == attachedId) {
                    x = event.getX() / 32.;
                    y = event.getY() / 32.;
                    z = event.getZ() / 32.;
                    firePositionUpdate();
                }
            }
        });
        eventBus.subscribe(P011PlayerPosition.class, new EventHandler<P011PlayerPosition>() {
            @Override
            public void handleEvent(P011PlayerPosition event) {
                if (event.getY() != -999) {
                    x = event.getX();
                    y = event.getY();
                    z = event.getZ();
                }

                firePositionUpdate();
            }
        });
        eventBus.subscribe(P012PlayerLook.class, new EventHandler<P012PlayerLook>() {
            @Override
            public void handleEvent(P012PlayerLook event) {
                yaw = event.getYaw();
                pitch = event.getPitch();
                firePositionUpdate();
            }
        });
        eventBus.subscribe(P013PlayerPositionAndLook.class, new EventHandler<P013PlayerPositionAndLook>() {
            @Override
            public void handleEvent(P013PlayerPositionAndLook event) {
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
        eventBus.subscribe(P006PlayerSpawnPosition.class, new EventHandler<P006PlayerSpawnPosition>() {
            @Override
            public void handleEvent(P006PlayerSpawnPosition event) {
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

    public float getPitch() {
        return pitch;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public float getYaw() {
        return yaw;
    }

    public double getZ() {
        return z;
    }
}
