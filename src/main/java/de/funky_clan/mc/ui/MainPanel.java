package de.funky_clan.mc.ui;

import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.net.ClientThread;

import javax.swing.*;
import java.awt.*;

/**
 * @author synopia
 */
public class MainPanel extends JPanel {
    private Configuration      configuration;
    private final ClientThread clientThread;

    private TopDownPanel    topDown;
    private SidePanel       side;
    private PlayerInfoPanel playerInfo;

    public MainPanel( final Configuration configuration ) {
        super( new BorderLayout() );
        this.configuration = configuration;

        Model model = configuration.getModel();

        playerInfo  = new PlayerInfoPanel(model);

        topDown     = new TopDownPanel(model, configuration.createColors() );
        side        = new SidePanel(model, configuration.createColors() );

        JSplitPane  splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.add( new JScrollPane(topDown) );
        splitPane.add( new JScrollPane(side) );

        add( splitPane,  BorderLayout.CENTER );
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
                        side.updatePlayerPos(relX, relY, relZ, (int) (radius) % 360);
                        playerInfo.updatePlayerPos(x, y, z, relX, relY, relZ, (int) (radius) % 360);
                    }
                });

            }
        });
        System.out.println(connected);

    }


}
