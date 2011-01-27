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
public class RasterPanel extends JPanel implements Scrollable {
    private Model model;
    private int sliceNo;
    private RenderContext context;
    private SelectedBlock selectedBlock;
    private SelectedBlock clientBlock;
    private final ClientThread clientThread;

    public RasterPanel(Model model) {
        this.model = model;
        this.sliceNo = 0;
        setFocusable(true);
        setPreferredSize(new Dimension(model.getWidth() * 2, model.getHeight() * 2));
        setAutoscrolls(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedBlock == null) {
                    selectedBlock = new SelectedBlock();
                }
                selectedBlock.setX(context.pixelToWorldX(e.getX()));
                selectedBlock.setY(context.pixelToWorldY(e.getY()));
                repaint();
            }

        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
                scrollRectToVisible(r);
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Dimension size = getSize();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        setPreferredSize(new Dimension(size.width + 5, size.height + 5));
                        revalidate();
                        break;
                    case KeyEvent.VK_DOWN:
                        setPreferredSize(new Dimension(size.width - 5, size.height - 5));
                        revalidate();
                        break;
                }

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
                repaint();
            }
        });
        context = new RenderContext(model);
        context.setStartPosition(0, 0);
        context.setSize(model.getWidth(), model.getHeight());

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
