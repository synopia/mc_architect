package de.funky_clan.mc.ui;

//~--- JDK imports ------------------------------------------------------------

import de.funky_clan.mc.model.RenderContext;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.*;

/**
 *
 * @author synopia
 */
public abstract class ZoomPanel extends JPanel {
    private double zoom = 1;
    Rectangle      lastZoomRect;
    private Point  start;
    Rectangle      zoomRect;
    protected RenderContext context = new RenderContext();

    public ZoomPanel() {
        context.init(0,0, getWidth(), getHeight(), getWidth(), getHeight());
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (start != null) {
                    zoomMouseDragged(e);
                }

                Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);

                scrollRectToVisible(r);
            }
        });
        addMouseListener( new MouseAdapter() {
            @Override
            public void mouseReleased( MouseEvent e ) {
                if( isMouseZooming( e )) {
                    zoomMouseReleased( e );
                }
            }
            @Override
            public void mousePressed( MouseEvent e ) {
                if( isMouseZooming( e )) {
                    zoomMousePressed( e );
                }
            }
        } );
        addMouseWheelListener( new MouseWheelListener() {
            @Override
            public void mouseWheelMoved( MouseWheelEvent e ) {
                double scale = 1 + e.getWheelRotation() * 0.05;

                if( zoom * scale > 1 ) {
                    zoom *= scale;
                    context.zoom( zoom, zoom );
                    repaint();  // todo
                }
            }
        } );
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        context.setScreenSize(width, height);
    }

    /**
     * Overwrite this method, to define when zooming.
     *
     * @param e current MouseEvent
     * @return true if the MouseEvent is a zooming event, false if not
     */
    public boolean isMouseZooming( MouseEvent e ) {
        return e.getButton() == MouseEvent.BUTTON1;
    }

    @Override
    protected void paintComponent( Graphics g ) {
        g.setXORMode( Color.WHITE );

        if( zoomRect != null ) {
            g.drawRect( zoomRect.x, zoomRect.y, zoomRect.width - 1, zoomRect.height - 1 );
        }
    }

    protected void repaintZoomRect( Rectangle rectangle ) {
        zoomRect = rectangle;

        Rectangle union = zoomRect;

        if( lastZoomRect != null ) {
            union = zoomRect.union( lastZoomRect );
        }

        repaint( union );
        lastZoomRect = zoomRect;
    }

    protected void zoomMousePressed( MouseEvent e ) {
        start = e.getPoint();
    }

    protected void zoomMouseReleased( MouseEvent e ) {
        zoomRect = null;

        Point end = e.getPoint();

        if( (start != null) && !start.equals( end )) {
            e.consume();

            applyWindow( start.x, start.y, end.x-start.x, end.y-start.y );
        }
    }

    public void applyWindow( int x, int y, int width, int height ) {
        double windowX = context.screenToModelX( x );
        double windowY = context.screenToModelY( y );
        double windowWidth = context.screenToModelX( width );
        double windowHeight = context.screenToModelY( height );

        context.init( windowX, windowY, windowWidth, windowHeight, getWidth(), getHeight() );

        repaint(); // todo

    }

    protected void zoomMouseDragged( MouseEvent e ) {
        e.consume();

        int width  = start.x - e.getX();
        int height = start.y - e.getY();
        int w      = Math.abs( width );
        int h      = Math.abs( height );
        int x      = (width < 0)
                     ? start.x
                     : e.getX();
        int y      = (height < 0)
                     ? start.y
                     : e.getY();

        repaintZoomRect( new Rectangle( x, y, w, h ));
    }
}
