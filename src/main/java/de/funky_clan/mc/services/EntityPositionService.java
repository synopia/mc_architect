package de.funky_clan.mc.services;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.funky_clan.mc.config.EntityType;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.events.model.EntityPositionUpdate;
import de.funky_clan.mc.events.model.EntityRemoved;
import de.funky_clan.mc.events.swing.EntityFilterChanged;
import de.funky_clan.mc.model.EntityBlock;
import de.funky_clan.mc.net.packets.*;

import java.util.HashMap;

/**
 * @author synopia
 */
@Singleton
public class EntityPositionService {
    private HashMap<Integer, EntityBlock> entities = new HashMap<Integer, EntityBlock>();
    @Inject
    private Provider<EntityBlock> playerBlockProvider;
    @Inject
    private EventDispatcher eventDispatcher;

    @Inject
    public EntityPositionService(final ModelEventBus eventBus ) {
        eventBus.subscribe(P023EntityCreated.class, new EventHandler<P023EntityCreated>() {
            @Override
            public void handleEvent(P023EntityCreated event) {
                if(EntityType.MAP[event.getType()]!=null ) {
                    EntityBlock block = playerBlockProvider.get();
                    entities.put(event.getEid(), block);

                    block.setPosition(event.getX()/32., event.getY()/32., event.getZ()/32.);
                    block.setType(EntityType.MAP[event.getType()]);
                    eventDispatcher.publish(new EntityPositionUpdate(event.getEid(), block));
                }
            }
        });
        eventBus.subscribe(P020EntitySpawnNamed.class, new EventHandler<P020EntitySpawnNamed>() {
            @Override
            public void handleEvent(P020EntitySpawnNamed event) {
                EntityBlock block = playerBlockProvider.get();
                entities.put(event.getEid(), block);

                block.setPosition(event.getX()/32., event.getY()/32., event.getZ()/32.);
                block.setDirection(event.getYaw());
                block.setType(EntityType.SMP_PLAYER);
                block.setName( event.getPlayerName() );
                eventDispatcher.publish(new EntityPositionUpdate(event.getEid(), block));
            }
        });
        eventBus.subscribe(P024EntitySpawn.class, new EventHandler<P024EntitySpawn>() {
            @Override
            public void handleEvent(P024EntitySpawn event) {
                if(EntityType.MAP[event.getType()]!=null ) {
                    EntityBlock block = playerBlockProvider.get();
                    entities.put(event.getEid(), block);

                    block.setPosition(event.getX()/32., event.getY()/32., event.getZ()/32.);
                    block.setDirection(event.getYaw());
                    block.setType(EntityType.MAP[event.getType()]);
                    eventDispatcher.publish(new EntityPositionUpdate(event.getEid(), block));
                }
            }
        });
        eventBus.subscribe(P032EntityLook.class, new EventHandler<P032EntityLook>() {
            @Override
            public void handleEvent(P032EntityLook event) {
                EntityBlock block = entities.get(event.getEid());
                if( block!=null ) {
                    block.setDirection(event.getYaw());
                    eventDispatcher.publish(new EntityPositionUpdate(event.getEid(), block));
                }
            }
        });
        eventBus.subscribe(P031EntityRelativeMove.class, new EventHandler<P031EntityRelativeMove>() {
            @Override
            public void handleEvent(P031EntityRelativeMove event) {
                EntityBlock block = entities.get(event.getEid());
                if( block!=null ) {
                    block.move(event.getDx()/32., event.getDy()/32., event.getDz()/32.);
                    eventDispatcher.publish(new EntityPositionUpdate(event.getEid(), block));
                }
            }
        });
        eventBus.subscribe(P033EntityRelativeMoveAndLook.class, new EventHandler<P033EntityRelativeMoveAndLook>() {
            @Override
            public void handleEvent(P033EntityRelativeMoveAndLook event) {
                EntityBlock block = entities.get(event.getEid());
                if( block!=null ) {
                    block.move(event.getDx()/32., event.getDy()/32., event.getDz()/32.);
                    block.setDirection(event.getYaw());
                    eventDispatcher.publish(new EntityPositionUpdate(event.getEid(), block));
                }
            }
        });

        eventBus.subscribe(P029EntityDestroy.class, new EventHandler<P029EntityDestroy>() {
            @Override
            public void handleEvent(P029EntityDestroy event) {
                EntityBlock block = entities.get(event.getEid());
                if( block!=null ) {
                    eventDispatcher.publish(new EntityRemoved(event.getEid(), block));
                }
            }
        });
    }
}
