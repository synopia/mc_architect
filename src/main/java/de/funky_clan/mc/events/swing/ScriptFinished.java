package de.funky_clan.mc.events.swing;

import de.funky_clan.mc.eventbus.Event;

/**
 * @author synopia
 */
public class ScriptFinished implements Event {
    private String fileName;

    public ScriptFinished(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
