package de.funky_clan.mc.scripts;

import com.google.inject.Inject;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.events.script.*;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.SliceType;
import de.funky_clan.mc.net.packets.BlockMultiUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author synopia
 */
public class ScriptFactory {
    @Inject private SliceGraphics sliceGraphicsX;
    @Inject private SliceGraphics sliceGraphicsY;
    @Inject private SliceGraphics sliceGraphicsZ;
    @Inject private WorldGraphics worldGraphics;
    @Inject private Model model;
    @Inject
    BinvoxLoader binvoxLoader;
    @Inject
    EventDispatcher eventDispatcher;
    private final Logger logger = LoggerFactory.getLogger(ScriptFactory.class);


    @Inject
    public ScriptFactory(final ModelEventBus eventBus) {
        eventBus.registerCallback(LoadScript.class, new EventHandler<LoadScript>() {
            @Override
            public void handleEvent(LoadScript event) {
                Script script;
                if( !event.hasScript() ) {
                    if( !event.getFileName().endsWith(".rb") ) {
                        return;
                    }
                    script = new Script(event.getFileName(), event.isUseClasspath());
                } else {
                    script = event.getScript();
                }
                logger.info("Loading script " + event.getFileName());
                script.load();
                eventDispatcher.fire(new ScriptLoaded(script) );
            }
        });

        eventBus.registerCallback(RunScript.class, new EventHandler<RunScript>() {
            @Override
            public void handleEvent(RunScript event) {
                sliceGraphicsX.setSliceType(SliceType.X);
                sliceGraphicsY.setSliceType(SliceType.Y);
                sliceGraphicsZ.setSliceType(SliceType.Z);

                Script script = event.getScript();

                script.put("@slice_x", sliceGraphicsX);
                script.put("@slice_y", sliceGraphicsY);
                script.put("@slice_z", sliceGraphicsZ);
                script.put("@world", worldGraphics);
                script.put("@binvox", binvoxLoader);
                script.put("@model", model);

                logger.info("Running script "+script.getName());
                script.run();

                script.setChunkUpdates(new HashMap<Long, BlockMultiUpdate>(model.getUpdates()));
                model.getUpdates().clear();

                eventDispatcher.fire(new ScriptFinished(script));
            }
        });

        eventBus.registerCallback(SendScriptData.class, new EventHandler<SendScriptData>() {
            @Override
            public void handleEvent(SendScriptData event) {
                Script script = event.getScript();
                HashMap<Long,BlockMultiUpdate> chunkUpdates = script.getChunkUpdates();
                for (BlockMultiUpdate update : chunkUpdates.values()) {
                    eventDispatcher.fire(update);
                }
                script.setSent( true );
            }
        });
    }

}
