package de.funky_clan.mc.ui;

import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.ConnectionEstablished;
import de.funky_clan.mc.events.ConnectionLost;
import de.funky_clan.mc.events.TargetServerChanged;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author synopia
 */
public class ConnectionToolbar extends JToolBar {
    private JLabel connectionStatus;
    private EventBus eventBus;
    private JTextField host;

    @Inject
    public ConnectionToolbar(EventBus eventBus) {
        setAlignmentX(LEFT_ALIGNMENT);
        this.eventBus = eventBus;
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

        eventBus.registerCallback(TargetServerChanged.class, new EventHandler<TargetServerChanged>() {
            @Override
            public void handleEvent(TargetServerChanged event) {
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
                eventBus.fireEvent(new TargetServerChanged(host.getText()));
            }
        });

        add(connectionStatus);
        addSeparator();
        add(new JLabel("host "));
        add(host);
    }


}
