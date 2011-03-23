package de.funky_clan.mc.net;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.swing.ConnectionDetailsChanged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author synopia
 */
@Singleton
public class MinecraftService {
    private final Logger logger = LoggerFactory.getLogger(MinecraftService.class);

    @Inject
    private MinecraftClient client;
    private Thread thread;
    private ServerSocket serverSocket;
    private int port;

    @Inject
    public MinecraftService(SwingEventBus eventBus) {
        eventBus.registerCallback(ConnectionDetailsChanged.class, new EventHandler<ConnectionDetailsChanged>() {
            @Override
            public void handleEvent(ConnectionDetailsChanged event) {
                stop();
                port = event.getListeningPort();
                start();
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
                    serverSocket.accept();
                    logger.info("Client connected at port "+port);


                } catch (IOException e) {
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
