package de.funky_clan.mc.net.protocol;

import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.net.protocol.events.PlayerPositionUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class ClientProtocol9 extends Protocol9{
    private final Logger log = LoggerFactory.getLogger(ClientProtocol9.class);
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    private EventBus eventBus = EventDispatcher.getDispatcher().getModelEventBus();

    @Override
    protected void load() {
        super.load();

        setDecoder( 0x0b, new MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                x = in.readDouble();
                y = in.readDouble();
                double stance = in.readDouble();
                z = in.readDouble();
                boolean onGround = in.readBoolean();

                eventBus.fireEvent( new PlayerPositionUpdate(x,y,z,yaw,pitch));
            }
        });

        setDecoder( 0x0c, new MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                yaw = in.readFloat();
                pitch = in.readFloat();
                boolean onGround = in.readBoolean();

                eventBus.fireEvent( new PlayerPositionUpdate(x,y,z,yaw,pitch));
            }
        });

        setDecoder( 0x0d, new MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                x = in.readDouble();
                y = in.readDouble();
                double stance = in.readDouble();
                z = in.readDouble();
                yaw = in.readFloat();
                pitch = in.readFloat();
                boolean onGround = in.readBoolean();

                eventBus.fireEvent( new PlayerPositionUpdate(x,y,z,yaw,pitch));
            }
        });
    }
}
