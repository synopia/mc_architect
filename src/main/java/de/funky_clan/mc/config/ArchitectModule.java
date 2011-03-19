package de.funky_clan.mc.config;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.file.RegionFileService;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.ui.MainPanel;
import de.funky_clan.mc.ui.renderer.BlockRenderer;
import de.funky_clan.mc.ui.renderer.ImageRenderer;
import de.funky_clan.mc.ui.renderer.PlayerRenderer;
import de.funky_clan.mc.ui.renderer.SliceRenderer;
import de.funky_clan.mc.util.Benchmark;

/**
 * @author synopia
 */
public class ArchitectModule implements Module {
    private Model model;
    private MainPanel mainPanel;

    @Override
    public void configure(Binder binder) {
        binder.bind(EventBus.class).in(Singleton.class);
        binder.bind(BlockRenderer.class).in(Singleton.class);
        binder.bind(ImageRenderer.class).in(Singleton.class);
        binder.bind(PlayerRenderer.class).in(Singleton.class);
        binder.bind(SliceRenderer.class).in(Singleton.class);
        binder.bind(Benchmark.class).in(Singleton.class);
        binder.bind(Colors.class).in(Singleton.class);

        binder.bind(Model.class).in(Singleton.class);
        binder.bind(MainPanel.class).in(Singleton.class);
        binder.bind(RegionFileService.class).in(Singleton.class);
    }

}
