package de.funky_clan.mc.ui;

import com.google.inject.Inject;
import de.funky_clan.mc.Main;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.SwingEventBus;
import de.funky_clan.mc.events.model.RunScript;
import de.funky_clan.mc.events.swing.ScriptFinished;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @author synopia
 */
public class ScriptsPanel extends JPanel {

    @Inject
    private EventDispatcher eventDispatcher;

    private JButton load;
    private JFileChooser fileChooser;

    @Inject
    public ScriptsPanel(SwingEventBus eventBus) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        eventBus.registerCallback(ScriptFinished.class, new EventHandler<ScriptFinished>() {
            @Override
            public void handleEvent(ScriptFinished event) {

            }
        });

        build();
        addScriptButton("kolloseum.rb", !Main.isDebug());
        addScriptButton("akw.rb", !Main.isDebug());
        addScriptButton("sphere.rb", !Main.isDebug());
        addScriptButton("glaskugel.rb", !Main.isDebug());
        if( Main.isDebug() ) {
            addScriptButton("test.rb", false);
        }
    }

    protected void build() {
        fileChooser = new JFileChooser();
        load = new JButton(new AbstractAction("Load Script") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(ScriptsPanel.this);
                if( result == JFileChooser.APPROVE_OPTION ) {
                    File file = fileChooser.getSelectedFile();
                    if( file.isFile() && file.exists() ) {
                        addScriptButton( file.getAbsolutePath() );
                    }
                }
            }
        });

        add( load );
    }

    protected void addScriptButton( final String filename) {
        addScriptButton(filename, false);
    }
    protected void addScriptButton( final String filename, final boolean useClasspath) {

        String name = filename.replace('\\', '/');
        name = filename.substring( name.lastIndexOf('/')+1, filename.lastIndexOf('.'));
        add(new JButton(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDispatcher.fire(new RunScript(filename, useClasspath) );
            }
        }));
        revalidate();
    }
}
