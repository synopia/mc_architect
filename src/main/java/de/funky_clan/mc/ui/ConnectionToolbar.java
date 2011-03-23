package de.funky_clan.mc.ui;

import com.google.inject.Inject;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.network.ConnectionEstablished;
import de.funky_clan.mc.events.network.ConnectionLost;
import de.funky_clan.mc.events.swing.ConnectionDetailsChanged;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author synopia
 */
public class ConnectionToolbar extends JToolBar {
    @Inject
    private EventDispatcher eventDispatcher;
    private JLabel connectionStatus;
    private JTextField host;

    @Inject
    public ConnectionToolbar(SwingEventBus eventBus) {
        setAlignmentX(LEFT_ALIGNMENT);
        build();

        eventBus.registerCallback(ConnectionEstablished.class, new EventHandler<ConnectionEstablished>() {
            @Override
            public void handleEvent(ConnectionEstablished event) {
                host.setEditable(false);
                connectionStatus.setText("connected as "+event.getUsername());
            }
        });

        eventBus.registerCallback(ConnectionLost.class, new EventHandler<ConnectionLost>() {
            @Override
            public void handleEvent(ConnectionLost event) {
                host.setEditable(true);
                connectionStatus.setText("disconnected");
            }
        });

        eventBus.registerCallback(ConnectionDetailsChanged.class, new EventHandler<ConnectionDetailsChanged>() {
            @Override
            public void handleEvent(ConnectionDetailsChanged event) {
                host.setText( event.getReadableHost() );
            }
        });
    }

    private void build() {
        connectionStatus = new JLabel("disconnected");
        host = new JTextField(50);
        host.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDispatcher.fire(new ConnectionDetailsChanged(12345, host.getText()));
            }
        });

        add(connectionStatus);
        addSeparator();
        add(new JLabel("host "));
        add(host);
    }


}
