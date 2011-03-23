package de.funky_clan.mc.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.net.MinecraftClient;
import de.funky_clan.mc.net.packets.PlayerPosition;

/**
 * @author synopia
 */
@Singleton
public class PlayerPositionService {

    private MinecraftClient client;

    @Inject
    public PlayerPositionService(final MinecraftClient client) {
        this.client = client;
        client.registerCallback(PlayerPosition.class, new EventHandler<PlayerPosition>() {
            @Override
            public void handleEvent(PlayerPosition event) {

            }
        });
    }
}
