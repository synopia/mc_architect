package de.funky_clan.mc.ui;

//~--- non-JDK imports --------------------------------------------------------

import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.model.BackgroundImage;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.Slice;
import de.funky_clan.mc.model.SliceType;
import de.funky_clan.mc.net.MitmThread;
import de.funky_clan.mc.net.protocol.ClientProtocol9;
import de.funky_clan.mc.net.protocol.ServerProtocol9;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.*;

/**
 * @author synopia
 */
public class MainPanel extends JPanel {
    private MitmThread mitmThread;
    private Configuration    configuration;
    private JTextField       host;
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
        mitmThread = new MitmThread(12345, new ClientProtocol9.ClientHandler() {
            @Override
            public void onPlayerUpdate(double x, double y, double z, float yaw, float pitch) {
                playerX   = (int)x;
                playerY   = (int)y;
                playerZ   = (int)z;
                playerDir = (int)yaw;
                updatePlayerPos( playerX, playerY, playerZ + zShift, playerDir );
            }

            @Override
            public void onConnect() {
                host.setEditable(false);
            }

            @Override
            public void onDisconnect() {
                host.setEditable(true);
            }
        }, new ServerProtocol9.ServerHandler() {
            @Override
            public void onChunkUpdate(int sx, int sy, int sz, int sizeX, int sizeY, int sizeZ, byte[] data) {
                configuration.getModel().setBlock(sx, sy, sz, sizeX, sizeY, sizeZ, data);
                repaint();
            }

            @Override
            public void onConnect() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onDisconnect() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        this.configuration = configuration;
        playerX            = configuration.getOriginX();
        playerY            = configuration.getOriginY();
        playerZ            = configuration.getOriginSlice();

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

        JToolBar info     = buildInfoToolBar();
        JToolBar imageBar = buildImageToolBar();

        add( rootSplitPane, BorderLayout.CENTER );
        add( info, BorderLayout.NORTH );
        add( imageBar, BorderLayout.SOUTH );

        mitmThread.start();
    }

    private void updatePlayerPos( final int x, final int y, final int z, final float radius ) {
        final int relX = x ;
        final int relY = y ;
        final int relZ = z ;

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
        updatePlayerPos( playerX, playerY + zShift, playerZ, playerDir );
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
        host = new JTextField();
        host.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mitmThread.setTargetHost( host.getText() );
                mitmThread.setTargetPort(25565);
            }
        });
        host.setText("localhost");
        mitmThread.setTargetHost( host.getText() );
        mitmThread.setTargetPort(25565);
//        host.setText("mc.funky-clan.de");

        JToolBar info = new JToolBar();

        info.add( host );
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
