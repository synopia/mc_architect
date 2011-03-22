package de.funky_clan.mc.scripts;

import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.ScriptFinished;
import de.funky_clan.mc.events.RunScript;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.SliceType;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

/**
 * @author synopia
 */
public class ScriptFactory {
    private EventBus eventBus;

    @Inject private SliceGraphics sliceGraphicsX;
    @Inject private SliceGraphics sliceGraphicsY;
    @Inject private SliceGraphics sliceGraphicsZ;
    @Inject private WorldGraphics worldGraphics;
    @Inject private Model model;
    @Inject
    BinvoxLoader binvoxLoader;


    @Inject
    public ScriptFactory(final EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.registerCallback(RunScript.class, new EventHandler<RunScript>() {
            @Override
            public void handleEvent(RunScript event) {
                if( !event.getFileName().endsWith(".rb") ) {
                    return;
                }
                sliceGraphicsX.setSliceType(SliceType.X);
                sliceGraphicsY.setSliceType(SliceType.Y);
                sliceGraphicsZ.setSliceType(SliceType.Z);

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
                eventBus.fireEvent(new ScriptFinished(event.getFileName()));
            }
        });
    }
}
