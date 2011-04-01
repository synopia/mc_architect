package de.funky_clan.mc.ui;

import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.station.stack.DefaultStackDockComponent;
import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.script.ScriptLoaded;
import de.funky_clan.mc.scripts.Script;

import javax.swing.JTextArea;
import java.awt.Font;
import java.util.HashMap;

/**
 * @author synopia
 */
public class ConsolePanel extends DefaultStackDockComponent {
    private HashMap<Script, JTextArea> tabs = new HashMap<Script, JTextArea>();

    @Inject
    public ConsolePanel( SwingEventBus eventBus ) {
        super();
        eventBus.registerCallback( ScriptLoaded.class, new EventHandler<ScriptLoaded>() {
            @Override
            public void handleEvent( ScriptLoaded event ) {
                Script    script = event.getScript();
                JTextArea textArea;

                if( tabs.containsKey( script )) {
                    textArea = tabs.get( script );
                } else {
                    textArea = new JTextArea( 10, 50 );
                    textArea.setFont( Font.getFont( "Monospaced" ));
                    addTab( script.getName(), null, textArea, new DefaultDockable( textArea ));
                    tabs.put( script, textArea );
                }

                String output = script.getOutput();

                if( output != null ) {
                    textArea.setText( output );
                }
            }
        } );
    }
}
