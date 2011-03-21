package de.funky_clan.mc.ui;

//~--- JDK imports ------------------------------------------------------------

import com.google.inject.Inject;
import de.funky_clan.mc.math.Point2i;
import de.funky_clan.mc.model.RenderContext;
import de.funky_clan.mc.util.Benchmark;

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
    private Rectangle      lastZoomRect;
    private Point start;
    private Rectangle      zoomRect;
    protected RenderContext context;
    @Inject
    private Benchmark benchmark;

    protected abstract RenderContext createRenderContext();

    public ZoomPanel() {

    }

    public void init() {
        context = createRenderContext();
        context.init(0,0,getWidth(), getHeight(), getWidth(), getHeight());
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
                    context.zoom(scale, scale, e.getPoint());
                    repaint();
                }
            }
        } );
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        context.setScreenSize(width, height);
        context.calculateSizes();
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
        benchmark.startBenchmark(ZoomPanel.class);
        Graphics2D graphics2D = (Graphics2D) g;

        initContext(graphics2D);

        paintContent(graphics2D);

        graphics2D.setXORMode( Color.WHITE );
        if( zoomRect != null ) {
            graphics2D.drawRect(zoomRect.x, zoomRect.y, zoomRect.width - 1, zoomRect.height - 1);
        }
        benchmark.endBenchmark(ZoomPanel.class);
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

            applyWindow( start.x, start.y, end.x-start.x, end.y-start.y );
        }
    }

    protected void dragTo( MouseEvent e ) {
        double windowPosX = context.screenToSliceX(start.x)-context.screenToSliceX(e.getX()) + context.getWindowPositionX();
        double windowPosY = context.screenToSliceY(start.y)-context.screenToSliceY(e.getY()) + context.getWindowPositionY();

        context.setWindowPosition(windowPosX, windowPosY);

        repaint();
        start = e.getPoint();
    }


    public void applyWindow( int startX, int startY, int width, int height ) {
        int x = (width>0)  ? startX : startX+width;
        int y = (height>0) ? startY : startY+height;
        int w = Math.abs(width);
        int h = Math.abs(height);

        if( x<10 || y<10 ) {
            return;
        }

        double windowPositionX = context.screenToSliceX(x);
        double windowPositionY = context.screenToSliceY(y);
        context.init(windowPositionX, windowPositionY, context.screenToSliceX(x+w)-windowPositionX, context.screenToSliceY(y+h)-windowPositionY, getWidth(), getHeight());

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

    protected void scrollTo(double x, double y) {
        double startX = x - context.getWindowSizeX()/2;
        double startY = y - context.getWindowSizeY()/2;
        context.init( startX, startY, context.getWindowSizeX(), context.getWindowSizeY(), context.getScreenSizeX(), context.getScreenSizeY() );
        repaint();
    }
}
