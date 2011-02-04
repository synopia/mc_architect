package de.funky_clan.mc.ui;

//~--- non-JDK imports --------------------------------------------------------

import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.model.*;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author synopia
 */
public class SlicePanel extends ZoomPanel {
    private RenderContext   context;
    private BackgroundImage image;
    private Model           model;
    private Player          player;
    private SelectedBlock   selectedBlock;
    private Slice           slice;
    private int             sliceNo;

    public SlicePanel( Model model, Configuration.Colors colors, final Slice slice ) {
        this.model = model;
        this.slice = slice;
        setZoom( 2 * (float) slice.getWidth() / slice.getHeight() );
        setFocusable( true );
        setAutoscrolls( true );
        player  = new Player( slice.getType() == Slice.SliceType.Z );
        context = new RenderContext( model );
        context.setColors( colors );
        addMouseListener( new MouseAdapter() {
            @Override
            public void mouseReleased( MouseEvent e ) {
                if( !e.isConsumed() ) {
                    if( selectedBlock == null ) {
                        selectedBlock = new SelectedBlock();
                    } else {
                        selectedBlock.repaint( SlicePanel.this, context );
                    }

                    int x = context.pixelToWorldX( e.getX() );
                    int y = context.pixelToWorldY( e.getY() );

                    selectedBlock.setX( x );
                    selectedBlock.setY( y );
                    selectedBlock.repaint( SlicePanel.this, context );
                }
            }
        } );
    }

    public void updatePlayerPos( int x, int y, int z, int angle ) {
        player.repaint( SlicePanel.this, context );

        int map[] = slice.mapWorldToSlice( x, y, z );
        int wx    = map[0];
        int wy    = map[1];
        int wz    = map[2];

        player.setX( wx );
        player.setY( wy );
        player.setZ( wz );
        player.setDirection( angle );
        setSliceNo( wz );
        scrollToPlayer( wx, wy );
    }

    private void scrollToPlayer( int wx, int wy ) {
        Rectangle rect = new Rectangle( context.worldToPixelX( wx - context.getWidth() / 2 ),
                                        context.worldToPixelY( wy - context.getHeight() / 2 ),
                                        context.worldToPixelX( context.getWidth() ),
                                        context.worldToPixelY( context.getHeight() ));

        scrollRectToVisible( rect );
    }

    @Override
    public void applyZoom( double zoom ) {
        Dimension newSize = new Dimension( (int) ( slice.getWidth() * zoom ), (int) ( slice.getHeight() * zoom ));

        setPreferredSize( newSize );
        revalidate();
    }

    @Override
    protected void paintComponent( Graphics g ) {
        initContext( (Graphics2D) g );

        BackgroundImage image = this.image;

        if( image == null ) {
            image = model.getImage( slice.getType(), sliceNo );
        }

        if( image != null ) {
            image.render( context );
        } else {
            g.setColor( context.getColors().getBackgroundColor() );
            g.fillRect( 0, 0, getWidth(), getHeight() );
        }

        if( slice != null ) {
            slice.setSlice( sliceNo );
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
        context.setGraphics( g );

        int windowWidth  = getParent().getWidth();
        int windowHeight = getParent().getHeight();
        int screenWidth  = getWidth();
        int screenHeight = getHeight();

        context.setScreenSize( screenWidth, screenHeight, slice.getWidth(), slice.getHeight() );
        context.setWindowStart( -getX(), -getY() );
        context.setWindowSize( windowWidth, windowHeight );
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension( slice.getWidth(), slice.getHeight() );
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
