package de.funky_clan.mc.ui;

//~--- non-JDK imports --------------------------------------------------------

import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.OreFound;
import de.funky_clan.mc.events.PlayerMoved;
import de.funky_clan.mc.model.*;
import de.funky_clan.mc.events.ChunkUpdate;

//~--- JDK imports ------------------------------------------------------------

import javax.vecmath.Point2d;
import javax.vecmath.Point2i;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    public SlicePanel( Model model, Configuration.Colors colors, final Slice slice ) {
        this.model = model;
        this.slice = slice;
        setFocusable(true);
        setAutoscrolls( true );
        player  = new Player( slice.getType() == SliceType.Z );
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

                int map[] = slice.mapWorldToSlice( (int) event.getX(), (int) event.getY(), (int) event.getZ() );
                int wx    = map[0];
                int wy    = map[1];
                int wz    = map[2];

                player.setPosition( new Point2d(wx,wy) );
                player.setZ(wz);
                player.setDirection( (int)event.getYaw() );
                setSliceNo( wz );
                scrollToPlayer( wx, wy );
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

    private void scrollToPlayer( int wx, int wy ) {
        Point2d windowSize = context.getWindowSize();
        Point2d halfWindowSize = new Point2d(windowSize);
        halfWindowSize.scale(-0.5);

        Point2d start = new Point2d(wx, wy);
        start.add( halfWindowSize );
        context.init( start, windowSize, context.getScreenSize() );
        repaint();
    }

    @Override
    protected void paintComponent( Graphics g ) {
        initContext( (Graphics2D) g );

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
            selectedBlock.render( context );
        }

        if( player != null ) {
            player.render( context );
        }

        super.paintComponent( g );
    }

    private void initContext( Graphics2D g ) {
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
}
