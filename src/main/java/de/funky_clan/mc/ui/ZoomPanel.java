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
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.*;

/**
 *
 * @author synopia
 */
public abstract class ZoomPanel extends JPanel {
    private Rectangle      lastZoomRect;
    private Point start;
    private Rectangle      zoomRect;
    protected RenderContext context;

    protected abstract RenderContext createRenderContext();

    public ZoomPanel() {

    }

    public void init() {
        context = createRenderContext();
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
                    start = e.getPoint();
                }
            }
        } );
        addMouseWheelListener( new MouseWheelListener() {
            @Override
            public void mouseWheelMoved( MouseWheelEvent e ) {
                if( e.isShiftDown() ) {
                    double scale = 1 + e.getWheelRotation() * 0.05;
                    Point2i delta = new Point2i(e.getX(), e.getY());
                    context.zoom(scale, scale, delta);
                    repaint();
                }
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
        Graphics2D graphics2D = (Graphics2D) g;

        initContext(graphics2D);

        paintContent(graphics2D);

        graphics2D.setXORMode( Color.WHITE );
        if( zoomRect != null ) {
            graphics2D.drawRect( zoomRect.x, zoomRect.y, zoomRect.width - 1, zoomRect.height - 1 );
        }
    }

    protected abstract void paintContent(Graphics2D graphics2D);

    protected void initContext( Graphics2D g ) {
        context.setGraphics(g);
    }

    protected void repaintZoomRect( Rectangle rectangle ) {
        zoomRect = rectangle;

        Rectangle union = zoomRect;

        if( lastZoomRect != null ) {
            union = zoomRect.union( lastZoomRect );
        }
        repaint(union);
        lastZoomRect = zoomRect;
    }

    protected void zoomMouseReleased( MouseEvent e ) {
        zoomRect = null;

        Point end = e.getPoint();

        if( (start != null) && !start.equals( end )) {
            e.consume();

            applyWindow( new Point2i(start), new Point2i(end).sub(new Point2i(start)) );
        }
    }

    protected void dragTo( MouseEvent e ) {
        Point2d d = context.screenToSlice(new Point2i(start)).
                sub(context.screenToSlice(new Point2i(e.getPoint()))).
                add(context.getWindowPosition());
        context.setWindowPosition(d);

        repaint();
        start = e.getPoint();
    }


    public void applyWindow( Point2i start, Point2i size ) {
        int x = (size.x()>0) ? start.x() : start.x()+size.x();
        int y = (size.y()>0) ? start.y() : start.y()+size.y();
        int w = Math.abs(size.x());
        int h = Math.abs(size.y());

        if( x<10 || y<10 ) {
            return;
        }
        Point2i realStart = new Point2i(x, y);
        Point2d windowPos = context.screenToSlice(realStart);
        Point2d windowSize = context.screenToSlice(realStart.add(new Point2i(w, h))).sub(windowPos);

        context.init( windowPos, windowSize, new Point2i(getWidth(), getHeight()) );

        repaint();

    }

    protected void zoomMouseDragged( MouseEvent e ) {
        e.consume();

        Point mousePos = e.getPoint();
        Point start    = this.start;
        int width  = mousePos.x - start.x;
        int height = mousePos.y - start.y;
        int w      = Math.abs( width );
        int h      = Math.abs( height );
        int x      = (width > 0)
                ? start.x
                : mousePos.x;
        int y      = (height > 0)
                ? start.y
                : mousePos.y;

        repaintZoomRect( new Rectangle( x, y, w, h ));
    }

    protected void scrollTo(Point2d slicePos) {
        Point2d start = slicePos.addScaled( -.5, context.getWindowSize());
        context.init( start, context.getWindowSize(), context.getScreenSize() );
        repaint();
    }
}
