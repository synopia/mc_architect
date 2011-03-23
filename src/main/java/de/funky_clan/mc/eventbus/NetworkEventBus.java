package de.funky_clan.mc.eventbus;

import de.funky_clan.mc.net.NetworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This eventbus adds another thread to a ThreadedEventBus, to process any incoming network data.
 *
 *
 * @author synopia
 */
public abstract class NetworkEventBus extends ThreadedEventBus{
    private final Logger logger = LoggerFactory.getLogger(NetworkEventBus.class);

    @Override
    public void start() {
        super.start();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while( true ) {
                    try {
                        if( isConnected() ) {
                            processNetwork();
                        } else {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // ignore
                            }
                        }
                    } catch (NetworkException e) {
                        disconnect(e);
                    }
                }
            }
        });
        thread.start();
    }

    protected abstract void disconnect(NetworkException e);
    protected abstract boolean isConnected();
    protected abstract DataInputStream getInputStream();
    protected abstract DataOutputStream getOutputStream();

    protected abstract void processNetwork();
}
