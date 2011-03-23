package de.funky_clan.mc.eventbus;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of a threaded eventbus.
 *
 *
 *
 * @author synopia
 */
public class BackgroundEventBus extends ThreadedEventBus<BackgroundEvent> {
    @Override
    protected Runnable getThreadRunner()  {
        return new Runnable() {
            public void run() {
                while (true) {
                    try {
                        processEventsAndWait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
