package de.funky_clan.mc.ui.widgets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.swing.OreDisplayUpdate;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.net.packets.ChunkData;
import de.funky_clan.mc.net.packets.ChunkPreparation;
import de.funky_clan.mc.util.Benchmark;
import de.funky_clan.mc.util.StatusBar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;

/**
 * @author synopia
 */
@Singleton
public class StatisticWidgetFactory {
    private JLabel           chunksText;
    private int              chunksLoaded;
    private JLabel           memoryText;
    private JLabel           benchmarkText;
    private JLabel           oreText;
    @Inject
    private Benchmark benchmark;
    @Inject
    private  Model model ;

    @Inject
    public StatisticWidgetFactory(SwingEventBus eventBus) {

        build();
        eventBus.registerCallback(ChunkData.class, new EventHandler<ChunkData>() {
            @Override
            public void handleEvent(ChunkData event) {
                chunksLoaded++;
            }
        });

        eventBus.registerCallback(OreDisplayUpdate.class, new EventHandler<OreDisplayUpdate>() {
            @Override
            public void handleEvent(OreDisplayUpdate event) {
                oreText.setText("Ores: "+event.getTotal());
            }
        });
        new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStats();
            }
        }).start();
    }
    
    private void build() {
        chunksText = new JLabel("Chunks: 1411/20000");
        memoryText = new JLabel("Mem: 500/1000");
        benchmarkText = new JLabel("CPU/GFX/NET: 100/100/100");
        oreText = new JLabel("Ores:           0");
    }

    protected void updateStats() {
        chunksText.setText("Chunks: " + model.getNumberOfChunks() + "/"+chunksLoaded );
        long max = Runtime.getRuntime().maxMemory();
        long free = Runtime.getRuntime().freeMemory();
        memoryText.setText("Mem: " + ((max-free)/1024/1024) + "/" + (max/1024/1024) );

        HashMap<String, Double> results = benchmark.getResults();
        if( results!=null ) {
            double networkBusTime = 0;
            if( results.containsKey("net") ) {
                networkBusTime = results.get("net");
            }
            double eventBusTime = 0;
            if( results.containsKey("bus") ) {
                eventBusTime = results.get("bus");
            }
            double renderTime = 0;
            if( results.containsKey("gfx") ) {
                renderTime = results.get("gfx");
            }

            benchmarkText.setText(String.format("CPU/GFX/NET: %.0f/%.0f/%.0f", eventBusTime * 100, renderTime * 100, networkBusTime*100));
        }
    }

    public JLabel getChunksText() {
        return chunksText;
    }

    public JLabel getMemoryText() {
        return memoryText;
    }

    public JLabel getBenchmarkText() {
        return benchmarkText;
    }

    public JLabel getOreText() {
        return oreText;
    }
}
