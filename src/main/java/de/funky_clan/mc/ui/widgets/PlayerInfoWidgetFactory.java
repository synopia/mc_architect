package de.funky_clan.mc.ui.widgets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.model.PlayerPositionUpdate;
import de.funky_clan.mc.model.Model;

import javax.swing.JLabel;

/**
 * @author synopia
 */
@Singleton
public class PlayerInfoWidgetFactory {
    private JLabel direction;
    @Inject
    private Model  model;
    private JLabel position;

    @Inject
    public PlayerInfoWidgetFactory( final SwingEventBus eventBus ) {
        build();
        eventBus.subscribe(PlayerPositionUpdate.class, new EventHandler<PlayerPositionUpdate>() {
            @Override
            public void handleEvent(PlayerPositionUpdate event) {
                direction.setText(formatDirection((int) event.getYaw()));
                position.setText("Position: "
                        + formatCoord(event.getBlockX(), event.getBlockY(), event.getBlockZ()));
            }
        });
    }

    private void build() {
        direction = new JLabel( "WE" );
        position  = new JLabel( "Position: 1600, 1600, 1000" );
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

    public JLabel getPosition() {
        return position;
    }

    public JLabel getDirection() {
        return direction;
    }
}
