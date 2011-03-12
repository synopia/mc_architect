package de.funky_clan.mc.net.protocol;

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

    private ClientHandler handler;

    public interface ClientHandler extends ProtocolHandler {
        void onPlayerUpdate( double x, double y, double z, float yaw, float pitch );
    }


    public ClientProtocol9() {
        this(null);
    }

    public ClientProtocol9(ClientHandler handler) {
        super(handler);
        this.handler = handler;
    }

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

                if( handler!=null ) {
                    handler.onPlayerUpdate(x,y,z,yaw,pitch);
                }
            }
        });

        setDecoder( 0x0c, new MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                yaw = in.readFloat();
                pitch = in.readFloat();
                boolean onGround = in.readBoolean();

                if( handler!=null ) {
                    handler.onPlayerUpdate(x,y,z,yaw,pitch);
                }
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

                if( handler!=null ) {
                    handler.onPlayerUpdate(x,y,z,yaw,pitch);
                }
            }
        });
    }
}
