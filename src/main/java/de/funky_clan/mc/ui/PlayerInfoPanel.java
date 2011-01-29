package de.funky_clan.mc.ui;

import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.Player;

import javax.swing.*;
import java.awt.*;

/**
 * @author paul.fritsche@googlemail.com
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
                "Absolute World: %s\n"+
                "Absolute Model: %s\n"+
                "Relative to mid: %s\n",
                formatCoord(x, y, z),
                formatCoord(relX, relY, relZ),
                formatCoord(relX-model.getWidth()/2, relY-model.getHeight()/2, relZ)
        );

        textArea.setText( text );
    }

    public String formatCoord( int x, int y, int z ) {
        return x+", "+y+", "+z;
    }
}
