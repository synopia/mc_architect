package de.funky_clan.mc.events.script;

import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.scripts.Script;

/**
 * @author synopia
 */
public class SendScriptData implements Event {
    private final Script script;

    public SendScriptData( Script script ) {
        this.script = script;
    }

    public Script getScript() {
        return script;
    }
}
