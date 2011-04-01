package de.funky_clan.mc.eventbus;

import com.google.inject.Inject;
import de.funky_clan.mc.util.Benchmark;

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
public abstract class ThreadedEventBus extends EventBus {
    protected final BlockingQueue<Event> events = new LinkedBlockingQueue<Event>();
    @Inject
    private Benchmark                    benchmark;
    private Thread                       thread;

    public ThreadedEventBus() {}

    public void start() {
        thread = new Thread( new Runnable() {
            @SuppressWarnings( {"InfiniteLoopStatement"} )
            @Override
            public void run() {
                while( true ) {
                    try {
                        Event event = events.take();

                        handleEvent( event );
                    } catch( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }
        } );
        thread.start();
        benchmark.addThreadId( "bus", thread.getId() );
    }

    @Override
    public void forceFireEvent( final Event event ) {
        if( Thread.currentThread() == thread ) {
            handleEvent( event );
        } else {
            events.add( event );
        }
    }
}
