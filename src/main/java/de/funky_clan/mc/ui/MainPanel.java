package de.funky_clan.mc.ui;

//~--- non-JDK imports --------------------------------------------------------

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.action.CButton;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.common.intern.DefaultCDockable;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.swing.*;
import de.funky_clan.mc.model.Box;
import de.funky_clan.mc.model.SliceType;
import de.funky_clan.mc.services.PlayerPositionService;
import de.funky_clan.mc.ui.widgets.ConnectionWidgetFactory;
import de.funky_clan.mc.ui.widgets.PlayerInfoWidgetFactory;
import de.funky_clan.mc.ui.widgets.StatisticWidgetFactory;
import de.funky_clan.mc.util.StatusBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author synopia
 */
public class MainPanel extends CControl {
    @Inject
    private Configuration    configuration;

    @Inject
    private SlicePanel       sideX;
    @Inject
    private SlicePanel       sideY;
    @Inject
    private SlicePanel       topDown;
    private int              zShift;
    private JLabel zShiftLabel;
    @Inject ColorsPanel       colorsPanel;
    @Inject
    ScriptsPanel scriptsPanel;
    @Inject OrePanel orePanel;
    @Inject
    PlayerInfoWidgetFactory playerInfoWidgetFactory;
    @Inject
    ConnectionWidgetFactory connectionWidgetFactory;
    @Inject
    StatisticWidgetFactory statisticWidgetFactory;
    @Inject
    private PlayerPositionService playerPositionService;

    @Inject @Named("Info")
    private StatusBar infoBar;
    @Inject @Named("Status")
    private StatusBar statusBar;

    @Inject
    private Box selectionBox;

    private JLabel statusText;
    private JToolBar toolBar;

    @Inject
    public MainPanel(final SwingEventBus eventBus) {
        eventBus.registerCallback(MouseRectangle.class, new EventHandler<MouseRectangle>() {
            @Override
            public void handleEvent(MouseRectangle event) {
                selectionBox.set(event.getX(), event.getY(), event.getZ(), event.getEndX(), event.getEndY(), event.getEndZ() );
                statusText.setText(String.format(
                        "Selection: (%.0f, %.0f, %.0f) -> (%.0f, %.0f, %.0f) = (%.0f, %.0f, %.0f)",
                        selectionBox.getStartX(), selectionBox.getStartY(), selectionBox.getStartZ(),
                        selectionBox.getEndX(), selectionBox.getEndY(), selectionBox.getEndZ(),
                        selectionBox.getEndX()-selectionBox.getStartX(), selectionBox.getEndY()-selectionBox.getStartY(), selectionBox.getEndZ()-selectionBox.getStartZ()
                ));
                MainPanel.this.getContentArea().repaint();
            }
        });
        eventBus.registerCallback(MouseMoved.class, new EventHandler<MouseMoved>() {
            @Override
            public void handleEvent(MouseMoved event) {
                int x = event.getX();
                int y = event.getY();
                int z = event.getZ();

                String pixelText = DataValues.find(configuration.getModel().getPixel(x,y,z,0)).toString();
                statusText.setText("Mouse: "+ x +", "+ y +", "+ z + " " + pixelText);
            }
        });
        eventBus.registerCallback(ColorChanged.class, new EventHandler<ColorChanged>() {
            @Override
            public void handleEvent(ColorChanged event) {
                MainPanel.this.getContentArea().repaint();
            }
        });

        eventBus.registerCallback(Initialize.class, new EventHandler<Initialize>() {
            @Override
            public void handleEvent(Initialize event) {
                onInit();
            }
        });
        buildToolBar();

    }

    protected void onInit() {

        topDown.setSliceType(SliceType.Z);
        sideX.setSliceType(SliceType.X);
        sideY.setSliceType(SliceType.Y);

        topDown.setPreferredSize(new Dimension(800,600));
        sideX.setPreferredSize(new Dimension(400,300));
        sideY.setPreferredSize(new Dimension(400,300));

        topDown.init();
        sideX.init();
        sideY.init();

        CGrid grid = new CGrid(this);

        grid.add(0, 0, 2, 1, topDown.getDockable());
        grid.add(0, 1, 1, 1, sideX.getDockable());
        grid.add(1, 1, 1, 1, sideY.getDockable());

        this.getContentArea().deploy(grid);

        this.getContentArea().getEast().add(new DefaultDockable(colorsPanel, "Colors"), 0);
        this.getContentArea().getEast().add(new DefaultDockable(scriptsPanel, "Scripts"), 1);
        this.getContentArea().getEast().add(new DefaultDockable(orePanel, "Ore"), 2);

        infoBar.addZone( "dir", playerInfoWidgetFactory.getDirection());
        infoBar.addZone( "pos", playerInfoWidgetFactory.getPosition());
        infoBar.addZone( "status", connectionWidgetFactory.getConnectionStatus() );
        infoBar.addZone( "host", connectionWidgetFactory.getHost() );

        statusBar.addZone( "statusText", statusText=new JLabel(), "*");
        statusBar.addZone( "benchmark", statisticWidgetFactory.getBenchmarkText());
        statusBar.addZone( "chunks", statisticWidgetFactory.getChunksText());
        statusBar.addZone( "mem", statisticWidgetFactory.getMemoryText());
        statusBar.addZone( "ore", statisticWidgetFactory.getOreText());

    }

    public void setZShift( int zShift ) {
        this.zShift = zShift;
        zShiftLabel.setText("player shift: " + zShift);
        playerPositionService.setZShift(zShift);
    }

    private JToolBar buildToolBar() {
        toolBar = new JToolBar(JToolBar.HORIZONTAL);

        zShiftLabel = new JLabel("player shift: 0");
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

        return toolBar;
    }

    public JToolBar getToolBar() {
        return toolBar;
    }
}
