package de.funky_clan.mc.scripts;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.events.script.LoadScript;
import de.funky_clan.mc.events.script.RunScript;
import de.funky_clan.mc.events.script.ScriptFinished;
import de.funky_clan.mc.events.script.ScriptLoaded;
import de.funky_clan.mc.events.script.SendScriptData;
import de.funky_clan.mc.file.SchematicLoader;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.SliceType;
import de.funky_clan.mc.net.packets.BlockMultiUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author synopia
 */
@Singleton
public class ScriptFactory {
    private final Logger  logger = LoggerFactory.getLogger( ScriptFactory.class );
    @Inject
    BinvoxLoader          binvoxLoader;
    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    private Model         model;
    @Inject
    private SchematicLoader schematicLoader;
    @Inject
    private SliceGraphics sliceGraphicsX;
    @Inject
    private SliceGraphics sliceGraphicsY;
    @Inject
    private SliceGraphics sliceGraphicsZ;
    @Inject
    private WorldGraphics worldGraphics;

    @Inject
    public ScriptFactory( final ModelEventBus eventBus ) {
        eventBus.subscribe(LoadScript.class, new EventHandler<LoadScript>() {
            @Override
            public void handleEvent(LoadScript event) {
                Script script;

                if (!event.hasScript()) {
                    if (!event.getFileName().endsWith(".rb")) {
                        return;
                    }

                    script = new Script(event.getFileName(), event.isUseClasspath());
                } else {
                    script = event.getScript();
                }

                logger.info("Loading script " + event.getFileName());
                script.load();
                eventDispatcher.publish(new ScriptLoaded(script));
            }
        });
        eventBus.subscribe(RunScript.class, new EventHandler<RunScript>() {
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
                script.put("@schematic", schematicLoader);
                script.put("@model", model);
                logger.info("Running script " + script.getName());
                script.run();
                script.setChunkUpdates(model.getUpdates());
                eventDispatcher.publish(new ScriptFinished(script));
            }
        });
    }
}
