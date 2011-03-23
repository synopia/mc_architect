package de.funky_clan.mc.ui;

import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.swing.OreDisplayUpdate;
import de.funky_clan.mc.net.packets.ChunkData;
import de.funky_clan.mc.net.packets.ChunkPreparation;
import de.funky_clan.mc.util.Benchmark;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * @author synopia
 */
public class StatisticsToolbar extends JToolBar {
    private JLabel           chunksText;
    private int              chunksLoaded;
    private int              chunksUnloaded;
    private JLabel           memoryText;
    private JLabel           benchmarkText;
    private JLabel           oreText;
    @Inject
    private Benchmark benchmark;

    @Inject
    public StatisticsToolbar(SwingEventBus eventBus) {
        setAlignmentX(LEFT_ALIGNMENT);

        build();
        eventBus.registerCallback(ChunkData.class, new EventHandler<ChunkData>() {
            @Override
            public void handleEvent(ChunkData event) {
                chunksLoaded++;
            }
        });
        eventBus.registerCallback(ChunkPreparation.class, new EventHandler<ChunkPreparation>() {
            @Override
            public void handleEvent(ChunkPreparation event) {
                if( !event.isLoad() ) {
                    chunksUnloaded++;
                    chunksLoaded--;
                }
            }
        });

        eventBus.registerCallback(OreDisplayUpdate.class, new EventHandler<OreDisplayUpdate>() {
            @Override
            public void handleEvent(OreDisplayUpdate event) {
                oreText.setText("Ores: "+event.getOre().size());
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
        chunksText = new JLabel("Chunks:");
        add(chunksText);
        addSeparator();

        memoryText = new JLabel("Mem: 0/0");
        add(memoryText);
        addSeparator();

        benchmarkText = new JLabel("CPU/GFX: 0/0");
        add(benchmarkText);
        addSeparator();

        oreText = new JLabel("Ores: 0");
        add(oreText);
        addSeparator();

        addSeparator();
    }
    protected void updateStats() {
        chunksText.setText("Chunks: " + chunksLoaded + "/"+chunksUnloaded );
        long max = Runtime.getRuntime().maxMemory();
        long free = Runtime.getRuntime().freeMemory();
        memoryText.setText("Mem: " + ((max-free)/1024/1024) + "/" + (max/1024/1024) );

        HashMap<Object, Double> results = benchmark.getResults();
        double eventBusTime = 0;
//        if( results.containsKey(eventBus) ) {
//            eventBusTime = results.get(eventBus);
//        }
        double renderTime = 0;
        if( results.containsKey(ZoomPanel.class) ) {
            renderTime = results.get(ZoomPanel.class);
        }

        benchmarkText.setText(String.format("CPU/GFX: %.0f/%.0f", eventBusTime * 100, renderTime * 100));
    }

}
