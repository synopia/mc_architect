package de.funky_clan.mc.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.net.MinecraftClient;
import de.funky_clan.mc.net.MinecraftServer;

/**
 * @author synopia
 */
@Singleton
public class EventDispatcher {
    @Inject
    private ModelEventBus modelEventBus;
    @Inject
    private SwingEventBus swingEventBus;
    @Inject
    private MinecraftClient client;
    @Inject
    private MinecraftServer server;


    public void fire( Event event ) {
        modelEventBus.fireEvent(event);
        swingEventBus.fireEvent(event);
        client.fireEvent(event);
        server.fireEvent(event);
    }

}
