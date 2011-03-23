package de.funky_clan.mc.eventbus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This eventbus implementation uses a thread to process events.
 *
 * When an event is fired to a threaded eventbus, its checked from which thread the new event is fired.
 * If its fired on the process thread, it gets handled imediately. Otherwise the event is stored in a
 * event queue and handled later on.
 *
 * @author synopia
 */
public abstract class ThreadedEventBus<E extends ThreadedEvent> extends EventBus<E>{
    protected BlockingQueue<E> events = new LinkedBlockingQueue<E>();
    private Thread thread;

    public ThreadedEventBus() {
    }

    public void start() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<EventHandler> list = new ArrayList<EventHandler>();
                while (true) {
                    try {
                        E event = events.take();
                        list.clear();
                        getCallbacks(event.getTopic(), event, list);
                        handleEvent(list, event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    // todo threaded eventbus shutdown

    @Override
    public void fireEvent(Object topic, E event) {
        throw new UnsupportedOperationException("ThreadedEventBus only accepts events with associated topics!");
    }

    @Override
    public void fireEvent(final E event) {
        if( Thread.currentThread()==thread ) {
            handleEvent(event.getTopic(), event);
        } else {
            events.add( event );
        }
    }
}
