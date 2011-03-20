package de.funky_clan.mc.net.protocol;

import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.events.PlayerPositionUpdate;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class PlayerPositionProtocol {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    @Inject
    private EventBus eventBus;

    private int entityId = -1;
    private int attachedId = -1;

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public void loadServer( Protocol server ) {
        server.setDecoder(0x27, new Protocol.MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                int entityId = in.readInt();
                int vehicleId = in.readInt();
                if (entityId == PlayerPositionProtocol.this.entityId) {
                    attachedId = vehicleId;
                }
            }
        });
        server.setDecoder(0x1f, new Protocol.MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                int id = in.readInt();
                double dx = in.readByte()/32.;
                double dy = in.readByte()/32.;
                double dz = in.readByte()/32.;
                if( id==attachedId ) {
                    x += dx;
                    y += dy;
                    z += dz;
                    firePositionUpdate();
                }
            }
        });
        server.setDecoder(0x20, new Protocol.MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                int id = in.readInt();
                int yaw = in.readByte();
                int pitch = in.readByte();
            }
        });
        server.setDecoder(0x21, new Protocol.MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                int id = in.readInt();
                double dx = in.readByte()/32.;
                double dy = in.readByte()/32.;
                double dz = in.readByte()/32.;
                int yaw = in.readByte();
                int pitch = in.readByte();
                if( id==attachedId ) {
                    x += dx;
                    y += dy;
                    z += dz;
                    firePositionUpdate();
                }
            }
        });
        server.setDecoder(0x22, new Protocol.MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                int id = in.readInt();
                double nx = in.readInt();
                double ny = in.readInt();
                double nz = in.readInt();
                int yaw = in.readByte();
                int pitch = in.readByte();

                if( id==attachedId ) {
                    x = nx;
                    y = ny;
                    z = nz;
                    firePositionUpdate();
                }
            }
        });
    }
    public void loadClient( Protocol client ) {
        client.setDecoder( 0x0b, new Protocol.MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                double nx = in.readDouble();
                double ny = in.readDouble();
                double stance = in.readDouble();
                double nz = in.readDouble();
                boolean onGround = in.readBoolean();

                if( ny!=-999 ) {
                    x = nx;
                    y = ny;
                    z = nz;
                }
                firePositionUpdate();
            }
        });

        client.setDecoder(0x0c, new Protocol.MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                yaw = in.readFloat();
                pitch = in.readFloat();
                boolean onGround = in.readBoolean();

                firePositionUpdate();
            }
        });

        client.setDecoder(0x0d, new Protocol.MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                double nx = in.readDouble();
                double ny = in.readDouble();
                double stance = in.readDouble();
                double nz = in.readDouble();
                yaw = in.readFloat();
                pitch = in.readFloat();
                boolean onGround = in.readBoolean();

                if( ny!=-999 ) {
                    x = nx;
                    y = ny;
                    z = nz;
                }

                firePositionUpdate();
            }
        });

    }

    private void firePositionUpdate() {
        eventBus.fireEvent(new PlayerPositionUpdate(x, y, z, yaw, pitch));
    }
}
