package de.funky_clan.mc.ui;

import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.swing.Initialize;
import de.funky_clan.mc.events.swing.MouseMoved;
import de.funky_clan.mc.events.swing.MouseRectangle;
import de.funky_clan.mc.model.Box;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.SliceType;
import de.funky_clan.mc.services.EntityPositionService;
import de.funky_clan.mc.services.PlayerPositionService;
import de.funky_clan.mc.ui.widgets.ConnectionWidgetFactory;
import de.funky_clan.mc.ui.widgets.PlayerInfoWidgetFactory;
import de.funky_clan.mc.ui.widgets.StatisticWidgetFactory;
import de.funky_clan.mc.util.StatusBar;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

/**
 * @author synopia
 */
public class MainPanel extends CControl {
    @Inject
    ColorsPanel                   colorsPanel;
    @Inject
    ConnectionWidgetFactory       connectionWidgetFactory;
    @Inject
    private ConsolePanel          consolePanel;
    @Inject
    @Named( "Info" )
    private StatusBar             infoBar;
    @Inject
    private Model                 model;
    @Inject
    PlayerInfoWidgetFactory       playerInfoWidgetFactory;
    @Inject
    private PlayerPositionService playerPositionService;
//    @Inject
//    private EntityPositionService entityPositionService;
    @Inject
    ScriptsPanel                  scriptsPanel;
    @Inject @Named("SelectionBox")
    private Box                   selectionBox;
    @Inject
    private SlicePanel            sideX;
    @Inject
    private SlicePanel            sideY;
    @Inject
    StatisticWidgetFactory        statisticWidgetFactory;
    @Inject
    @Named( "Status" )
    private StatusBar             statusBar;
    private JLabel                statusText;
    private JToolBar              toolBar;
    @Inject
    private SlicePanel            topDown;
    private int                   yShift;
    private JLabel                yShiftLabel;

    @Inject
    public MainPanel( final SwingEventBus eventBus ) {
        eventBus.subscribe(MouseRectangle.class, new EventHandler<MouseRectangle>() {
            @Override
            public void handleEvent(MouseRectangle event) {
                selectionBox.set(event.getX(), event.getY(), event.getZ(), event.getEndX(), event.getEndY(),
                        event.getEndZ());
                statusText.setText(
                        String.format(
                                "Selection: (%.0f, %.0f, %.0f) -> (%.0f, %.0f, %.0f) = (%.0f, %.0f, %.0f)",
                                selectionBox.getStartX(), selectionBox.getStartY(), selectionBox.getStartZ(),
                                selectionBox.getEndX(), selectionBox.getEndY(), selectionBox.getEndZ(),
                                selectionBox.getEndX() - selectionBox.getStartX(),
                                selectionBox.getEndY() - selectionBox.getStartY(),
                                selectionBox.getEndZ() - selectionBox.getStartZ()));
                MainPanel.this.getContentArea().repaint();
            }
        });
        eventBus.subscribe(MouseMoved.class, new EventHandler<MouseMoved>() {
            @Override
            public void handleEvent(MouseMoved event) {
                int x = event.getX();
                int y = event.getY();
                int z = event.getZ();
                String pixelText = DataValues.find(model.getPixel(x, y, z, 0)).toString();

                statusText.setText("Mouse: " + x + ", " + y + ", " + z + " " + pixelText);
            }
        });
        eventBus.subscribe(Initialize.class, new EventHandler<Initialize>() {
            @Override
            public void handleEvent(Initialize event) {
                onInit();
            }
        });
        buildToolBar();
    }

    protected void onInit() {
        topDown.setSliceType( SliceType.Y );
        sideX.setSliceType( SliceType.X );
        sideY.setSliceType( SliceType.Z );
        topDown.setPreferredSize( new Dimension( 800, 600 ));
        sideX.setPreferredSize( new Dimension( 400, 300 ));
        sideY.setPreferredSize( new Dimension( 400, 300 ));
        topDown.init();
        sideX.init();
        sideY.init();

        CGrid grid = new CGrid( this );

        grid.add( 0, 0, 2, 1, topDown.getDockable() );
        grid.add( 0, 1, 1, 1, sideX.getDockable() );
        grid.add( 1, 1, 1, 1, sideY.getDockable() );
        this.getContentArea().deploy( grid );
        this.getContentArea().getEast().add( new DefaultDockable( colorsPanel, "Colors" ), 0 );
        this.getContentArea().getEast().add( new DefaultDockable( scriptsPanel, "Scripts" ), 1 );
        this.getContentArea().getSouth().add( new DefaultDockable( consolePanel, "Console" ), 0 );
        infoBar.addZone( "dir", playerInfoWidgetFactory.getDirection() );
        infoBar.addZone( "pos", playerInfoWidgetFactory.getPosition() );
        infoBar.addZone( "status", connectionWidgetFactory.getConnectionStatus() );
        infoBar.addZone( "host", connectionWidgetFactory.getHost() );
        statusBar.addZone( "statusText", statusText = new JLabel(), "*" );
        statusBar.addZone( "benchmark", statisticWidgetFactory.getBenchmarkText() );
        statusBar.addZone( "chunks", statisticWidgetFactory.getChunksText() );
        statusBar.addZone( "mem", statisticWidgetFactory.getMemoryText() );
        statusBar.addZone( "ore", statisticWidgetFactory.getOreText() );
    }

    public void setYShift( int yShift ) {
        this.yShift = yShift;
        yShiftLabel.setText( "player shift: " + yShift );
        playerPositionService.setYShift( yShift );
    }

    private JToolBar buildToolBar() {
        toolBar     = new JToolBar( JToolBar.HORIZONTAL );
        yShiftLabel = new JLabel( "player shift: 0" );
        toolBar.add( yShiftLabel );
        toolBar.addSeparator();
        toolBar.add( new AbstractAction( "raise player" ) {
            @Override
            public void actionPerformed( ActionEvent e ) {
                setYShift( yShift + 1 );
            }
        } );
        toolBar.addSeparator();
        toolBar.add( new AbstractAction( "lower player" ) {
            @Override
            public void actionPerformed( ActionEvent e ) {
                setYShift( yShift - 1 );
            }
        } );

        return toolBar;
    }

    public JToolBar getToolBar() {
        return toolBar;
    }
}
