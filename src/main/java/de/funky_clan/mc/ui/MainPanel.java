package de.funky_clan.mc.ui;

//~--- non-JDK imports --------------------------------------------------------

import com.google.inject.Inject;
import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.*;
import de.funky_clan.mc.events.mouse.MouseMoved;
import de.funky_clan.mc.events.mouse.MouseRectangle;
import de.funky_clan.mc.model.Box;
import de.funky_clan.mc.model.Chunk;
import de.funky_clan.mc.model.SliceType;
import de.funky_clan.mc.net.MitmThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author synopia
 */
public class MainPanel extends JPanel {
    @Inject
    private MitmThread mitmThread;
    @Inject
    private Configuration    configuration;

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
    private JLabel zShiftLabel;
    private EventBus         eventBus;
    @Inject ColorsPanel       colorsPanel;
    @Inject
    ScriptsPanel scriptsPanel;
    @Inject OrePanel orePanel;
    @Inject PlayerInfoToolbar playerInfoToolbar;
    @Inject StatisticsToolbar statisticsToolbar;
    @Inject ConnectionToolbar connectionToolbar;

    @Inject
    private Box selectionBox;

    private JLabel mousePosInfo;
    private JLabel selectionInfo;

    @Inject
    public MainPanel(final EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.registerCallback(MouseRectangle.class, new EventHandler<MouseRectangle>() {
            @Override
            public void handleEvent(MouseRectangle event) {
                selectionBox.set(event.getX(), event.getY(), event.getZ(), event.getEndX(), event.getEndY(), event.getEndZ() );
                selectionInfo.setText(String.format(
                        "Selection: (%.0f, %.0f, %.0f) -> (%.0f, %.0f, %.0f) = (%.0f, %.0f, %.0f)",
                        selectionBox.getStartX(), selectionBox.getStartY(), selectionBox.getStartZ(),
                        selectionBox.getEndX(), selectionBox.getEndY(), selectionBox.getEndZ(),
                        selectionBox.getEndX()-selectionBox.getStartX(), selectionBox.getEndY()-selectionBox.getStartY(), selectionBox.getEndZ()-selectionBox.getStartZ()
                ));
                repaint();
            }
        });
        eventBus.registerCallback(MouseMoved.class, new EventHandler<MouseMoved>() {
            @Override
            public void handleEvent(MouseMoved event) {
                int x = event.getX();
                int y = event.getY();
                int z = event.getZ();

                String pixelText = DataValues.find(configuration.getModel().getPixel(x,y,z,0)).toString();
                mousePosInfo.setText("Mouse: "+ x +", "+ y +", "+ z + " " + pixelText);
            }
        });
        eventBus.registerCallback(ColorChanged.class, new EventHandler<ColorChanged>() {
            @Override
            public void handleEvent(ColorChanged event) {
                repaint();
            }
        });

        eventBus.registerCallback(PlayerPositionUpdate.class, new EventHandler<PlayerPositionUpdate>() {
            @Override
            public void handleEvent(PlayerPositionUpdate event) {
                updatePlayerPosition(event);
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
    }

    private void updatePlayerPosition(PlayerPositionUpdate event) {
        double oldX = playerX;
        double oldY = playerY;
        double oldZ = playerZ;
        playerX = event.getX();
        playerY = event.getY();
        playerZ = event.getZ();
        yaw     = event.getYaw();
        pitch   = event.getPitch();

        boolean blockChanged = (int)playerX!=(int)oldX || (int)playerY!=(int)oldY || (int)playerZ!=(int)oldZ;
        boolean chunkChanged = Chunk.getChunkId(oldX, oldZ)!=Chunk.getChunkId(playerX, playerZ);

        firePlayerMoved( blockChanged, chunkChanged );
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
                        firePlayerMoved(true, false);
                        break;
                    case KeyEvent.VK_DOWN:
                        playerX--;
                        firePlayerMoved(true, false);
                        break;
                    case KeyEvent.VK_LEFT:
                        playerZ++;
                        firePlayerMoved(true, false);
                        break;
                    case KeyEvent.VK_RIGHT:
                        playerZ--;
                        firePlayerMoved(true, false);
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

        JSplitPane rootSplitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
        JSplitPane sliceViewSplitPane  = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        JSplitPane southSplitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );

        sliceViewSplitPane.setResizeWeight(0.66);
        sliceViewSplitPane.setLeftComponent(new JScrollPane(topDown));
        sliceViewSplitPane.setRightComponent(southSplitPane);
        southSplitPane.setResizeWeight( 0.5d );
        southSplitPane.setLeftComponent( new JScrollPane( sideX ));
        southSplitPane.setRightComponent( new JScrollPane( sideY ));

        rootSplitPane.setResizeWeight(0.80);
        rootSplitPane.setLeftComponent(sliceViewSplitPane);
        JTabbedPane tabs = new JTabbedPane();
        rootSplitPane.setRightComponent(tabs);
        tabs.addTab("Colors", colorsPanel);
        tabs.addTab("Scripts", scriptsPanel);
        tabs.addTab("Ore", orePanel);

        JPanel info     = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.X_AXIS));
        info.add( playerInfoToolbar );
        info.add( connectionToolbar);
        info.add( statisticsToolbar );

        JToolBar imageBar = buildToolBar();

        add( rootSplitPane, BorderLayout.CENTER );
        add( info, BorderLayout.NORTH );
        add( imageBar, BorderLayout.SOUTH );

        mitmThread.setSourcePort(12345);
        mitmThread.start();
    }

    protected void firePlayerMoved(boolean blockChanged, boolean chunkChanged ) {
        eventBus.fireEvent( new PlayerMoved(playerX, playerY, playerZ, yaw, pitch, zShift, blockChanged, chunkChanged));
    }

    public void setZShift( int zShift ) {
        this.zShift = zShift;
        zShiftLabel.setText("z shift: " + zShift);
        firePlayerMoved(true, false);
    }

    private JToolBar buildToolBar() {
        JToolBar toolBar = new JToolBar();

        zShiftLabel = new JLabel();
        setZShift( 0 );
        toolBar.add(zShiftLabel);
        toolBar.addSeparator();
        toolBar.add(new AbstractAction("raise z") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setZShift(zShift + 1);
            }
        });
        toolBar.addSeparator();
        toolBar.add(new AbstractAction("lower z") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setZShift(zShift - 1);
            }
        });

        toolBar.addSeparator();
        toolBar.add(mousePosInfo = new JLabel("Mouse: "));
        toolBar.addSeparator();
        toolBar.add(selectionInfo = new JLabel(""));
        return toolBar;
    }
}
