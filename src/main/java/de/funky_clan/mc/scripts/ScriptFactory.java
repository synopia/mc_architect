package de.funky_clan.mc.scripts;

import com.google.inject.Inject;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.events.swing.ScriptFinished;
import de.funky_clan.mc.events.model.RunScript;
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
        eventBus.registerCallback(RunScript.class, new EventHandler<RunScript>() {
            @Override
            public void handleEvent(RunScript event) {
                if( !event.getFileName().endsWith(".rb") ) {
                    return;
                }
                sliceGraphicsX.setSliceType(SliceType.X);
                sliceGraphicsY.setSliceType(SliceType.Z);
                sliceGraphicsZ.setSliceType(SliceType.Y);

                ScriptingContainer container     = new ScriptingContainer();
                container.put( "@slice_x", sliceGraphicsX);
                container.put( "@slice_y", sliceGraphicsY);
                container.put( "@slice_z", sliceGraphicsZ);
                container.put( "@world", worldGraphics );
                container.put( "@binvox", binvoxLoader );
                container.put( "@model", model );
                if( event.isUseClasspath() ) {
                    container.runScriptlet(PathType.CLASSPATH, event.getFileName() );
                } else {
                    container.runScriptlet(PathType.ABSOLUTE, event.getFileName() );
                }
                model.fireUpdates();
                eventDispatcher.fire(new ScriptFinished(event.getFileName()));
            }
        });
    }
}
