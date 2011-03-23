package de.funky_clan.mc.net;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.network.ConnectionEstablished;
import de.funky_clan.mc.events.swing.ConnectionDetailsChanged;
import de.funky_clan.mc.net.packets.Handshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author synopia
 */
@Singleton
public class MinecraftService {
    private final Logger logger = LoggerFactory.getLogger(MinecraftService.class);

    @Inject
    private MinecraftClient client;
    @Inject
    private MinecraftServer server;
    private Thread thread;
    private ServerSocket serverSocket;
    private String            targetHost;
    private int               targetPort;
    private int port;

    @Inject
    private EventDispatcher eventDispatcher;

    @Inject
    public MinecraftService(ModelEventBus eventBus) {
        eventBus.registerCallback(ConnectionDetailsChanged.class, new EventHandler<ConnectionDetailsChanged>() {
            @Override
            public void handleEvent(ConnectionDetailsChanged event) {
                stop();
                port = event.getListeningPort();
                targetHost = event.getHost();
                targetPort = event.getPort();
                start();
            }
        });
        eventBus.registerCallback(Handshake.class, new EventHandler<Handshake>() {
            @Override
            public void handleEvent(Handshake event) {
                eventDispatcher.fire(new ConnectionEstablished(event.getUsername()) );
            }
        });
    }

    public void start() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("Start listening thread at port "+port);
                    serverSocket = new ServerSocket(port);
                    Socket sourceSocket = serverSocket.accept();
                    logger.info("Client connected at port "+port);

                    logger.info("connecting to minecraft server " + targetHost + " : "+targetPort);
                    Socket targetSocket = new Socket( targetHost, targetPort );

                    OutputStream toSource = sourceSocket.getOutputStream();
                    InputStream fromSource = sourceSocket.getInputStream();

                    OutputStream toTarget = targetSocket.getOutputStream();
                    InputStream fromTarget = targetSocket.getInputStream();

                    client.connect(fromSource, toTarget);
                    server.connect(fromTarget, toSource);

                    logger.info("connection established!");
                } catch (IOException e) {
                    logger.info("connection could not established!");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void stop() {
        logger.info("Stop listening at port "+port);
        client.disconnect();
        if( thread!=null ) {
            thread.interrupt();
        }
    }
}
