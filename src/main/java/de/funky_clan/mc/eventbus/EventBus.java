package de.funky_clan.mc.eventbus;

import com.google.inject.Inject;
import de.funky_clan.mc.util.Benchmark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An eventbus using a publish/subscribe mechanism.
 *
 * Subscribers (EventHandler) may register to specific events. Events with a specified topic
 * are only handled by subscribers of the same topic.
 *
 * @author synopia
 */
public abstract class EventBus {
    private HashMap<Class<? extends Event>, List<EventHandler<?>>> handlers = new HashMap<Class<? extends Event>, List<EventHandler<?>>>();
    private HashMap<Class<? extends Event>, VetoHandler<?>> vetoHandlers = new HashMap<Class<? extends Event>, VetoHandler<?>>();

    private final Logger log = LoggerFactory.getLogger(EventBus.class);

    @Inject
    private Benchmark benchmark;

    public EventBus() {
        registerCallback(VetoEvent.class, new EventHandler<VetoEvent>() {
            @Override
            public void handleEvent(VetoEvent event) {
                Event realEvent = event.getEvent();
                handleVeto(realEvent);
            }
        });
    }

    @SuppressWarnings("unchecked")
    protected void handleEvent(Event event) {
        if( event!=null ) {
            List<EventHandler> callbacks = getCallbacks(event);
            for (EventHandler callback : callbacks) {
                callback.handleEvent(event);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public boolean isVeto( Event event ) {
        Class<? extends Event> cls = event.getClass();
        if( vetoHandlers.containsKey(cls) ) {
            VetoHandler handler = vetoHandlers.get(cls);
            return handler.isVeto(event);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public void handleVeto( Event event ) {
        Class<? extends Event> cls = event.getClass();
        if( vetoHandlers.containsKey(cls) ) {
            VetoHandler handler = vetoHandlers.get(cls);
            handler.handleVeto(event);
        }
    }

    public void fireEvent(final Event event) {
        if( !isVeto(event) ) {
            forceFireEvent(event);
        } else {
            forceFireEvent(new VetoEvent(event));
        }
    }

    public abstract void forceFireEvent(final Event event);

    public synchronized boolean hasCallbacks( Event event ) {
        return handlers.containsKey(event.getClass());
    }
    public synchronized <T extends Event> void registerVetoHandler( Class<T> cls, VetoHandler<T> handler ) {
        vetoHandlers.put(cls, handler);
    }
    public synchronized <T extends Event> void registerCallback( Class<T> cls, EventHandler<T> callback ) {
        addCallback(handlers, cls, callback);
    }

    protected List<EventHandler> getCallbacks(Event event) {
        ArrayList<EventHandler> handlers = new ArrayList<EventHandler>();
        getCallbacks(event, handlers );
        return handlers;
    }

    protected synchronized void getCallbacks(Event event, List<EventHandler> resultCallbacks) {
        Class cls = event.getClass();
        if( handlers.containsKey(cls) ) {
            List<EventHandler<?>> callbackList = handlers.get(cls);
            for (EventHandler callback : callbackList) {
                resultCallbacks.add(callback);
            }
        }
    }

    private <T extends Event>void addCallback(HashMap<Class<? extends Event>, List<EventHandler<?>>> target, Class<T> cls, EventHandler<T> handler) {
        List<EventHandler<?>> handlerList;
        if( target.containsKey(cls) ) {
            handlerList = target.get(cls);
        } else {
            handlerList = new ArrayList<EventHandler<?>>();
            target.put(cls, handlerList);
        }
        handlerList.add(handler);
    }
}
