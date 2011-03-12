package de.funky_clan.mc.ui;

//~--- non-JDK imports --------------------------------------------------------

import de.funky_clan.mc.model.Model;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;

import javax.swing.*;

/**
 * @author synopia
 */
public class PlayerInfoLabels {
    private JLabel absoluteModel;
    private JLabel absoluteWorld;
    private JLabel direction;
    private Model  model;
    private JLabel relativeMid;

    public PlayerInfoLabels( Model model ) {
        super();
        this.model    = model;
        direction     = new JLabel();
        absoluteWorld = new JLabel();
        absoluteModel = new JLabel();
        relativeMid   = new JLabel();
    }

    public void updatePlayerPos( int x, int y, int z, int relX, int relY, int relZ, int angle ) {
        direction.setText( "Direction: " + formatDirection( angle ));
        absoluteWorld.setText( "Absolute World: " + formatCoord( x, y, z ));
        absoluteModel.setText( "Absolute Model: " + formatCoord( relX, relY, relZ ));
        relativeMid.setText( "");
    }

    public JLabel getDirection() {
        return direction;
    }

    public JLabel getAbsoluteWorld() {
        return absoluteWorld;
    }

    public JLabel getAbsoluteModel() {
        return absoluteModel;
    }

    public JLabel getRelativeMid() {
        return relativeMid;
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
