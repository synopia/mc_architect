package de.funky_clan.mc.net;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.eventbus.NetworkEvent;
import de.funky_clan.mc.events.network.ConnectionEstablished;
import de.funky_clan.mc.events.network.ConnectionLost;
import de.funky_clan.mc.events.swing.ConnectionDetailsChanged;
import de.funky_clan.mc.net.packets.P255Disconnect;
import de.funky_clan.mc.net.packets.P002Handshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author synopia
 */
@Singleton
public class MinecraftService {
    private final Logger          logger = LoggerFactory.getLogger( MinecraftService.class );
    @Inject
    private MinecraftClient       client;
    @Inject
    private EventDispatcher eventDispatcher;
    private final ExecutorService pool;
    private int                   port;
    private MinecraftServer       server;
    private ServerSocket          serverSocket;
    private String                targetHost;
    private int                   targetPort;

    @Inject
    public MinecraftService( ModelEventBus eventBus, final MinecraftServer server ) {
        this.server = server;
        logger.info( "Starting MinecraftService..." );
        pool = Executors.newFixedThreadPool( 1 );
        eventBus.subscribe(ConnectionDetailsChanged.class, new EventHandler<ConnectionDetailsChanged>() {
            @Override
            public void handleEvent(ConnectionDetailsChanged event) {
                stop();
                port = event.getListeningPort();
                targetHost = event.getHost();
                targetPort = event.getPort();
                start();
            }
        });
        eventBus.subscribe(P002Handshake.class, new EventHandler<P002Handshake>() {
            @Override
            public void handleEvent(P002Handshake event) {
                logger.info("Received handshake signal from " + event.getSourceName() + ". (" + event.getUsername()
                        + ")");

                if (event.getSource() == NetworkEvent.CLIENT) {
                    eventDispatcher.publish(new ConnectionEstablished(event.getUsername()));
                }
            }
        });
        eventBus.subscribe(ConnectionLost.class, new EventHandler<ConnectionLost>() {
            @Override
            public void handleEvent(ConnectionLost event) {
                logger.info("Received connection lost signal.");
                start();
            }
        });
        server.subscribe(P255Disconnect.class, new EventHandler<P255Disconnect>() {
            @Override
            public void handleEvent(P255Disconnect event) {
                logger.info("Received disconnect signal from " + event.getSourceName() + ".");
                event.setReason("MCA -> "+event.getReason());
            }
        });
    }

    public void start() {
        pool.execute( new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info( "Start listening thread at port " + port );
                    serverSocket = new ServerSocket( port );

                    Socket sourceSocket = serverSocket.accept();

                    logger.info( "Client connected at port " + port );
                    logger.info( "connecting to minecraft server " + targetHost + " : " + targetPort );

                    Socket       targetSocket = new Socket( targetHost, targetPort );
                    OutputStream toSource     = sourceSocket.getOutputStream();
                    InputStream  fromSource   = sourceSocket.getInputStream();
                    OutputStream toTarget     = targetSocket.getOutputStream();
                    InputStream  fromTarget   = targetSocket.getInputStream();

                    client.connect( fromSource, toTarget );
                    server.connect( fromTarget, toSource );
                    logger.info( "connection established!" );

                } catch( IOException e ) {
                    logger.info( "connection could not established!" );
                    e.printStackTrace();
                } finally {
                    try {
                        if( serverSocket!=null ) {
                            serverSocket.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        } );
    }

    public void stop() {
        logger.info( "Stopping network" );
        client.disconnect();
        server.disconnect();
    }
}
