package de.funky_clan.mc.events.model;

import de.funky_clan.mc.eventbus.Event;
import de.funky_clan.mc.model.EntityBlock;

/**
 * @author synopia
 */
public class EntityPositionUpdate implements Event {
    private final int eid;
    private final EntityBlock block;

    public EntityPositionUpdate(int eid, EntityBlock block) {
        this.eid = eid;
        this.block = block;
    }

    public int getEid() {
        return eid;
    }

    public EntityBlock getBlock() {
        return block;
    }
}
