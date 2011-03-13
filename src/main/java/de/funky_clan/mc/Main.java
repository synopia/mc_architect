package de.funky_clan.mc;

//~--- non-JDK imports --------------------------------------------------------

import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.ui.MainPanel;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.*;

/**
 * @author synopia
 */
public class Main extends JFrame {

    public Main( Configuration configuration ) {
        MainPanel mainPanel=new MainPanel( configuration );
        setTitle( "Minecraft Architect for v1.3.01" );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        add( mainPanel );
        pack();
    }

    public void start() {
        this.setVisible( true );
    }

    public static void main( String[] args ) {
        String configFilename = "kolloseum.rb";

        if( args.length > 0 ) {
            configFilename = args[0];
        }

        Configuration conf = Configuration.createFromRuby( configFilename );
        Main          main = new Main( conf );

        main.start();
    }
}
