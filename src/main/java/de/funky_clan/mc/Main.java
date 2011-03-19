package de.funky_clan.mc;

//~--- non-JDK imports --------------------------------------------------------

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.funky_clan.mc.config.ArchitectModule;
import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.Initialize;
import de.funky_clan.mc.file.RegionFileService;
import de.funky_clan.mc.model.OreDetector;
import de.funky_clan.mc.ui.MainPanel;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.*;

/**
 * @author synopia
 */
public class Main extends JFrame {

    @Inject
    MainPanel mainPanel;
    @Inject
    EventBus eventBus;
//    @Inject
//    RegionFileService regionFileService;
    @Inject
    OreDetector oreDetector;

    public Main() {
    }

    public void init() {
        setTitle("Minecraft Architect for v" + Main.class.getPackage().getImplementationVersion());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(mainPanel);
        eventBus.registerCallback(Initialize.class, new EventHandler<Initialize>() {
            @Override
            public void handleEvent(Initialize event) {
                pack();
            }
        });
        eventBus.fireEvent( new Initialize() );

    }

    public void start() {
        this.setVisible( true );
    }

    public static void main( String[] args ) {
        ArchitectModule module = new ArchitectModule();
        Injector injector = Guice.createInjector(module);

        String configFilename = "kolloseum.rb";

        if( args.length > 0 ) {
            configFilename = args[0];
        }

//        Configuration conf = Configuration.createFromRuby( configFilename );
        Main          main = injector.getInstance(Main.class);
        main.init();
        main.start();
    }
}
