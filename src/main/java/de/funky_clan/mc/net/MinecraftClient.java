package de.funky_clan.mc.net;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.NetworkEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author synopia
 */
@Singleton
public class MinecraftClient extends MinecraftNetworkEventBus {
    private final Logger logger = LoggerFactory.getLogger( MinecraftClient.class );
    @Inject
    private EventDispatcher      eventDispatcher;

    public MinecraftClient() {
        logger.info( "Starting Minecraft Client Network..." );
    }

    @Override
    protected byte getNetworkType() {
        return NetworkEvent.CLIENT;
    }
}
