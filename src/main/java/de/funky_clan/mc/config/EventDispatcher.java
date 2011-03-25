package de.funky_clan.mc.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.*;
import de.funky_clan.mc.net.MinecraftClient;
import de.funky_clan.mc.net.MinecraftServer;

import java.util.HashMap;

/**
 * @author synopia
 */
@Singleton
public class EventDispatcher {
    @Inject
    private ModelEventBus modelEventBus;
    @Inject
    private SwingEventBus swingEventBus;
    @Inject
    private MinecraftClient client;
    @Inject
    private MinecraftServer server;

    private static class HandlerBus {
        public VetoHandler vetoHandler;
        public EventBus bus;

        private HandlerBus(VetoHandler<?> vetoHandler, EventBus bus) {
            this.vetoHandler = vetoHandler;
            this.bus = bus;
        }
    }
    private HashMap<Class<? extends Event>, HandlerBus> vetoHandlers = new HashMap<Class<? extends Event>, HandlerBus>();

    public synchronized <T extends Event> void registerVetoHandler( EventBus bus, Class<T> cls, VetoHandler<T> handler ) {
        vetoHandlers.put(cls, new HandlerBus(handler, bus));
    }

    @SuppressWarnings("unchecked")
    public boolean isVeto( Event event ) {
        Class<? extends Event> cls = event.getClass();
        if( vetoHandlers.containsKey(cls) ) {
            HandlerBus handlerBus = vetoHandlers.get(cls);
            return handlerBus.vetoHandler.isVeto(event);
        }
        return false;
    }

    public void fire( Event event ) {
        fire(event, false);
    }

    public void fire( Event event, boolean ignoreVeto ) {
        if( ignoreVeto || !isVeto(event) ) {
            modelEventBus.fireEvent(event);
            swingEventBus.fireEvent(event);
            client.fireEvent(event);
            server.fireEvent(event);
        } else {
            HandlerBus handlerBus = vetoHandlers.get(event.getClass());
            handlerBus.bus.fireEvent( new VetoEvent(event, handlerBus.vetoHandler) );
        }
    }
}
