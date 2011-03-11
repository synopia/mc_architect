package de.funky_clan.mc.net;

/**
 * @author synopia
 */
public class MinecraftMitmThread extends MitmThread {
    public interface PlayerPositionHandler {
        public void onConnect();
        public void onDisconnect();
        public void onPlayerPosition( double x, double y, double z, float yaw, float pitch);
    }

    private PlayerPositionHandler handler;

    public MinecraftMitmThread(int sourcePort, final PlayerPositionHandler handler ) {
        super(sourcePort);
        this.handler = handler;
        setConnectionHandler(new ConnectionHandler() {
            @Override
            public Handler createRequestHandler() {
                return new MitmThread.Handler() {
                    private int type;
                    double x = 0;
                    double y = 0;
                    double stance = 0;
                    double z = 0;
                    float yaw = 0;
                    float pitch = 0;

                    @Override
                    public void onData(byte[] buffer, int length) {
                        if (length == 1) {
                            type = buffer[0];
                        } else if (type != 0) {
                            switch (type) {
                                case 0xA:
                                    break;
                                case 0xB:
                                    x = Double.longBitsToDouble(readLong(buffer, 0));
                                    y = Double.longBitsToDouble(readLong(buffer, 8));
                                    stance = Double.longBitsToDouble(readLong(buffer, 16));
                                    z = Double.longBitsToDouble(readLong(buffer, 24));
                                    break;
                                case 0xC:
                                    yaw = Float.intBitsToFloat(readInt(buffer, 0));
                                    pitch = Float.intBitsToFloat(readInt(buffer, 4));
                                    break;
                                case 0xD:
                                    x = Double.longBitsToDouble(readLong(buffer, 0));
                                    y = Double.longBitsToDouble(readLong(buffer, 8));
                                    stance = Double.longBitsToDouble(readLong(buffer, 16));
                                    z = Double.longBitsToDouble(readLong(buffer, 24));
                                    yaw = Float.intBitsToFloat(readInt(buffer, 24 + 8));
                                    pitch = Float.intBitsToFloat(readInt(buffer, 24 + 8 + 4));

                            }
                            type = 0;
                            handler.onPlayerPosition(x, z, y, yaw, pitch);
                        }

                    }
                };
            }

            @Override
            public Handler createResponseHandler() {
                return null;
            }

            @Override
            public void onConnect() {
                handler.onConnect();
            }

            @Override
            public void onDisconnect() {
                handler.onDisconnect();
            }
        });
    }

    private int readInt(byte[] buffer, int offs ) {
        return ((int)buffer[offs+3] & 0x0ff) |
                (((int)buffer[offs+2] & 0x0ff) << 8) |
                (((int)buffer[offs+1] & 0x0ff) << 16) |
                (((int)buffer[offs+0] & 0x0ff) << 24);

    }
    private long readLong(byte[] buffer, int offs ) {
        return ((long)buffer[offs+7] & 0x0ff) |
                (((long)buffer[offs+6] & 0x0ff) << 8) |
                (((long)buffer[offs+5] & 0x0ff) << 16) |
                (((long)buffer[offs+4] & 0x0ff) << 24) |
                (((long)buffer[offs+3] & 0x0ff) << 32) |
                (((long)buffer[offs+2] & 0x0ff) << 40) |
                (((long)buffer[offs+1] & 0x0ff) << 48) |
                (((long)buffer[offs+0] & 0x0ff) << 56);
    }

}
