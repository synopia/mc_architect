package de.funky_clan.mc.ui;

//~--- non-JDK imports --------------------------------------------------------

import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.swing.PlayerMoved;
import de.funky_clan.mc.model.Model;

import javax.swing.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author synopia
 */
public class PlayerInfoToolbar extends JToolBar {
    private JLabel position;
    private JLabel direction;
    @Inject
    private Model  model;
    private EventBus eventBus;

    @Inject
    public PlayerInfoToolbar(final EventBus eventBus) {
        setAlignmentX(LEFT_ALIGNMENT);
        this.eventBus = eventBus;

        build();


        eventBus.registerCallback(PlayerMoved.class, new EventHandler<PlayerMoved>() {
            @Override
            public void handleEvent(PlayerMoved event) {
                direction.setText("Direction: " + formatDirection((int) event.getYaw()));
                position.setText("Position: " + formatCoord((int) event.getX(), (int) event.getY(), (int) event.getZ()));
            }
        });
    }

    private void build() {
        direction     = new JLabel();
        position      = new JLabel();

        add(direction);
        addSeparator();
        add(position);
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
