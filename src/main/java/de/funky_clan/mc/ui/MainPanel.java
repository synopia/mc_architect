package de.funky_clan.mc.ui;

//~--- non-JDK imports --------------------------------------------------------

import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.model.BackgroundImage;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.Slice;
import de.funky_clan.mc.net.ClientThread;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;
import java.awt.event.ActionEvent;

import java.util.HashMap;

import javax.swing.*;

/**
 * @author synopia
 */
public class MainPanel extends JPanel {
    private ClientThread     clientThread;
    private Configuration    configuration;
    private Action           connection;
    private float            playerDir;
    private PlayerInfoLabels playerInfo;
    private int              playerX;
    private int              playerY;
    private int              playerZ;
    private SlicePanel       sideX;
    private SlicePanel       sideY;
    private SlicePanel       topDown;
    private int              zShift;
    private JLabel           zShiftLabel;

    public MainPanel( final Configuration configuration ) {
        super( new BorderLayout() );
        clientThread = new ClientThread();
        clientThread.start();
        this.configuration = configuration;
        playerX            = -configuration.getOriginX();
        playerY            = configuration.getOriginY();
        playerZ            = configuration.getOriginZ();

        Model model = configuration.getModel();

        topDown = new SlicePanel( model, configuration.createColors(), new Slice( model, Slice.SliceType.Z ));
        sideX   = new SlicePanel( model, configuration.createColors(), new Slice( model, Slice.SliceType.X ));
        sideY   = new SlicePanel( model, configuration.createColors(), new Slice( model, Slice.SliceType.Y ));

        JSplitPane rootSplitPane  = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        JSplitPane southSplitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );

        rootSplitPane.setResizeWeight( 0.66 );
        rootSplitPane.setLeftComponent( new JScrollPane( topDown ));
        rootSplitPane.setRightComponent( southSplitPane );
        southSplitPane.setResizeWeight( 0.5d );
        southSplitPane.setLeftComponent( new JScrollPane( sideX ));
        southSplitPane.setRightComponent( new JScrollPane( sideY ));

        JToolBar info     = buildInfoToolBar();
        JToolBar imageBar = buildImageToolBar();

        add( rootSplitPane, BorderLayout.CENTER );
        add( info, BorderLayout.NORTH );
        add( imageBar, BorderLayout.SOUTH );
    }

    public void connect() {
        clientThread.connect( "localhost", 12345, new ClientThread.DataListener() {
            @Override
            public void onConnect() {
                connection.putValue( Action.NAME, "Disconnect" );
            }
            @Override
            public void onDisconnect() {
                connection.putValue( Action.NAME, "Connect" );
            }
            @Override
            public void onPlayerPosition( final int x, final int y, final int z, final float radius ) {
                playerX   = x;
                playerY   = y;
                playerZ   = z;
                playerDir = radius;
                updatePlayerPos( x, y, z + zShift, radius );
            }
        } );
    }

    private void updatePlayerPos( final int x, final int y, final int z, final float radius ) {
        final int relX = y - configuration.getOriginY();
        final int relY = -x - configuration.getOriginX();
        final int relZ = z - configuration.getOriginZ();

        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                topDown.updatePlayerPos( relX, relY, relZ, (int) ( radius ) % 360 );
                sideX.updatePlayerPos( relX, relY, relZ, (int) ( radius ) % 360 );
                sideY.updatePlayerPos( relX, relY, relZ, (int) ( radius ) % 360 );
                playerInfo.updatePlayerPos( x, y, z, relX, relY, relZ, (int) ( radius ) % 360 );
            }
        } );
    }

    public void setZShift( int zShift ) {
        this.zShift = zShift;
        zShiftLabel.setText( "z shift: " + zShift );
        updatePlayerPos( playerX, playerY, playerZ + zShift, playerDir );
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

    private JToolBar buildInfoToolBar() {
        playerInfo = new PlayerInfoLabels( configuration.getModel() );
        connection = new AbstractAction( "Connect" ) {
            @Override
            public void actionPerformed( ActionEvent e ) {
                connect();
            }
        };

        JToolBar info = new JToolBar();

        info.add( connection );
        info.addSeparator();
        info.add( playerInfo.getDirection() );
        info.addSeparator();
        info.add( playerInfo.getAbsoluteWorld() );
        info.addSeparator();
        info.add( playerInfo.getAbsoluteModel() );
        info.addSeparator();
        info.add( playerInfo.getRelativeMid() );
        info.addSeparator();

        return info;
    }
}
