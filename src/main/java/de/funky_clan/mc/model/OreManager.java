package de.funky_clan.mc.model;

import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.OreFound;

/**
 * @author synopia
 */
public class OreManager {
    private EventBus eventBus;


    @Inject
    public OreManager(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.registerCallback(OreFound.class, new EventHandler<OreFound>() {
            @Override
            public void handleEvent(OreFound event) {

            }
        });
    }
}
