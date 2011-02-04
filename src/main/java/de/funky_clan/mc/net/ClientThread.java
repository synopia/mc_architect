package de.funky_clan.mc.net;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.ObjectInputStream;

import java.net.Socket;

/**
 * Thread, that listens for data from a player position server.
 *
 * @author synopia
 */
public class ClientThread extends Thread {
    private String            host;
    private ObjectInputStream input;
    private DataListener      listener;
    private int               port;
    private Socket            socket;

    public ClientThread() {}

    /**
     * Tries to connect to given server (which is in fact the patched minecraft client).
     * Whenever data is received, a corresponding listener method is called IN THIS THREAD!
     *
     * @param host     server ip address or name
     * @param port     server port
     * @param listener listener, which is informed about incoming data
     * @return true if connection was successful, false otherwise
     */
    public boolean connect( String host, int port, DataListener listener ) {
        this.listener = listener;
        this.host     = host;
        this.port     = port;

        return true;
    }

    protected boolean doConnect() {
        boolean result;

        try {
            socket = new Socket( host, port );
            input  = new ObjectInputStream( socket.getInputStream() );
            result = true;

            if( listener != null ) {
                listener.onConnect();
            }
        } catch( IOException e ) {
            result = false;
        }

        return result;
    }

    @Override
    public void run() {
        while( true ) {
            if( (socket != null) && (input != null) && socket.isConnected() ) {
                try {
                    int   x      = input.readInt();
                    int   y      = input.readInt();
                    int   z      = input.readInt();
                    float radius = input.readFloat();

                    if( listener != null ) {
                        listener.onPlayerPosition( x, y, z, radius );
                    }
                } catch( IOException e ) {
                    e.printStackTrace();
                    socket = null;
                    input  = null;

                    if( listener != null ) {
                        listener.onDisconnect();
                    }
                }
            } else {
                if( host != null ) {
                    if( !doConnect() ) {
                        host = null;
                    }
                }

                try {
                    sleep( 1000 );
                } catch( InterruptedException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface DataListener {
        public void onConnect();

        public void onDisconnect();

        public void onPlayerPosition( int x, int y, int z, float radius );
    }
}
