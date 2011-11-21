package de.funky_clan.mc.scripts;

import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.events.model.PlayerPositionUpdate;
import de.funky_clan.mc.file.SchematicLoader;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.services.PlayerPositionService;

/**
 * @author synopia
 */
public class SchematicScript extends Script {
    @Inject
    private Model model;
    @Inject
    private SchematicLoader schematicLoader;
    @Inject
    private WorldGraphics worldGraphics;
    @Inject
    private PlayerPositionService playerPositionService;

    @Override
    public void init() {

    }

    @Override
    public void load() {
        author = "";
        name   = getFilename();
        loaded = true;
    }

    @Override
    public void run() {
        running   = true;
        model.clearBlueprint();
        int x = (int) playerPositionService.getX();
        int y = (int) playerPositionService.getY();
        int z = (int) playerPositionService.getZ();
        schematicLoader.load(worldGraphics, getFilename(), x, y, z, 1,1,1);
        running   = false;
        finished  = true;
    }
}
