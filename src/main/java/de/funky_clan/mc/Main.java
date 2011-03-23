package de.funky_clan.mc;

//~--- non-JDK imports --------------------------------------------------------

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.funky_clan.mc.config.ArchitectModule;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.swing.ConnectionDetailsChanged;
import de.funky_clan.mc.events.swing.Initialize;
import de.funky_clan.mc.net.MinecraftClient;
import de.funky_clan.mc.net.MinecraftServer;
import de.funky_clan.mc.net.MinecraftService;
import de.funky_clan.mc.services.BaseOreDetectorService;
import de.funky_clan.mc.services.OreDetectorService;
import de.funky_clan.mc.scripts.ScriptFactory;
import de.funky_clan.mc.ui.MainPanel;

import javax.swing.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author synopia
 */
public class Main extends JFrame {

    @Inject
    MainPanel mainPanel;

//    @Inject
//    RegionFileService regionFileService;

    @Inject
    OreDetectorService oreDetectorService;
    @Inject
    ScriptFactory scriptFactory;
    @Inject
    SwingEventBus swingEventBus;
    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    MinecraftService minecraftService;
    @Inject
    MinecraftServer minecraftServer;
    @Inject
    MinecraftClient minecraftClient;
    @Inject
    ModelEventBus modelEventBus;

    public Main() {
    }

    public static boolean isDebug() {
        return Main.class.getPackage().getImplementationVersion()==null;
    }

    public void init() {
        startServices();
        String version = Main.class.getPackage().getImplementationVersion();
        setTitle("Minecraft Architect for v" + version);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(mainPanel);
        swingEventBus.registerCallback(Initialize.class, new EventHandler<Initialize>() {
            @Override
            public void handleEvent(Initialize event) {
                pack();
            }
        });
        eventDispatcher.fire(new Initialize());
        if( isDebug() ) {
            eventDispatcher.fire(new ConnectionDetailsChanged(12345,"localhost"));
        } else {
            eventDispatcher.fire(new ConnectionDetailsChanged(12345,"mc.funky-clan.de"));
        }
    }

    public void start() {
        this.setVisible( true );
    }

    protected void startServices() {
        modelEventBus.start();

        minecraftServer.start();
        minecraftClient.start();
    }

    public static void main( String[] args ) {
        ArchitectModule module = new ArchitectModule();
        Injector injector = Guice.createInjector(module);

        Main main = injector.getInstance(Main.class);
        main.init();
        main.start();
    }
}
