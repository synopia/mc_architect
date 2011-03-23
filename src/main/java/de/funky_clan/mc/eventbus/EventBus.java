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
 * This basic implementation runs all subscribers as the event is published (fired).
 *
 * @author synopia
 */
public class EventBus<E extends Event> {
    private HashMap<Class<? extends E>, List<EventHandler<?>>> allHandlers = new HashMap<Class<? extends E>, List<EventHandler<?>>>();
    private HashMap<Object, HashMap<Class<? extends E>, List<EventHandler<?>>>> topicHandlers = new HashMap<Object, HashMap<Class<? extends E>, List<EventHandler<?>>>>();

    private final Logger log = LoggerFactory.getLogger(EventBus.class);

    @Inject
    private Benchmark benchmark;

    public EventBus() {
    }

    protected void handleEvent(Object topic, E event) {
        handleEvent(getCallbacks(topic, event), event);
    }

    @SuppressWarnings("unchecked")
    protected void handleEvent(List<EventHandler> callbacks, E event) {
        if( event!=null ) {
            benchmark.startBenchmark(this);
//            log.info("Calling "+callbacks.size()+" handlers for event "+event);
            for (EventHandler callback : callbacks) {
                callback.handleEvent(event);
            }
            benchmark.endBenchmark(this);
        }
    }

    public void fireEvent( final E event ) {
        fireEvent( null, event );
    }
    public void fireEvent( Object topic, final E event ) {
        handleEvent(getCallbacks(topic, event), event);
    }

    public synchronized <T extends E> void registerCallback( Class<T> cls, EventHandler<T> callback ) {
        addCallback( allHandlers, cls, callback );
    }

    public synchronized <T extends E> void registerCallback( Object topic, Class<T> cls, EventHandler<T> callback ) {
        HashMap<Class<? extends E>, List<EventHandler<?>>> topicHandlers;
        if( this.topicHandlers.containsKey(topic) ) {
            topicHandlers = this.topicHandlers.get(topic);
        } else {
            topicHandlers = new HashMap<Class<? extends E>, List<EventHandler<?>>>();
            this.topicHandlers.put(topic, topicHandlers);
        }
        addCallback(topicHandlers, cls, callback);
    }

    protected List<EventHandler> getCallbacks(Object topic, E event) {
        ArrayList<EventHandler> handlers = new ArrayList<EventHandler>();
        getCallbacks(topic, event, handlers );
        return handlers;
    }

    protected synchronized void getCallbacks(Object topic, E event, List<EventHandler> resultCallbacks) {
        Class cls = event.getClass();
        if( topic!=null ) {
            if( topicHandlers.containsKey(topic) ) {
                HashMap<Class<? extends E>, List<EventHandler<?>>> channelHandler = topicHandlers.get(topic);
                if( channelHandler.containsKey(cls) ) {
                    List<EventHandler<?>> callbacks = channelHandler.get(cls);
                    for (EventHandler callback : callbacks) {
                        resultCallbacks.add(callback);
                    }
                }
            }
        }

        if( allHandlers.containsKey(cls) ) {
            List<EventHandler<?>> callbackList = allHandlers.get(cls);
            for (EventHandler callback : callbackList) {
                resultCallbacks.add(callback);
            }
        }
    }

    private <T extends E>void addCallback(HashMap<Class<? extends E>, List<EventHandler<?>>> target, Class<T> cls, EventHandler<T> handler) {
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
