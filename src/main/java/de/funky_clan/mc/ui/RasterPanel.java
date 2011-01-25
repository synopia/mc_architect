package de.funky_clan.mc.ui;

import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.RenderContext;
import de.funky_clan.mc.model.SelectedBlock;
import de.funky_clan.mc.model.Slice;

import javax.swing.*;
import java.awt.*;

/**
 * @author synopia
 */
public class RasterPanel extends JPanel {
    private Model model;
    private int sliceNo;
    private RenderContext context;
    private SelectedBlock selectedBlock;

    public RasterPanel(Model model) {
        this.model = model;
        this.sliceNo = 0;
        setPreferredSize(new Dimension(model.getWidth() * 2, model.getHeight() * 2));
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

    }

    private void initContext(Graphics2D g) {
        if (context == null) {
            context = new RenderContext(model);
        }
        context.setG(g);
        context.setStartPosition(0, 0);
        context.setSize(model.getWidth(), model.getHeight());
        context.setWindowSize(getWidth(), getHeight());
    }
}
