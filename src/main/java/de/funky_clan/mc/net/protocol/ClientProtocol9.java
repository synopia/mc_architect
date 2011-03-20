package de.funky_clan.mc.net.protocol;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author synopia
 */
public class ClientProtocol9 extends Protocol9{
    private final Logger log = LoggerFactory.getLogger(ClientProtocol9.class);

    @Inject
    private PlayerPositionProtocol playerPositionProtocol;

    @Override
    public void load() {
        super.load();

        playerPositionProtocol.loadClient(this);
    }
}
