package de.funky_clan.mc.ui;

import de.funky_clan.mc.model.Model;

import javax.swing.*;
import java.awt.*;

/**
 * @author synopia
 */
public class PlayerInfoPanel extends JPanel {
    private JTextArea textArea;
    private Model     model;

    public PlayerInfoPanel(Model model) {
        super();
        this.model = model;
        textArea   = new JTextArea();

        textArea.setPreferredSize(new Dimension(300, 70));
        add(textArea);
    }

    public void updatePlayerPos( int x, int y, int z, int relX, int relY, int relZ, int angle ) {
        String text = String.format(
                "Direction: %s\n"+
                "Absolute World: %s\n"+
                "Absolute Model: %s\n"+
                "Relative to mid: %s\n",
                formatDirection(angle),
                formatCoord(x, y, z),
                formatCoord(relX, relY, relZ),
                formatCoord(relX-model.getSizeX()/2, relY-model.getSizeY()/2, relZ)
        );

        textArea.setText( text );
    }

    public String formatDirection( int angle ) {
        angle %= 360;
        while( angle<0 ) {
            angle += 360;
        }

        String dir = "";
        if( angle<45 ) {
            dir = "W";
        } else if( angle<2*45 ) {
            dir = "NW";
        } else if( angle<3*45 ) {
            dir = "N";
        } else if( angle<4*45 ) {
            dir = "NE";
        } else if( angle<5*45 ) {
            dir = "E";
        } else if( angle<6*45 ) {
            dir = "SE";
        } else if( angle<7*45 ) {
            dir = "S";
        } else if( angle<8*45 ) {
            dir = "SW";
        }

        return dir;
    }

    public String formatCoord( int x, int y, int z ) {
        return x+", "+y+", "+z;
    }
}
