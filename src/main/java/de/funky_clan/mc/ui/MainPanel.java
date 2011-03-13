package de.funky_clan.mc.ui;

//~--- non-JDK imports --------------------------------------------------------

import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.file.RegionFileCache;
import de.funky_clan.mc.model.BackgroundImage;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.Slice;
import de.funky_clan.mc.model.SliceType;
import de.funky_clan.mc.model.events.RequestChunkData;
import de.funky_clan.mc.net.protocol.events.ChunkUpdate;
import de.funky_clan.mc.ui.events.PlayerMoved;
import de.funky_clan.mc.net.MitmThread;
import de.funky_clan.mc.net.protocol.events.ConnectionEstablished;
import de.funky_clan.mc.net.protocol.events.ConnectionLost;
import de.funky_clan.mc.net.protocol.events.PlayerPositionUpdate;
import de.funky_clan.mc.ui.events.TargetServerChanged;
import org.jnbt.ByteArrayTag;
import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.*;

/**
 * @author synopia
 */
public class MainPanel extends JPanel {
    private MitmThread mitmThread;
    private Configuration    configuration;
    private JTextField       host;
    private PlayerInfoLabels playerInfo;
    private double           playerX;
    private double           playerY;
    private double           playerZ;
    private float            yaw;
    private float            pitch;
    private SlicePanel       sideX;
    private SlicePanel       sideY;
    private SlicePanel       topDown;
    private int              zShift;
    private JLabel           zShiftLabel;
    private EventBus         eventBus = EventDispatcher.getDispatcher().getModelEventBus();

    public MainPanel( final Configuration configuration ) {
        super( new BorderLayout() );
        mitmThread = new MitmThread(12345);

        eventBus.registerCallback(RequestChunkData.class, new EventHandler<RequestChunkData>() {
            @Override
            public void handleEvent(RequestChunkData event) {
                DataInputStream inputStream = RegionFileCache.getChunkDataInputStream(new File("d:/games/minecraft/world"), event.getChunkX(), event.getChunkZ());
                try {
                    System.out.println("Loading chunk "+event.getChunkX()+", 0, "+event.getChunkZ());
                    NBTInputStream nbt = new NBTInputStream(inputStream);
                    CompoundTag root=(CompoundTag) nbt.readTag();
                    CompoundTag level = (CompoundTag) root.getValue().get("Level");
                    ByteArrayTag blocks = (ByteArrayTag) level.getValue().get("Blocks");

                    eventBus.fireEvent(new ChunkUpdate(event.getChunkX() << 4, 0, event.getChunkZ()<<4, 1 << 4, 1 << 7, 1 << 4, blocks.getValue()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        eventBus.registerCallback(ChunkUpdate.class, new EventHandler<ChunkUpdate>() {
            @Override
            public void handleEvent(ChunkUpdate event) {
                configuration.getModel().setBlock(event.getSx(), event.getSy(), event.getSz(), event.getSizeX(), event.getSizeY(), event.getSizeZ(), event.getData());
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

        this.configuration = configuration;
        playerX            = configuration.getMidX();
        playerY            = configuration.getMidY();
        playerZ            = configuration.getMidZ();

        Model model = configuration.getModel();

        topDown = new SlicePanel( model, configuration.createColors(), new Slice( model, SliceType.Z ));
        sideX   = new SlicePanel( model, configuration.createColors(), new Slice( model, SliceType.X ));
        sideY   = new SlicePanel( model, configuration.createColors(), new Slice( model, SliceType.Y ));

        topDown.setPreferredSize(new Dimension(800,600));
        sideX.setPreferredSize(new Dimension(400,300));
        sideY.setPreferredSize(new Dimension(400,300));

        JSplitPane rootSplitPane  = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        JSplitPane southSplitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );

        rootSplitPane.setResizeWeight( 0.66 );
        rootSplitPane.setLeftComponent( new JScrollPane( topDown ));
        rootSplitPane.setRightComponent( southSplitPane );
        southSplitPane.setResizeWeight( 0.5d );
        southSplitPane.setLeftComponent( new JScrollPane( sideX ));
        southSplitPane.setRightComponent( new JScrollPane( sideY ));

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

        mitmThread.start();

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
        playerInfo = new PlayerInfoLabels( configuration.getModel() );

        JToolBar info = new JToolBar();

        info.add(playerInfo.getTargetConnection());
        info.addSeparator();
        info.add(playerInfo.getDirection());
        info.addSeparator();
        info.add(playerInfo.getPosition());
        info.addSeparator();

        return info;
    }

    private JToolBar buildInfoToolBar2() {
        JToolBar info = new JToolBar();
        info.add( new JLabel("Chunks: 0/0") );
        info.addSeparator();
        return info;
    }
}
