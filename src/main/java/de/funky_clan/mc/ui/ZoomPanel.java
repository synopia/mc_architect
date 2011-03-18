package de.funky_clan.mc.ui;

//~--- JDK imports ------------------------------------------------------------

import de.funky_clan.mc.math.Point2d;
import de.funky_clan.mc.math.Point2i;
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
    Rectangle      lastZoomRect;
    private Point2i start;
    Rectangle      zoomRect;
    protected RenderContext context;

    protected abstract RenderContext createRenderContext();

    public ZoomPanel() {
    }

    public void init() {
        context = createRenderContext();
        onInit();
    }

    public void onInit() {
        context.init(new Point2d(0,0), new Point2d(getWidth(), getHeight()), new Point2i(getWidth(), getHeight()));
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if( start == null ) {
                    return;
                }
                if (isMouseZooming(e) ) {
                    zoomMouseDragged(e);
                } else if( isMouseDragging(e) ) {
                    dragTo( e );
                }
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
                if( isMouseZooming( e ) || isMouseDragging(e) ) {
                    start = new Point2i(e.getX(), e.getY());
                }
            }
        } );
        addMouseWheelListener( new MouseWheelListener() {
            @Override
            public void mouseWheelMoved( MouseWheelEvent e ) {
                double scale = 1 + e.getWheelRotation() * 0.05;
                context.zoom(scale, scale);
                repaint();
            }
        } );
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        context.setScreenSize(new Point2i(width, height));
    }

    /**
     * Overwrite this method, to define when zooming.
     *
     * @param e current MouseEvent
     * @return true if the MouseEvent is a zooming event, false if not
     */
    public boolean isMouseZooming( MouseEvent e ) {
        return (e.getModifiers()&MouseEvent.BUTTON1_MASK) != 0;
    }
    public boolean isMouseDragging( MouseEvent e ) {
        return (e.getModifiers()&MouseEvent.BUTTON3_MASK) != 0;
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

    protected void zoomMouseReleased( MouseEvent e ) {
        zoomRect = null;

        Point end = e.getPoint();

        if( (start != null) && !start.equals( end )) {
            e.consume();

            applyWindow( start.x(), start.y(), end.x-start.x(), end.y-start.y());
        }
    }

    protected void dragTo( MouseEvent e ) {
        Point2d d = context.screenToWorld(start.sub(new Point2i(e.getX(),e.getY()))).add(context.getWindowPosition());
        context.setWindowPosition(d);

        repaint();
        start = new Point2i(e.getX(), e.getY());
    }


    public void applyWindow( int x, int y, int width, int height ) {
        if( width<10 || height<10 ) {
            return;
        }
        Point2d windowPos = context.screenToWorld(new Point2i(x, y));
        Point2d windowSize = context.screenToWorld(new Point2i(x+width,y+height)).sub(windowPos);

        context.init( windowPos, windowSize, new Point2i(getWidth(), getHeight()) );

        repaint();

    }

    protected void zoomMouseDragged( MouseEvent e ) {
        e.consume();

        int width  = start.x() - e.getX();
        int height = start.y() - e.getY();
        int w      = Math.abs( width );
        int h      = Math.abs( height );
        int x      = (width < 0)
                ? start.x()
                : e.getX();
        int y      = (height < 0)
                ? start.y()
                : e.getY();

        repaintZoomRect( new Rectangle( x, y, w, h ));
    }

    protected void scrollTo(Point2d slicePos) {
        Point2d start = slicePos.addScaled( -.5, context.getWindowSize());
        context.init( start, context.getWindowSize(), context.getScreenSize() );
        repaint();
    }
}
