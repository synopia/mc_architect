package de.funky_clan.mc;

import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.ui.RasterPanel;

import javax.swing.*;

/**
 * @author synopia
 */
public class Main extends JFrame {
    private Model model;
    private RasterPanel rasterPanel;

    public Main(Model model) {
        this.model = model;
        rasterPanel = new RasterPanel(model);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane scrollPane = new JScrollPane(rasterPanel);
        add(scrollPane);
        pack();

    }

    public void start() {
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Configuration conf = new Configuration();
        conf.create(187, 155, 42)
                .drawEllipse(92, 76, 92, 42)
                .image("level1_small.png")
                .axis(92, 76);
        ;

        Main main = new Main(conf.getModel());
        main.start();
    }
}
