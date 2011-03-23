package de.funky_clan.mc.net.protocol;

import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.events.network.ConnectionEstablished;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class ClientProtocol9 extends MinecraftBinding {
    private final Logger log = LoggerFactory.getLogger(ClientProtocol9.class);

    @Inject
    private PlayerPositionProtocol playerPositionProtocol;

    @Inject
    EventBus eventBus;


    @Override
    public void load() {
        super.load();

        playerPositionProtocol.loadClient(this);

        setDecoder(0x01, new MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                eventBus.fireEvent(new ConnectionEstablished(username));
            }
        });

    }
}
