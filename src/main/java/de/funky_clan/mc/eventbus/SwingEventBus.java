package de.funky_clan.mc.eventbus;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.util.Benchmark;

import javax.swing.SwingUtilities;
import java.util.List;

/**
 * Whenever an event is published on the swing eventbus, it is checked, which thread fired the event:
 * If its fired on the Swing EventDispatchThread, the event is handled imediately.
 * Otherwise invokeLater is used, to queue the eventhandling.
 *
 * @author synopia
 */
@Singleton
public class SwingEventBus extends EventBus {
    @Inject
    private Benchmark benchmark;

    public SwingEventBus() {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                benchmark.addThreadId( "gfx", Thread.currentThread().getId() );
            }
        } );
    }

    @Override
    public void publish(final Event event) {
        if( SwingUtilities.isEventDispatchThread() ) {
            handleEvent( event );
        } else {
            handleEventInEdt( event );
        }
    }

    @SuppressWarnings( "unchecked" )
    protected void handleEventInEdt( final Event event ) {
        if(( event != null ) && hasSubscribers(event)) {
            List<EventHandler> callbacks = getCallbacks( event );

            for( final EventHandler callback : callbacks ) {
                SwingUtilities.invokeLater( new Runnable() {
                    @Override
                    public void run() {
                        callback.handleEvent( event );
                    }
                } );
            }
        }
    }
}
