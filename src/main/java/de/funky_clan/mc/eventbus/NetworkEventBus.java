package de.funky_clan.mc.eventbus;

import com.google.inject.Inject;
import de.funky_clan.mc.net.NetworkException;
import de.funky_clan.mc.util.Benchmark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * This eventbus adds another thread to a ThreadedEventBus, to process any incoming network data.
 *
 *
 * @author synopia
 */
public abstract class NetworkEventBus extends ThreadedEventBus {
    private final Logger logger = LoggerFactory.getLogger( NetworkEventBus.class );
    @Inject
    private Benchmark    benchmark;
    private Thread       netThread;

    @Override
    public void start() {
        super.start();
        netThread = new Thread( new Runnable() {
            @SuppressWarnings( {"InfiniteLoopStatement"} )
            @Override
            public void run() {
                while( true ) {
                    try {
                        if( isConnected() ) {
                            processNetwork();
                        } else {
                            try {
                                Thread.sleep( 1000 );
                            } catch( InterruptedException e ) {

                                // ignore
                            }
                        }
                    } catch( NetworkException e ) {
                        disconnect( e );
                    }
                }
            }
        } );
        netThread.start();
        benchmark.addThreadId( "net", netThread.getId() );
    }

    protected void disconnect() {
        netThread.interrupt();
    }

    protected abstract void disconnect( NetworkException e );

    protected abstract boolean isConnected();

    protected abstract DataInputStream getInputStream();

    protected abstract DataOutputStream getOutputStream();

    protected abstract void processNetwork();
}
