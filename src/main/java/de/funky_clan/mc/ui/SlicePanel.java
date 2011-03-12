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
    private BackgroundImage image;
    private Model           model;
    private Player          player;
    private SelectedBlock   selectedBlock;
    private Slice           slice;
    private int             sliceNo;

    public SlicePanel( Model model, Configuration.Colors colors, final Slice slice ) {
        this.model = model;
        this.slice = slice;
        setFocusable(true);
        setAutoscrolls( true );
        player  = new Player( slice.getType() == SliceType.Z );
        context.setColors( colors );
        context.setWindowSize(50,50);
        addMouseListener( new MouseAdapter() {
            @Override
            public void mouseReleased( MouseEvent e ) {
                if( !e.isConsumed() && e.getButton()==MouseEvent.BUTTON1 ) {
                    if( selectedBlock == null ) {
                        selectedBlock = new SelectedBlock();
                    } else {
                        selectedBlock.repaint( SlicePanel.this, context );
                    }

                    int x = (int)context.screenToModelX(context.getScreenWidth()-e.getX());
                    int y = (int)context.screenToModelY(context.getScreenHeight()-e.getY());

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
        double windowX = context.getWindowX();
        double windowY = context.getWindowY();
        double windowWidth = context.getWindowWidth();
        double windowHeight = context.getWindowHeight();

        context.init( wx-windowWidth/2, wy-windowHeight/2, windowWidth, windowHeight, getWidth(), getHeight() );
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
