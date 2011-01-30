package de.funky_clan.mc.ui;

import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.Slice;
import de.funky_clan.mc.net.ClientThread;

import javax.swing.*;
import java.awt.*;

/**
 * @author synopia
 */
public class MainPanel extends JPanel {
    private Configuration      configuration;
    private final ClientThread clientThread;

    private SlicePanel topDown;
    private SlicePanel sideX;
    private SlicePanel sideY;
    private PlayerInfoPanel playerInfo;

    public MainPanel( final Configuration configuration ) {
        super( new BorderLayout() );
        this.configuration = configuration;

        Model model = configuration.getModel();

        playerInfo  = new PlayerInfoPanel(model);

        topDown     = new SlicePanel(model, configuration.createColors(), new Slice(model, Slice.SliceType.Z) );
        sideX       = new SlicePanel(model, configuration.createColors(), new Slice(model, Slice.SliceType.X) );
        sideY       = new SlicePanel(model, configuration.createColors(), new Slice(model, Slice.SliceType.Y) );

        JSplitPane rootSplitPane  = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane southSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        rootSplitPane.setResizeWeight( 0.66 );
        rootSplitPane.setLeftComponent(new JScrollPane(topDown));
        rootSplitPane.setRightComponent(southSplitPane);

        southSplitPane.setResizeWeight( 0.5d );
        southSplitPane.setLeftComponent(new JScrollPane(sideX));
        southSplitPane.setRightComponent(new JScrollPane(sideY));

        add( rootSplitPane,  BorderLayout.CENTER );
        add( playerInfo, BorderLayout.NORTH );

        clientThread = new ClientThread();
        clientThread.start();
        boolean connected = clientThread.connect("localhost", 12345, new ClientThread.DataListener() {
            @Override
            public void onPlayerPosition(final int x, final int y, final int z, final float radius) {
                final int relX = y-configuration.getOriginY();
                final int relY = -x-configuration.getOriginX();
                final int relZ = z-configuration.getOriginZ();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        topDown.updatePlayerPos(relX, relY, relZ, (int) (radius) % 360);
                        sideX.updatePlayerPos(relX, relY, relZ, (int) (radius) % 360);
                        sideY.updatePlayerPos(relX, relY, relZ, (int) (radius) % 360);
                        playerInfo.updatePlayerPos(x, y, z, relX, relY, relZ, (int) (radius) % 360);
                    }
                });

            }
        });
        System.out.println(connected);

    }


}
