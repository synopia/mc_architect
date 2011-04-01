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
public class MinecraftServer extends MinecraftNetworkEventBus {
    private final Logger logger = LoggerFactory.getLogger( MinecraftServer.class );
    @Inject
    EventDispatcher      eventDispatcher;

    public MinecraftServer() {
        logger.info( "Starting Minecraft Server Network..." );
    }

    @Override
    protected byte getNetworkType() {
        return NetworkEvent.SERVER;
    }
}
