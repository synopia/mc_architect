package de.funky_clan.mc.config;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.file.RegionFileService;
import de.funky_clan.mc.model.Box;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.scripts.WorldGraphics;
import de.funky_clan.mc.ui.MainPanel;
import de.funky_clan.mc.ui.renderer.*;
import de.funky_clan.mc.util.Benchmark;

/**
 * @author synopia
 */
public class ArchitectModule implements Module {

    @Override
    public void configure(Binder binder) {

        binder.bind(BlockRenderer.class).in(Singleton.class);
        binder.bind(ImageRenderer.class).in(Singleton.class);
        binder.bind(PlayerRenderer.class).in(Singleton.class);
        binder.bind(SliceRenderer.class).in(Singleton.class);
        binder.bind(BoxRenderer.class).in(Singleton.class);

        binder.bind(Box.class).in(Singleton.class);
        binder.bind(Benchmark.class).in(Singleton.class);
        binder.bind(Colors.class).in(Singleton.class);
        binder.bind(WorldGraphics.class).in(Singleton.class);

        binder.bind(MainPanel.class).in(Singleton.class);
        binder.bind(RegionFileService.class).in(Singleton.class);
    }

}
