package de.funky_clan.mc.ui;

//~--- non-JDK imports --------------------------------------------------------

import com.google.inject.Inject;
import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.*;
import de.funky_clan.mc.model.BackgroundImage;
import de.funky_clan.mc.model.SliceType;
import de.funky_clan.mc.net.MitmThread;
import de.funky_clan.mc.util.Benchmark;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author synopia
 */
public class MainPanel extends JPanel {
    @Inject
    private MitmThread mitmThread;
    @Inject
    private Configuration    configuration;
    private JTextField       host;
    @Inject
    private PlayerInfoLabels playerInfo;
    private double           playerX;
    private double           playerY;
    private double           playerZ;
    private float            yaw;
    private float            pitch;
    @Inject
    private SlicePanel       sideX;
    @Inject
    private SlicePanel       sideY;
    @Inject
    private SlicePanel       topDown;
    private int              zShift;
    private JLabel           zShiftLabel;
    private EventBus         eventBus;
    private JLabel           chunksText;
    private int              chunksLoaded;
    private int              chunksUnloaded;
    private JLabel           memoryText;
    private JLabel           benchmarkText;
    @Inject
    private Benchmark benchmark;
    @Inject
    private ColorsPanel colorsPanel;

    @Inject
    public MainPanel(final EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.registerCallback(ColorChanged.class, new EventHandler<ColorChanged>() {
            @Override
            public void handleEvent(ColorChanged event) {
                repaint();
            }
        });
        eventBus.registerCallback(ChunkUpdate.class, new EventHandler<ChunkUpdate>() {
            @Override
            public void handleEvent(ChunkUpdate event) {
                chunksLoaded ++;
            }
        });
        eventBus.registerCallback(UnloadChunk.class, new EventHandler<UnloadChunk>() {
            @Override
            public void handleEvent(UnloadChunk event) {
                chunksUnloaded ++;
                chunksLoaded --;
            }
        });

        eventBus.registerCallback(PlayerPositionUpdate.class, new EventHandler<PlayerPositionUpdate>() {
            @Override
            public void handleEvent(PlayerPositionUpdate event) {
                playerX = event.getX();
                playerY = event.getY();
                playerZ = event.getZ();
                yaw     = event.getYaw();
                pitch   = event.getPitch();
                firePlayerMoved();
            }
        });

        eventBus.registerCallback(ConnectionEstablished.class, new EventHandler<ConnectionEstablished>() {
            @Override
            public void handleEvent(ConnectionEstablished event) {
                host.setEditable(false);
            }
        });

        eventBus.registerCallback(ConnectionLost.class, new EventHandler<ConnectionLost>() {
            @Override
            public void handleEvent(ConnectionLost event) {
                host.setEditable(true);
            }
        });

        eventBus.registerCallback(TargetServerChanged.class, new EventHandler<TargetServerChanged>() {
            @Override
            public void handleEvent(TargetServerChanged event) {
                mitmThread.setTargetHost( event.getHost() );
                mitmThread.setTargetPort( event.getPort() );
            }
        });

        eventBus.registerCallback(Initialize.class, new EventHandler<Initialize>() {
            @Override
            public void handleEvent(Initialize event) {
                onInit();
            }
        });
        new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStats();
            }
        }).start();
    }

    protected void onInit() {
        setLayout(new BorderLayout());

        this.setFocusable(true);

        this.addKeyListener( new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        playerX++;
                        firePlayerMoved();
                        break;
                    case KeyEvent.VK_DOWN:
                        playerX--;
                        firePlayerMoved();
                        break;
                    case KeyEvent.VK_LEFT:
                        playerZ++;
                        firePlayerMoved();
                        break;
                    case KeyEvent.VK_RIGHT:
                        playerZ--;
                        firePlayerMoved();
                        break;
                }
            }
        });

        playerX            = configuration.getMidX();
        playerY            = configuration.getMidY();
        playerZ            = configuration.getMidZ();

        topDown.setSliceType(SliceType.Z);
        sideX.setSliceType(SliceType.X);
        sideY.setSliceType(SliceType.Y);

        topDown.setPreferredSize(new Dimension(800,600));
        sideX.setPreferredSize(new Dimension(400,300));
        sideY.setPreferredSize(new Dimension(400,300));

        topDown.init();
        sideX.init();
        sideY.init();

        JSplitPane rootSplitPane  = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        JSplitPane southSplitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );

        rootSplitPane.setResizeWeight( 0.66 );
        rootSplitPane.setLeftComponent( new JScrollPane( topDown ));
        rootSplitPane.setRightComponent( southSplitPane );
        southSplitPane.setResizeWeight( 0.5d );
