package de.funky_clan.mc.ui.widgets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.network.ConnectionEstablished;
import de.funky_clan.mc.events.network.ConnectionLost;
import de.funky_clan.mc.events.swing.ConnectionDetailsChanged;
import de.funky_clan.mc.net.packets.Disconnect;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author synopia
 */
@Singleton
public class ConnectionWidgetFactory {
    private JLabel          connectionStatus;
    @Inject
    private EventDispatcher eventDispatcher;
    private JTextField      host;

    @Inject
    public ConnectionWidgetFactory( SwingEventBus eventBus ) {
        build();
        eventBus.subscribe(ConnectionEstablished.class, new EventHandler<ConnectionEstablished>() {
            @Override
            public void handleEvent(ConnectionEstablished event) {
                updateStatus(true, "connected");
            }
        });
        eventBus.subscribe(ConnectionLost.class, new EventHandler<ConnectionLost>() {
            @Override
            public void handleEvent(ConnectionLost event) {
                updateStatus(false, "disconnected");
            }
        });
        eventBus.subscribe(Disconnect.class, new EventHandler<Disconnect>() {
            @Override
            public void handleEvent(Disconnect event) {
                updateStatus(false, "disconnected");
            }
        });
        eventBus.subscribe(ConnectionDetailsChanged.class, new EventHandler<ConnectionDetailsChanged>() {
            @Override
            public void handleEvent(ConnectionDetailsChanged event) {
                host.setText(event.getReadableHost());
            }
        });
    }

    protected void updateStatus( boolean connected, String msg ) {
        host.setEditable( !connected );
        connectionStatus.setText( msg );
    }

    private void build() {
        connectionStatus = new JLabel( "disconnected" );
        host             = new JTextField( 50 );
        host.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                eventDispatcher.publish(new ConnectionDetailsChanged(12345, host.getText()));
            }
        } );
    }

    public JLabel getConnectionStatus() {
        return connectionStatus;
    }

    public JTextField getHost() {
        return host;
    }
}
