package de.funky_clan.mc.ui;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.action.CButton;
import bibliothek.gui.dock.common.action.CRadioButton;
import bibliothek.gui.dock.common.action.CRadioGroup;
import bibliothek.gui.dock.common.intern.CDockable;
import com.google.inject.Inject;
import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.model.ModelUpdate;
import de.funky_clan.mc.events.model.PlayerPositionUpdate;
import de.funky_clan.mc.events.script.ScriptFinished;
import de.funky_clan.mc.events.swing.ColorChanged;
import de.funky_clan.mc.events.swing.MouseMoved;
import de.funky_clan.mc.events.swing.MouseRectangle;
import de.funky_clan.mc.events.swing.OreDisplayUpdate;
import de.funky_clan.mc.math.Position;
import de.funky_clan.mc.model.Box;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.Ore;
import de.funky_clan.mc.model.PlayerBlock;
import de.funky_clan.mc.model.RenderContext;
import de.funky_clan.mc.model.SelectedBlock;
import de.funky_clan.mc.model.Slice;
import de.funky_clan.mc.model.SliceType;
import de.funky_clan.mc.ui.renderer.BlockRenderer;
import de.funky_clan.mc.ui.renderer.BoxRenderer;
import de.funky_clan.mc.ui.renderer.OreRenderer;
import de.funky_clan.mc.ui.renderer.PlayerRenderer;
import de.funky_clan.mc.ui.renderer.SliceRenderer;

import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author synopia
 */
public class SlicePanel extends ZoomPanel {
    private final List<Ore>              ores     = new ArrayList<Ore>();
    private final Position               position = new Position();
    @Inject
    private BlockRenderer<SelectedBlock> blockRenderer;
    private int                          blockStartX;
    private int                          blockStartY;
    private int                          blockStartZ;
    @Inject
    private BoxRenderer                  boxRenderer;
    @Inject
    private Colors                       colors;
    @Inject
    private SwingEventBus                eventBus;
    @Inject
    private EventDispatcher              eventDispatcher;
    private int                          lastMouseX;
    private int                          lastMouseY;
    private int                          lastMouseZ;
    @Inject
    private Model                        model;
    private MouseMode                    mouseMode;
    @Inject
    private OrePanel                     orePanel;
    @Inject
    private OreRenderer                  oreRenderer;
    @Inject
    private PlayerBlock playerBlock;
    @Inject
    private PlayerRenderer               playerRenderer;
    @Inject
    private Box                          selectedBox;
    private boolean                      selectionBoxMode;
    @Inject
    private Slice                        slice;
    private int                          sliceNo;
    @Inject
    private SliceRenderer                sliceRenderer;

    public enum MouseMode {ZOOM, SELECTION}

    @Override
    public void init() {
        super.init();
        position.setSlice( slice );
        position.setRenderContext( context );
        setFocusable( true );
        setAutoscrolls( true );
        context.setColors( colors );
        context.setWindowSize( 50, 50 );
        eventBus.subscribe(ScriptFinished.class, new EventHandler<ScriptFinished>() {
            @Override
            public void handleEvent(ScriptFinished event) {
                sliceRenderer.invalidate();
                repaint();
            }
        });
        eventBus.subscribe(PlayerPositionUpdate.class, new EventHandler<PlayerPositionUpdate>() {
            @Override
            public void handleEvent(PlayerPositionUpdate event) {
                playerBlock.setPosition(event.getX(), event.getY(), event.getZ());
                playerBlock.setDirection((int) event.getYaw());
                position.setWorld(event.getX(), event.getY(), event.getZ());
                scrollTo(position);
            }
        });
        eventBus.subscribe(ModelUpdate.class, new EventHandler<ModelUpdate>() {
            @Override
            public void handleEvent(ModelUpdate event) {
                sliceRenderer.invalidate();
                repaint();
            }
        });
        eventBus.subscribe(OreDisplayUpdate.class, new EventHandler<OreDisplayUpdate>() {
            @Override
            public void handleEvent(OreDisplayUpdate event) {
                if (event.getComponent() == SlicePanel.this) {
                    ores.clear();
                    ores.addAll(event.getOre());
                    repaint();
                }
            }
        });
        eventBus.subscribe(ColorChanged.class, new EventHandler<ColorChanged>() {
            @Override
            public void handleEvent(ColorChanged event) {
                sliceRenderer.invalidate();
                repaint();
            }
        });
    }

    @Override
    protected void onMouseRectangle( MouseEvent e, int x, int y, int width, int height ) {
        if( e.isShiftDown() || e.isControlDown() || ( mouseMode == MouseMode.ZOOM )) {
            applyWindow( x, y, width, height );
        } else {
            position.setScreen( x, y );

            int sx = position.getBlockX();
            int sy = position.getBlockY();
            int sz = position.getBlockZ();

            position.setScreen( x + width, y + height );

            int ex    = position.getBlockX();
            int ey    = position.getBlockY();
            int ez    = position.getBlockZ();
            int sizeX = ex - sx;
            int sizeY = ey - sy;
            int sizeZ = ez - sz;

            eventDispatcher.publish(new MouseRectangle(sx, sy, sz, sizeX, sizeY, sizeZ));
        }
    }

