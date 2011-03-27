package de.funky_clan.mc.events.script;

import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.scripts.Script;

/**
 * @author synopia
 */
public class ScriptFinished implements Event {
    private Script script;

    public ScriptFinished(Script script) {
        this.script = script;
    }

    public Script getScript() {
        return script;
    }
}
