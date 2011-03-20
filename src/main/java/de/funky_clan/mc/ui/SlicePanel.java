package de.funky_clan.mc.ui;

//~--- non-JDK imports --------------------------------------------------------

import com.google.inject.Inject;
import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.*;
import de.funky_clan.mc.math.Position;
import de.funky_clan.mc.model.*;
import de.funky_clan.mc.ui.renderer.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author synopia
 */
public class SlicePanel extends ZoomPanel {
    private BackgroundImage image;
    @Inject
    private Model           model;
    @Inject
    private Player          player;
    private SelectedBlock   selectedBlock;
    @Inject
    private Slice           slice;
    private int             sliceNo;
    @Inject
    private EventBus        eventBus;
    private final List<Ore> ores = new ArrayList<Ore>();
    @Inject
    private Colors colors;

    @Inject
    private BlockRenderer<SelectedBlock> blockRenderer;
    @Inject
    private ImageRenderer imageRenderer;
    @Inject
    private PlayerRenderer playerRenderer;
    @Inject
    private SliceRenderer sliceRenderer;
    @Inject
    private OreRenderer oreRenderer;

    private Position position = new Position();

    @Override
    public void init() {
        super.init();
        position.setSlice(slice);
        position.setRenderContext(context);

        setFocusable(true);
        setAutoscrolls( true );
        context.setColors( colors );
        context.setWindowSize(50,50);
        addMouseListener( new MouseAdapter() {
            @Override
            public void mouseReleased( MouseEvent e ) {
                if( !e.isConsumed() && e.getButton()==MouseEvent.BUTTON1 ) {
                }
            }
        } );
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if( e.getWheelRotation()>0 ) {
                    setSliceNo(sliceNo+1);
                } else {
                    setSliceNo(sliceNo-1);
                }


            }
        });
        eventBus.registerCallback(PlayerMoved.class, new EventHandler<PlayerMoved>() {
            @Override
            public void handleEvent(PlayerMoved event) {
                player.setPosition(event.getX(), event.getY(), event.getZ());
                player.setDirection( (int)event.getYaw() );

                position.setWorld(event.getX(), event.getY(), event.getZ());
                scrollTo(position);
            }
        });

        eventBus.registerCallback(ChunkUpdate.class, new EventHandler<ChunkUpdate>() {
            @Override
            public void handleEvent(ChunkUpdate event) {
                repaint();
            }
        });

        eventBus.registerCallback(BlockUpdate.class, new EventHandler<BlockUpdate>() {
            @Override
            public void handleEvent(BlockUpdate event) {
                repaint();
            }
        });

        eventBus.registerCallback(OreDisplayUpdate.class, new EventHandler<OreDisplayUpdate>() {
            @Override
            public void handleEvent(OreDisplayUpdate event) {
                synchronized (ores) {
                    ores.clear();
                    ores.addAll(event.getOre());
                }
                repaint();
            }
        });
    }

    protected void scrollTo( Position pos ) {
        pos.sliceToWorld(slice.getType());
        scrollTo(pos.getSliceX(), pos.getSliceY(), pos.getSliceNo());
    }

    protected void scrollTo( double x, double y, int slice) {
        setSliceNo(slice);
        scrollTo(x, y);
    }

    @Override
    protected void paintContent( Graphics2D g ) {
        BackgroundImage image = this.image;

        if( image == null ) {
            image = model.getImage( slice.getType(), sliceNo );
        }

        g.setColor( context.getColors().getBackgroundColor() );
        g.fillRect( 0, 0, getWidth(), getHeight() );

        if( image != null ) {
            imageRenderer.render(image, context );
        }
        if( slice != null ) {
            slice.setSlice(sliceNo);
            sliceRenderer.render(slice, context);
        }

        synchronized (ores) {
            oreRenderer.render( ores, context);
        }

        if( selectedBlock != null ) {
            blockRenderer.render( selectedBlock, context );
        }

        if( player != null ) {
            playerRenderer.render(player, context);
        }
    }


    public void setImage( BackgroundImage image ) {
        this.image = image;
        repaint();
    }

    private void setSliceNo( int wz ) {
        if( sliceNo != wz ) {
            sliceNo = wz;
            repaint();
        }
    }

    @Override
    protected RenderContext createRenderContext() {
        return new RenderContext(slice);
    }

    public void setSliceType(SliceType type) {
        slice.setType(type);
        slice.setMaxRenderDepth( type==SliceType.Z?20:1 );
        player.setDrawViewCone( type==SliceType.Z );
    }
}
