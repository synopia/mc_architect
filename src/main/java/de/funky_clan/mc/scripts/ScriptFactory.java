package de.funky_clan.mc.scripts;

import com.google.inject.Inject;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.events.script.LoadScript;
import de.funky_clan.mc.events.script.RunScript;
import de.funky_clan.mc.events.script.ScriptFinished;
import de.funky_clan.mc.events.script.ScriptLoaded;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.SliceType;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

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

                script.run();

                model.fireUpdates();
                eventDispatcher.fire(new ScriptFinished(script));
            }
        });
    }

}
