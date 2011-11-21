package de.funky_clan.mc;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import de.funky_clan.mc.config.ArchitectModule;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.script.LoadScript;
import de.funky_clan.mc.events.swing.ConnectionDetailsChanged;
import de.funky_clan.mc.events.swing.Initialize;
import de.funky_clan.mc.net.MinecraftClient;
import de.funky_clan.mc.net.MinecraftServer;
import de.funky_clan.mc.net.MinecraftService;
import de.funky_clan.mc.net.packets.P006PlayerSpawnPosition;
import de.funky_clan.mc.scripts.ScriptFactory;
import de.funky_clan.mc.services.OreDetectorService;
import de.funky_clan.mc.ui.MainPanel;
import de.funky_clan.mc.util.StatusBar;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * @author synopia
 */
public class Main extends JFrame {
    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    @Named( "Info" )
    StatusBar        info;
    @Inject
    MainPanel        mainPanel;
    @Inject
    MinecraftClient  minecraftClient;
    @Inject
    MinecraftServer  minecraftServer;
    @Inject
    MinecraftService minecraftService;
    @Inject
    ModelEventBus    modelEventBus;

//  @Inject
//  RegionFileService regionFileService;
    @Inject
    OreDetectorService oreDetectorService;
    @Inject
    ScriptFactory      scriptFactory;
    @Inject
    @Named( "Status" )
    StatusBar          status;
    @Inject
    SwingEventBus      swingEventBus;

    public Main() {}

    public static boolean isDebug() {
        return Main.class.getPackage().getImplementationVersion() == null;
    }

    public void init() {
        setLayout( new BorderLayout() );
        startServices();

        String version = Main.class.getPackage().getImplementationVersion();

        setTitle( "Minecraft Architect for v" + version );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        add( mainPanel.getContentArea(), BorderLayout.CENTER );
        add( info, BorderLayout.NORTH );

        JPanel south = new JPanel();

        south.setLayout( new BoxLayout( south, BoxLayout.Y_AXIS ));

        JToolBar toolBar = mainPanel.getToolBar();

        toolBar.setAlignmentX( Component.LEFT_ALIGNMENT );
        status.setAlignmentX( Component.LEFT_ALIGNMENT );
        south.add( toolBar );
        south.add( status );
        add( south, BorderLayout.SOUTH );
        swingEventBus.subscribe(Initialize.class, new EventHandler<Initialize>() {
            @Override
            public void handleEvent(Initialize event) {
                setBounds(20, 20, 600, 500);
                eventDispatcher.publish(new P006PlayerSpawnPosition());
            }
        });
        eventDispatcher.publish(new Initialize());

        if( isDebug() ) {
            eventDispatcher.publish(new ConnectionDetailsChanged(12345, "mc.funky-clan.de"));
//            eventDispatcher.publish(new ConnectionDetailsChanged(12345, "localhost"));
        } else {
            eventDispatcher.publish(new ConnectionDetailsChanged(12345, "mc.funky-clan.de"));
        }

        eventDispatcher.publish(new LoadScript("kolloseum.rb", !Main.isDebug()));
        eventDispatcher.publish(new LoadScript("akw.rb", !Main.isDebug()));
        eventDispatcher.publish(new LoadScript("glaskugel.rb", !Main.isDebug()));
        eventDispatcher.publish(new LoadScript("superformula.rb", !Main.isDebug()));
        eventDispatcher.publish(new LoadScript("station_select_schematic.rb", !Main.isDebug()));
        eventDispatcher.publish(new LoadScript("nagoya.rb", !Main.isDebug()));
        eventDispatcher.publish(new LoadScript("spider.rb", !Main.isDebug()));
        eventDispatcher.publish(new LoadScript("dino.rb", !Main.isDebug()));
        eventDispatcher.publish(new LoadScript("skull.rb", !Main.isDebug()));
        eventDispatcher.publish(new LoadScript("ModernVilla.schematic", !Main.isDebug()));

        if( Main.isDebug() ) {
            eventDispatcher.publish(new LoadScript("test.rb", false));
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
        ArchitectModule module   = new ArchitectModule();
        Injector        injector = Guice.createInjector( module );
        Main            main     = injector.getInstance( Main.class );

        main.init();
        main.start();
    }
}
