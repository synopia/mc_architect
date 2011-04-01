package de.funky_clan.mc.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.eventbus.VetoEvent;
import de.funky_clan.mc.eventbus.VetoHandler;
import de.funky_clan.mc.net.MinecraftClient;
import de.funky_clan.mc.net.MinecraftServer;

import java.util.HashMap;

/**
 *
 *
 * @author synopia
 */
@Singleton
public class EventDispatcher {
    private final HashMap<Class<? extends Event>, HandlerBus> vetoHandlers = new HashMap<Class<? extends Event>,
                                                                                 HandlerBus>();
    @Inject
    private MinecraftClient client;
    @Inject
    private ModelEventBus   modelEventBus;
    @Inject
    private MinecraftServer server;
    @Inject
    private SwingEventBus   swingEventBus;

    public synchronized <T extends Event> void registerVetoHandler( EventBus bus, Class<T> cls,
            VetoHandler<T> handler ) {
        vetoHandlers.put( cls, new HandlerBus( handler, bus ));
    }

    @SuppressWarnings( "unchecked" )
    public boolean isVeto( Event event ) {
        Class<? extends Event> cls = event.getClass();

        if( vetoHandlers.containsKey( cls )) {
            HandlerBus handlerBus = vetoHandlers.get( cls );

            return handlerBus.vetoHandler.isVeto( event );
        }

        return false;
    }

    public void fire( Event event ) {
        fire( event, false );
    }

    public void fire( Event event, boolean ignoreVeto ) {
        if( ignoreVeto || !isVeto( event )) {
            modelEventBus.fireEvent( event );
            swingEventBus.fireEvent( event );
            client.fireEvent( event );
            server.fireEvent( event );
        } else {
            HandlerBus handlerBus = vetoHandlers.get( event.getClass() );

            handlerBus.bus.fireEvent( new VetoEvent( event, handlerBus.vetoHandler ));
        }
    }

    private static class HandlerBus {
        public final EventBus    bus;
        public final VetoHandler vetoHandler;

        private HandlerBus( VetoHandler<?> vetoHandler, EventBus bus ) {
            this.vetoHandler = vetoHandler;
            this.bus         = bus;
        }
    }
}
