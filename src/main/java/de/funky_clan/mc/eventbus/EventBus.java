package de.funky_clan.mc.eventbus;

import com.google.inject.Inject;
import de.funky_clan.mc.util.Benchmark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author synopia
 */
public class EventBus {
    private HashMap<Class<? extends Event>, List<EventHandler<?>>> allHandlers = new HashMap<Class<? extends Event>, List<EventHandler<?>>>();
    private HashMap<Object, HashMap<Class<? extends Event>, List<EventHandler<?>>>> channelHandlers = new HashMap<Object, HashMap<Class<? extends Event>, List<EventHandler<?>>>>();

    private BlockingQueue<Event> events = new LinkedBlockingQueue<Event>();
    private final Logger log = LoggerFactory.getLogger(EventBus.class);

    @Inject
    private Benchmark benchmark;

    public EventBus() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        handleNextEvent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @SuppressWarnings("unchecked")
    public boolean handleNextEvent() throws InterruptedException {
        List<EventHandler> callbacks = new ArrayList<EventHandler>();
        Event event = events.take();

        if( event!=null ) {
            benchmark.startBenchmark(this);
            callbacks.clear();
            getCallbacks(event, callbacks);
//            log.info("Calling "+callbacks.size()+" handlers for event "+event);
            for (EventHandler callback : callbacks) {
                callback.handleEvent(event);
            }
            benchmark.endBenchmark(this);
        }
        return events.size()>0;
    }

    public void fireEvent( Event event ) {
        events.add(event);
    }

    public synchronized <T extends Event> void registerCallback( Class<T> cls, EventHandler<T> callback ) {
        addCallback( allHandlers, cls, callback );
    }

    public synchronized <T extends Event> void registerCallback( Object channel, Class<T> cls, EventHandler<T> callback ) {
        HashMap<Class<? extends Event>, List<EventHandler<?>>> channelHandler;
        if( channelHandlers.containsKey(channel) ) {
            channelHandler = channelHandlers.get(channel);
        } else {
            channelHandler = new HashMap<Class<? extends Event>, List<EventHandler<?>>>();
            channelHandlers.put(channel, channelHandler);
        }
        addCallback(channelHandler, cls, callback);
    }

    protected synchronized void getCallbacks(Event event, List<EventHandler> resultCallbacks) {
        Class cls = event.getClass();
        Object channel = event.getChannel();
        if( channelHandlers.containsKey(channel) ) {
            HashMap<Class<? extends Event>, List<EventHandler<?>>> channelHandler = channelHandlers.get(channel);
            if( channelHandler.containsKey(cls) ) {
                List<EventHandler<?>> callbacks = channelHandler.get(cls);
                for (EventHandler callback : callbacks) {
                    resultCallbacks.add(callback);
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
