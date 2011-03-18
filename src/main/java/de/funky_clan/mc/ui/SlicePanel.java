package de.funky_clan.mc.ui;

//~--- non-JDK imports --------------------------------------------------------

import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.OreFound;
import de.funky_clan.mc.events.PlayerMoved;
import de.funky_clan.mc.math.Point2d;
import de.funky_clan.mc.math.Point2i;
import de.funky_clan.mc.math.Point3d;
import de.funky_clan.mc.math.Point3i;
import de.funky_clan.mc.model.*;
import de.funky_clan.mc.events.ChunkUpdate;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

/**
 * @author synopia
 */
public class SlicePanel extends ZoomPanel {
    private BackgroundImage image;
    private Model           model;
    private Player          player;
    private SelectedBlock   selectedBlock;
    private Slice           slice;
    private int             sliceNo;
    private EventBus        eventBus = EventDispatcher.getDispatcher().getModelEventBus();
    private List<Ore>       ores = new ArrayList<Ore>();
    Configuration.Colors colors;

    public SlicePanel( Model model, Configuration.Colors colors, final Slice slice ) {
        this.model = model;
        this.slice = slice;
        this.colors = colors;
    }

    @Override
    public void onInit() {
        super.onInit();

        setFocusable(true);
        setAutoscrolls( true );
        player  = new Player( slice.getType() == SliceType.Z );
        final SliceRenderContext context = (SliceRenderContext) this.context;
        context.setColors( colors );
        context.setWindowSize(new Point2d(50,50));
        addMouseListener( new MouseAdapter() {
            @Override
            public void mouseReleased( MouseEvent e ) {
                if( !e.isConsumed() && e.getButton()==MouseEvent.BUTTON1 ) {
                    if( selectedBlock == null ) {
                        selectedBlock = new SelectedBlock();
                    } else {
                        selectedBlock.repaint( SlicePanel.this, context );
                    }

                    Point2i pos = new Point2i(-e.getX(), -e.getY());
                    pos.add(context.getScreenSize());
                    selectedBlock.setPosition(context.screenToWorld(pos));
                    selectedBlock.repaint( SlicePanel.this, context );
                }
            }
        } );
        eventBus.registerCallback(PlayerMoved.class, new EventHandler<PlayerMoved>() {
            @Override
            public void handleEvent(PlayerMoved event) {
                player.repaint( SlicePanel.this, context );

                player.setPosition(new Point3d(event.getX(), event.getY(), event.getZ()));
                player.setDirection( (int)event.getYaw() );
                scrollTo(player.getPosition());
            }
        });

        eventBus.registerCallback(ChunkUpdate.class, new EventHandler<ChunkUpdate>() {
            @Override
            public void handleEvent(ChunkUpdate event) {
                repaint();
            }
        });

        eventBus.registerCallback(OreFound.class, new EventHandler<OreFound>() {
            @Override
            public void handleEvent(OreFound event) {
                ores.addAll(event.getOres());
                repaint();
            }
        });
    }

    protected void scrollTo( Point3d position ) {
        Point3d slicePos = slice.worldToSlice(position);

        setSliceNo((int) slicePos.z());
        scrollTo(new Point2d(slicePos.x(), slicePos.y()));
    }

    @Override
    protected void paintComponent( Graphics g ) {
        initContext((Graphics2D) g);

        BackgroundImage image = this.image;

        if( image == null ) {
            image = model.getImage( slice.getType(), sliceNo );
        }

        g.setColor( context.getColors().getBackgroundColor() );
        g.fillRect( 0, 0, getWidth(), getHeight() );

        if( image != null ) {
            image.render( context );
        }
        if( slice != null ) {
            slice.setSlice(sliceNo);
            slice.render( context );
        }
        if( selectedBlock != null ) {
            selectedBlock.render( (SliceRenderContext) context );
        }

        if( player != null ) {
            player.render( (SliceRenderContext) context );
        }

        super.paintComponent( g );
    }

    private void initContext( Graphics2D g ) {
        AffineTransform instance = AffineTransform.getRotateInstance(Math.PI, getWidth() / 2, getHeight() / 2);
        g.setTransform(instance);
        context.setGraphics(g);
    }

    public void setImage( BackgroundImage image ) {
        this.image = image;
        repaint();
    }

    private void setSliceNo( int wz ) {
        if( sliceNo != wz ) {
            sliceNo = wz;
            repaint();
        } else {
            sliceNo = wz;
            player.repaint( SlicePanel.this, context );
        }
    }

    @Override
    protected RenderContext createRenderContext() {
        return new SliceRenderContext(slice);
    }
}
