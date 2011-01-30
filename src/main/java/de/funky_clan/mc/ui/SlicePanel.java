package de.funky_clan.mc.ui;

import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.model.*;

import java.awt.*;
import java.awt.Graphics;

/**
 * @author paul.fritsche@googlemail.com
 */
public class SlicePanel extends ZoomPanel {
    private Model model;
    private RenderContext context;
    private SelectedBlock selectedBlock;
    private Player player;
    private int sliceNo;
    private Slice slice;

    public SlicePanel(Model model, Configuration.Colors colors, Slice slice) {
        this.model = model;
        this.slice = slice;

        setZoom((float) slice.getWidth() / slice.getHeight() );
        setFocusable(true);

        setAutoscrolls(true);
        player = new Player();
        context = new RenderContext(model);
        context.setColors(colors);
    }

    public void updatePlayerPos( int x, int y, int z, int angle ) {
        player.repaint(SlicePanel.this, context);

        int wx = y;
        int wy = model.getSizeZ()-z;
        int wz = x;

        switch (slice.getType()) {
            case X:
                wx = y;
                wy = model.getSizeZ()-z;
                wz = x;
                break;
            case Y:
                wx = x;
                wy = model.getSizeZ()-z;
                wz = y;
                break;
            case Z:
                wx = x;
                wy = y;
                wz = z;
                break;
        }

        player.setX(wx);
        player.setY(wy);
        player.setZ(wz);

        player.setDirection(angle);

        if( sliceNo!=wz ) {
            sliceNo = wz;
            repaint();
        } else {
            sliceNo = wz;
            Rectangle rect = new Rectangle(
                    context.worldToPixelX(wx-context.getWidth()/2), context.worldToPixelY(wy-context.getHeight()/2),
                    context.worldToPixelX(context.getWidth()), context.worldToPixelY(context.getHeight())
            );
            scrollRectToVisible(rect);
            player.repaint(SlicePanel.this, context);
        }
    }

    @Override
    public void applyZoom(double zoom) {
        Dimension newSize = new Dimension((int) (slice.getWidth() * zoom), (int) (slice.getHeight() * zoom));
        setPreferredSize(newSize);
        revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        initContext((Graphics2D) g);
        g.setColor( context.getColors().getBackgroundColor() );
        g.fillRect(0,0,getWidth(), getHeight());

        if (slice != null) {
            slice.setSlice( sliceNo );
            slice.render(context);
        }

        if (selectedBlock != null) {
            selectedBlock.render(context);
        }
        if (player != null) {
            player.render(context);
        }
        super.paintComponent(g);

    }

    private void initContext(Graphics2D g) {
        context.setGraphics(g);
        int windowWidth  = getParent().getWidth();
        int windowHeight = getParent().getHeight();
        int screenWidth  = getWidth();
        int screenHeight = getHeight();

        context.setScreenSize(screenWidth, screenHeight, slice.getWidth(), slice.getHeight() );
        context.setWindowStart(-getX(), -getY());
        context.setWindowSize(windowWidth, windowHeight);
    }


    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(slice.getWidth(), slice.getHeight());
    }
}