    @Override
    protected void onMouseDragged( MouseEvent e, int x, int y, int lastX, int lastY ) {
        double windowPosX = context.screenToSliceX( lastX ) - context.screenToSliceX( x )
                            + context.getWindowPositionX();
        double windowPosY = context.screenToSliceY( lastY ) - context.screenToSliceY( y )
                            + context.getWindowPositionY();

        context.setWindowPosition( windowPosX, windowPosY );
        repaint();
    }

    @Override
    protected void onMouseWheel( MouseWheelEvent e, int x, int y, int wheelRotation ) {
        if( e.isShiftDown() || e.isControlDown() || ( mouseMode == MouseMode.ZOOM )) {
            double scale = 1 + wheelRotation * 0.05;

            context.zoom( scale, scale, e.getPoint() );
            sliceRenderer.invalidate();
            repaint();
        } else {
            if( e.getWheelRotation() > 0 ) {
                setSliceNo( sliceNo + 1 );
            } else {
                setSliceNo( sliceNo - 1 );
            }
        }
    }

    @Override
    protected void onMouseMoved( MouseEvent e, int x, int y ) {
        position.setScreen( x, y, sliceNo );

        int blockX = position.getBlockX();
        int blockY = position.getBlockY();
        int blockZ = position.getBlockZ();

        if(( lastMouseX != blockX ) || ( lastMouseY != blockY ) || ( lastMouseZ != blockZ )) {
            eventDispatcher.publish(new MouseMoved(blockX, blockY, blockZ));
        }

        lastMouseX = blockX;
        lastMouseY = blockY;
        lastMouseZ = blockZ;

        if( selectionBoxMode ) {
            eventDispatcher.publish(new MouseRectangle(blockStartX, blockStartY, blockStartZ, blockX + 1, blockY + 1,
                    blockZ + 1));
            repaint();
        }
    }

    @Override
    protected void onMousePressed( MouseEvent e, int x, int y ) {
        position.setScreen( x, y, sliceNo );
        blockStartX = position.getBlockX();
        blockStartY = position.getBlockY();
        blockStartZ = position.getBlockZ();
        eventDispatcher.publish(new MouseRectangle(blockStartX, blockStartY, blockStartZ, blockStartX + 1,
                blockStartY + 1, blockStartZ + 1));
        selectionBoxMode = true;
        repaint();
    }

    @Override
    protected void onMouseReleased( MouseEvent e, int x, int y ) {
        selectionBoxMode = false;
    }

    protected void scrollTo( Position pos ) {
        pos.sliceToWorld( slice.getType() );
        scrollTo( pos.getSliceX(), pos.getSliceY(), pos.getSliceNo() );
    }

    protected void scrollTo( double x, double y, int slice ) {
        setSliceNo( slice );
        scrollTo( x, y );
    }

    @Override
    public boolean isRectMode( MouseEvent e ) {
        return super.isRectMode( e ) && ( e.isShiftDown() || e.isControlDown() || ( mouseMode == MouseMode.ZOOM ));
    }

    @Override
    protected void paintContent( Graphics2D g ) {
        g.setColor( context.getColors().getBackgroundColor() );
        g.fillRect( 0, 0, getWidth(), getHeight() );

        if( slice != null ) {
            slice.setSlice( sliceNo );
            sliceRenderer.render( slice, context );
        }

        synchronized( ores ) {
            oreRenderer.render( ores, context );
        }

        if( selectedBox != null ) {
            boxRenderer.render( selectedBox, context );
        }

        if( playerBlock != null ) {
            playerRenderer.render(playerBlock, context );
        }
    }

    private void setSliceNo( int wz ) {
        if( sliceNo != wz ) {
            sliceNo = wz;
            sliceRenderer.invalidate();
            repaint();
        }
    }

    @Override
    protected RenderContext createRenderContext() {
        return new RenderContext( slice );
    }

    public void setSliceType( SliceType type ) {
        slice.setType( type );
        slice.setMaxRenderDepth(( type == SliceType.Y )
                                ? 20
                                : 1 );
        playerBlock.setDrawViewCone( type == SliceType.Y );
    }

    public CDockable getDockable() {
        DefaultSingleCDockable dockable = new DefaultSingleCDockable( slice.getType().toString(), slice.getType().name,
                                              this );

        dockable.setExternalizable( false );

        CRadioButton selectionMode = new CRadioButton( "selection mode",
                                         new ImageIcon( Toolkit.getDefaultToolkit().getImage( "disabled_co.gif" ))) {
            @Override
            protected void changed() {
                if( this.isSelected() ) {
                    mouseMode = MouseMode.SELECTION;
                }
            }
        };
        CRadioButton zoomMode = new CRadioButton( "zoom mode",
                                    new ImageIcon( Toolkit.getDefaultToolkit().getImage( "insp_sbook.gif" ))) {
            @Override
            protected void changed() {
                if( this.isSelected() ) {
                    mouseMode = MouseMode.ZOOM;
                }
            }
        };
        CRadioGroup g         = new CRadioGroup();

        g.add( selectionMode );
        g.add( zoomMode );
        dockable.addAction( selectionMode );
        dockable.addAction( zoomMode );
        dockable.addAction( new CButton( "focus player",
                                         new ImageIcon( Toolkit.getDefaultToolkit().getImage( "collapseall.gif" ))) {
            @Override
            protected void action() {
                position.setWorld( playerBlock.getPositionX(), playerBlock.getPositionY(), playerBlock.getPositionZ() );
                scrollTo( position );
            }
        } );
        dockable.addAction( orePanel.getMenu( this ));

        return dockable;
    }
}
