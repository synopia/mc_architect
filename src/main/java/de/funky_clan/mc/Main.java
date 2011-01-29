package de.funky_clan.mc;

import de.funky_clan.mc.config.Configuration;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.ui.MainPanel;
import de.funky_clan.mc.ui.RasterPanel;

import javax.swing.*;

/**
 * @author synopia
 */
public class Main extends JFrame {
    private MainPanel mainPanel;

    public Main(Configuration configuration) {
        mainPanel = new MainPanel(configuration);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(mainPanel);
        pack();
    }

    public void start() {
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Configuration conf = Configuration.createFromRuby("kolloseum.rb");
        Main main = new Main(conf);
        main.start();
    }
}
