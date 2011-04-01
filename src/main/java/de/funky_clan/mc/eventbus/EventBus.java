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
 * Subscribers (EventHandler) may register to specific events and get called, whenever this event is published
 * into this event bus
 *
 * @author synopia
 */
public abstract class EventBus {
    private final HashMap<Class<? extends Event>, List<EventHandler<?>>> handlers = new HashMap<Class<? extends Event>,
                                                                                        List<EventHandler<?>>>();
    private final Logger log = LoggerFactory.getLogger( EventBus.class );
    @Inject
    private Benchmark    benchmark;

    public EventBus() {
        subscribe(VetoEvent.class, new EventHandler<VetoEvent>() {
            @Override
            @SuppressWarnings("unchecked")
            public void handleEvent(VetoEvent event) {
                Event realEvent = event.getEvent();
                VetoHandler vetoHandler = event.getHandler();

                vetoHandler.handleVeto(realEvent);
            }
        });
    }

    @SuppressWarnings( "unchecked" )
    protected void handleEvent( Event event ) {
        if( event != null ) {
            List<EventHandler> callbacks = getCallbacks( event );

            for( EventHandler callback : callbacks ) {
                callback.handleEvent( event );
            }
        }
    }

    public abstract void publish(final Event event);

    public synchronized boolean hasSubscribers(Event event) {
        return handlers.containsKey( event.getClass() );
    }

    public synchronized <T extends Event> void subscribe(Class<T> cls, EventHandler<T> callback) {
        addCallback( handlers, cls, callback );
    }

    protected List<EventHandler> getCallbacks( Event event ) {
        ArrayList<EventHandler> handlers = new ArrayList<EventHandler>();

        getCallbacks( event, handlers );

        return handlers;
    }

    protected synchronized void getCallbacks( Event event, List<EventHandler> resultCallbacks ) {
        Class cls = event.getClass();

        if( handlers.containsKey( cls )) {
            List<EventHandler<?>> callbackList = handlers.get( cls );

            for( EventHandler callback : callbackList ) {
                resultCallbacks.add( callback );
            }
        }
    }

    private <T extends Event> void addCallback( HashMap<Class<? extends Event>, List<EventHandler<?>>> target,
            Class<T> cls, EventHandler<T> handler ) {
        List<EventHandler<?>> handlerList;

        if( target.containsKey( cls )) {
            handlerList = target.get( cls );
        } else {
            handlerList = new ArrayList<EventHandler<?>>();
            target.put( cls, handlerList );
        }

        handlerList.add( handler );
    }
}