//        southSplitPane.setLeftComponent( new JScrollPane( sideX ));
//        southSplitPane.setRightComponent( new JScrollPane( sideY ));

        JPanel info     = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JToolBar bar1 = buildInfoToolBar1();
        JToolBar bar2 = buildInfoToolBar2();
        bar1.setAlignmentX(LEFT_ALIGNMENT);
        bar2.setAlignmentX(LEFT_ALIGNMENT);
        info.add(bar2);
        info.add(bar1);

        JToolBar imageBar = buildImageToolBar();

        add( rootSplitPane, BorderLayout.CENTER );
        add( info, BorderLayout.NORTH );
        add( imageBar, BorderLayout.SOUTH );
        add( colorsPanel, BorderLayout.EAST );

        mitmThread.setSourcePort(12345);
        mitmThread.start();

//        eventBus.fireEvent(new TargetServerChanged("mc.funky-clan.de"));
        eventBus.fireEvent(new TargetServerChanged("localhost"));
    }

    protected void firePlayerMoved() {
        eventBus.fireEvent( new PlayerMoved(playerX, playerY, playerZ, yaw, pitch, zShift));
    }

    public void setZShift( int zShift ) {
        this.zShift = zShift;
        zShiftLabel.setText("z shift: " + zShift);
        firePlayerMoved();
    }

    private JToolBar buildImageToolBar() {
        JToolBar imageBar = new JToolBar();

        imageBar.add( new AbstractAction( "auto" ) {
            @Override
            public void actionPerformed( ActionEvent e ) {
                topDown.setImage( null );
            }
        } );

        HashMap<String, BackgroundImage> images = configuration.getImages();

        for( final BackgroundImage image : images.values() ) {
            imageBar.add( new AbstractAction( image.getFilename(), image.getIcon() ) {
                @Override
                public void actionPerformed( ActionEvent e ) {
                    topDown.setImage( image );
                }
            } );
        }

        imageBar.addSeparator();
        imageBar.addSeparator();
        zShiftLabel = new JLabel();
        setZShift( 0 );
        imageBar.add( zShiftLabel );
        imageBar.addSeparator();
        imageBar.add( new AbstractAction( "raise z" ) {
            @Override
            public void actionPerformed( ActionEvent e ) {
                setZShift( zShift + 1 );
            }
        } );
        imageBar.addSeparator();
        imageBar.add( new AbstractAction( "lower z" ) {
            @Override
            public void actionPerformed( ActionEvent e ) {
                setZShift( zShift - 1 );
            }
        } );

        return imageBar;
    }

    private JToolBar buildInfoToolBar1() {
        JToolBar info = new JToolBar();

        info.add(playerInfo.getTargetConnection());
        info.addSeparator();

        return info;
    }

    protected void updateStats() {
        chunksText.setText("Chunks: " + chunksLoaded + "/"+chunksUnloaded );
        long max = Runtime.getRuntime().maxMemory();
        long free = Runtime.getRuntime().freeMemory();
        memoryText.setText("Mem: " + ((max-free)/1024/1024) + "/" + (max/1024/1024) );

        HashMap<Object, Double> results = benchmark.getResults();
        double eventBusTime = 0;
        if( results.containsKey(eventBus) ) {
            eventBusTime = results.get(eventBus);
        }
        double renderTime = 0;
        if( results.containsKey(ZoomPanel.class) ) {
            renderTime = results.get(ZoomPanel.class);
        }

        benchmarkText.setText(String.format("CPU/GFX: %.0f/%.0f", eventBusTime*100, renderTime*100));
    }

    private JToolBar buildInfoToolBar2() {
        JToolBar info = new JToolBar();
        chunksText = new JLabel("Chunks:");
        info.add(chunksText);
        info.addSeparator();

        memoryText = new JLabel("Mem: 0/0");
        info.add(memoryText);
        info.addSeparator();

        benchmarkText = new JLabel("CPU/GFX: 0/0");
        info.add(benchmarkText);
        info.addSeparator();

        info.add(playerInfo.getDirection());
        info.addSeparator();
        info.add(playerInfo.getPosition());
        info.addSeparator();
        return info;
    }
}
