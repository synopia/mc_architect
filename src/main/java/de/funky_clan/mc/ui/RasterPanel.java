package de.funky_clan.mc.ui;

import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.RenderContext;
import de.funky_clan.mc.model.SelectedBlock;
import de.funky_clan.mc.model.Slice;
import de.funky_clan.mc.net.ClientThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
        setPreferredSize(new Dimension(model.getWidth() * 2, model.getHeight() * 2));
        setAutoscrolls(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if( !e.isConsumed() ) {
                    if (selectedBlock == null) {
                        selectedBlock = new SelectedBlock();
                    }
                    selectedBlock.setX(context.pixelToWorldX(e.getX()));
                    selectedBlock.setY(context.pixelToWorldY(e.getY()));
                    repaint();
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
                repaint();
            }
        });
        context = new RenderContext(model);
        context.setStartPosition(0, 0);
        context.setSize(model.getWidth(), model.getHeight());

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
        context.setWindowSize(getWidth(), getHeight());
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
