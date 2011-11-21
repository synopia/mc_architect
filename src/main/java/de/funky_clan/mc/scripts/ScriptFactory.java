package de.funky_clan.mc.scripts;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.events.script.LoadScript;
import de.funky_clan.mc.events.script.RunScript;
import de.funky_clan.mc.events.script.ScriptFinished;
import de.funky_clan.mc.events.script.ScriptLoaded;
import de.funky_clan.mc.file.SchematicLoader;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.SliceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author synopia
 */
@Singleton
public class ScriptFactory {
    private final Logger  logger = LoggerFactory.getLogger( ScriptFactory.class );
    @Inject
    private Provider<RubyScript> rubyScriptProvider;
    @Inject
    private Provider<SchematicScript> schematicScriptProvider;
    @Inject
    private Model model;

    @Inject
    EventDispatcher eventDispatcher;

    @Inject
    public ScriptFactory( final ModelEventBus eventBus ) {
        eventBus.subscribe(LoadScript.class, new EventHandler<LoadScript>() {
            @Override
            public void handleEvent(LoadScript event) {
                Script script;

                if (!event.hasScript()) {
                    if (event.getFileName().endsWith(".rb")) {
                        script = rubyScriptProvider.get();
                        script.setFilename(event.getFileName(), event.isUseClasspath());
                    } else if( event.getFileName().endsWith(".schematic") ) {
                        script = schematicScriptProvider.get();
                        script.setFilename(event.getFileName(), event.isUseClasspath() );
                    } else {
                        return;
                    }
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
                Script script = event.getScript();
                logger.info("Running script " + script.getName());
                script.run();
                script.setChunkUpdates(model.getUpdates());
                eventDispatcher.publish(new ScriptFinished(script));
            }
        });
    }
}
