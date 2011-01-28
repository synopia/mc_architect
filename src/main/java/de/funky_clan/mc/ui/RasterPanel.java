package de.funky_clan.mc.ui;

import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.RenderContext;
import de.funky_clan.mc.model.SelectedBlock;
import de.funky_clan.mc.model.Slice;
import de.funky_clan.mc.net.ClientThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author synopia
 */
public class RasterPanel extends ZoomPanel implements Scrollable {
    private Model model;
    private int sliceNo;
    private RenderContext context;
    private SelectedBlock selectedBlock;
    private SelectedBlock clientBlock;
    private final ClientThread clientThread;

    public RasterPanel(final Model model) {
        this.model = model;
        this.sliceNo = 0;
        setFocusable(true);
        setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
        setAutoscrolls(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if( !e.isConsumed() ) {
                    if (selectedBlock == null) {
                        selectedBlock = new SelectedBlock();
                    } else {
                        selectedBlock.repaint(RasterPanel.this, context);
                    }
                    int x = context.pixelToWorldX(e.getX());
                    int y = context.pixelToWorldY(e.getY());
                    selectedBlock.setX(x);
                    selectedBlock.setY(y);
                    selectedBlock.repaint(RasterPanel.this, context);
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
                scrollRectToVisible(r);
            }
        });

        clientBlock = new SelectedBlock();
        clientThread = new ClientThread();
        clientThread.start();
        boolean connected = clientThread.connect("localhost", 12345, new ClientThread.DataListener() {
            @Override
            public void onPlayerPosition(int x, int y, int z) {
                clientBlock.setX(x);
                clientBlock.setY(y);
                sliceNo = z;
                Rectangle rect = new Rectangle(
                        context.worldToPixelX(x), context.worldToPixelY(y),
                        context.worldToPixelX(1), context.worldToPixelY(1)
                );
                scrollRectToVisible(rect);
                clientBlock.repaint(RasterPanel.this, context);
            }
        });
        context = new RenderContext(model);
    }

    @Override
    public void applyZoom(double zoom) {
        Dimension newSize = new Dimension((int) (model.getWidth() * zoom), (int) (model.getHeight() * zoom));
        setPreferredSize(newSize);
        revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        initContext((Graphics2D) g);

        Slice slice = model.getSlice(sliceNo);
        if (slice != null) {
            slice.render(context);
        }

        if (selectedBlock != null) {
            selectedBlock.render(context);
        }
        if (clientBlock != null) {
            clientBlock.render(context);
        }
        super.paintComponent(g);

    }

    private void initContext(Graphics2D g) {
        context.setGraphics(g);
        int windowWidth  = getParent().getWidth();
        int windowHeight = getParent().getHeight();
        int screenWidth  = getWidth();
        int screenHeight = getHeight();

        context.setScreenSize(screenWidth, screenHeight);
        context.setWindowStart(-getX(), -getY());
        context.setWindowSize(windowWidth, windowHeight);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(model.getWidth(), model.getHeight());
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 1;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 1;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
