package de.funky_clan.mc.events.swing;

import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.eventbus.SwingEvent;

/**
 * @author synopia
 */
public class ScriptFinished implements SwingEvent {
    private String fileName;

    public ScriptFinished(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
