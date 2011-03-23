package de.funky_clan.mc.eventbus;

import javax.swing.*;

/**
 * Whenever an event is fired to the swing eventbus, it is checked, which thread fired the event:
 * If its fired on the Swing EventDispatchThread, the event is handled imediately.
 * Otherwise invokeLater is used, to queue the eventhandling.
 *
 * @author synopia
 */
public class SwingEventBus extends EventBus<SwingEvent> {
    @Override
    public void fireEvent(final Object topic, final SwingEvent event) {
        if( SwingUtilities.isEventDispatchThread() ) {
            handleEvent(topic, event);
        } else {
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run() {
                    handleEvent(topic, event);
                }
            });
        }
    }
}
