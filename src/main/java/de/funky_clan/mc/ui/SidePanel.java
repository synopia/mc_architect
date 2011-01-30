package de.funky_clan.mc.ui;

import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.model.*;

import java.awt.*;
import java.awt.Graphics;

/**
 * @author paul.fritsche@googlemail.com
 */
public class SidePanel extends ZoomPanel {
    private Model model;
    private RenderContext context;
    private SelectedBlock selectedBlock;
    private Player player;
    private int modelSideWidth; // either models height or width (the bigger one)
    private int modelSideHeight; // number of slices
    private int sliceNo;

    public SidePanel(Model model, Configuration.Colors colors ) {
        this.model = model;

        modelSideWidth = Math.max( model.getSizeX(), model.getSizeY() );
        modelSideHeight = model.getSizeZ();

        applyZoom((float)modelSideWidth/modelSideHeight);
        setFocusable(true);

        setAutoscrolls(true);
        player = new Player();
        context = new RenderContext(model);
        context.setColors(colors);
    }

    public void updatePlayerPos( int x, int y, int z, int angle ) {
        player.repaint(SidePanel.this, context);

        int wx = y;
        int wy = z;
        int wz = x;

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
            player.repaint(SidePanel.this, context);
        }
    }

    @Override
    public void applyZoom(double zoom) {
        Dimension newSize = new Dimension((int) (modelSideWidth * zoom), (int) (modelSideHeight * zoom));
        setPreferredSize(newSize);
        revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        initContext((Graphics2D) g);
        g.setColor( context.getColors().getBackgroundColor() );
        g.fillRect(0,0,getWidth(), getHeight());

        Slice slice = model.getXSlice(sliceNo);
        if (slice != null) {
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

        context.setScreenSize(screenWidth, screenHeight, modelSideWidth, modelSideHeight);
        context.setWindowStart(-getX(), -getY());
        context.setWindowSize(windowWidth, windowHeight);
    }


    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(modelSideWidth, modelSideHeight);
    }
}
