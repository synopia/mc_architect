package de.funky_clan.mc.ui;

//~--- non-JDK imports --------------------------------------------------------

import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.ui.events.PlayerMoved;
import de.funky_clan.mc.ui.events.TargetServerChanged;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author synopia
 */
public class PlayerInfoLabels {
    private JLabel position;
    private JLabel direction;
    private Model  model;
    private EventBus eventBus = EventDispatcher.getDispatcher().getModelEventBus();
    private JTextField host;

    public PlayerInfoLabels( Model model ) {
        super();
        this.model    = model;
        direction     = new JLabel();
        position      = new JLabel();

        eventBus.registerCallback(PlayerMoved.class, new EventHandler<PlayerMoved>() {
            @Override
            public void handleEvent(PlayerMoved event) {
                direction.setText("Direction: " + formatDirection((int) event.getYaw()));
                position.setText("Position: " + formatCoord((int) event.getX(), (int) event.getY(), (int) event.getZ()));
            }
        });
        eventBus.registerCallback(TargetServerChanged.class, new EventHandler<TargetServerChanged>() {
            @Override
            public void handleEvent(TargetServerChanged event) {
                host.setText( event.getReadableHost() );
            }
        });
    }

    public JTextField getTargetConnection() {
        host = new JTextField();
        host.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new TargetServerChanged(host.getText()));
            }
        });
        return host;
    }

    public JLabel getDirection() {
        return direction;
    }

    public JLabel getPosition() {
        return position;
    }

    public String formatDirection( int angle ) {
        angle += 45 / 2;
        angle %= 360;

        while( angle < 0 ) {
            angle += 360;
        }

        String dir = "";

        if( angle < 45 ) {
            dir = "W";
        } else if( angle < 2 * 45 ) {
            dir = "NW";
        } else if( angle < 3 * 45 ) {
            dir = "N";
        } else if( angle < 4 * 45 ) {
            dir = "NE";
        } else if( angle < 5 * 45 ) {
            dir = "E";
        } else if( angle < 6 * 45 ) {
            dir = "SE";
        } else if( angle < 7 * 45 ) {
            dir = "S";
        } else if( angle < 8 * 45 ) {
            dir = "SW";
        }

        return dir;
    }

    public String formatCoord( int x, int y, int z ) {
        return x + ", " + y + ", " + z;
    }
}
