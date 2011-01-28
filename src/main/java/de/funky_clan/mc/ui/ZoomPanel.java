package de.funky_clan.mc.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author synopia
 */
public abstract class ZoomPanel extends JPanel {
    private Point start;
    private double zoom = 1;
    Rectangle zoomRect;
    Rectangle lastZoomRect;

    public ZoomPanel() {
        addMouseMotionListener( new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if( start!=null ) {
                    zoomMouseDragged(e);
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if( isMouseZooming(e) ) {
                    zoomMouseReleased( e );
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if( isMouseZooming(e) ) {
                    zoomMousePressed(e);
                }
            }
        });

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double scale = 1 + e.getWheelRotation() * 0.05;
                if( zoom*scale>1 ) {
                    zoom *= scale;
                    applyZoom( zoom );
                }
            }
        });
    }

    /**
     * This method is called whenever zoom factor changes. Implementations may resize and invalidate itself here.
     *
     * @param zoom factor, that should be zoom to (absolute). "1" is original size
     */
    public abstract void applyZoom(double zoom);

    /**
     * Overwrite this method, to define when zooming.
     * Notice: Default impementation consumes zooming MouseEvents!
     *
     * @param e current MouseEvent
     * @return true if the MouseEvent is a zooming event, false if not
     */
    public boolean isMouseZooming( MouseEvent e ) {
        boolean result = e.getButton() == MouseEvent.BUTTON1;
        if( result ) {
            e.consume();
        }
        return result;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setXORMode(Color.WHITE);
        if( zoomRect!=null ) {
            g.drawRect( zoomRect.x, zoomRect.y, zoomRect.width-1, zoomRect.height-1 );
        }
    }

    protected void repaintZoomRect(Rectangle rectangle) {
        zoomRect        = rectangle;
        Rectangle union = zoomRect;

        if( lastZoomRect!=null ) {
            union = zoomRect.union(lastZoomRect);
        }
        repaint( union );
        lastZoomRect = zoomRect;
    }

    protected void zoomMousePressed(MouseEvent e) {
        start = e.getPoint();
    }

    protected void zoomMouseReleased(MouseEvent e) {
        zoomRect = null;
        Point end = e.getPoint();
        if( start!=null && !start.equals(end) ) {
            double selectedWidth  = Math.abs( end.getX() - start.getX() );
            double selectedHeight = Math.abs( end.getY() - start.getY() );
            if( selectedHeight<5 || selectedWidth<5 ) {
                return;
            }

            double scale;
            if( getParent().getWidth()>getParent().getHeight() ) {
                scale = getParent().getWidth() / selectedWidth;
            } else {
                scale = getParent().getHeight() / selectedHeight;
            }
            zoom *= scale;

            applyZoom(zoom);

            final int x = ((int)(start.x*scale));
            final int y = ((int)(start.y*scale));

            setPosition(x, y);
        }
    }

    protected void setPosition(final int x, final int y) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ((JViewport) getParent()).setViewPosition(new Point(x, y));
            }
        });
    }

    protected void zoomMouseDragged(MouseEvent e) {
        int width  = start.x - e.getX();
        int height = start.y - e.getY();
        int w = Math.abs(width);
        int h = Math.abs(height);
        int x = width < 0 ? start.x : e.getX();
        int y = height < 0 ? start.y : e.getY();

        repaintZoomRect(new Rectangle(x, y, w, h));
    }
}
