package de.funky_clan.mc.events.script;

import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.scripts.Script;

/**
 * @author synopia
 */
public class LoadScript implements Event {
    private String fileName;
    private boolean useClasspath;
    private Script script;

    public LoadScript(String fileName, boolean useClasspath) {
        this.fileName = fileName;
        this.useClasspath = useClasspath;
    }

    public LoadScript( Script script ) {
        this.script = script;
    }

    public LoadScript(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isUseClasspath() {
        return useClasspath;
    }

    public Script getScript() {
        return script;
    }

    public boolean hasScript() {
        return script!=null;
    }
}
