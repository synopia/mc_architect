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
    private SchematicLoader.Schematic schematic;

    @Override
    public void init() {

    }

    @Override
    public void load() {
        author = "";
        name   = getFilename();
        
        schematic = schematicLoader.load(name);
        if( schematic!=null ) {
            loaded = true;
        }
    }

    @Override
    public void run() {
        running   = true;
        model.clearBlueprint();
        int x = (int) playerPositionService.getX();
        int y = (int) playerPositionService.getY();
        int z = (int) playerPositionService.getZ();
        int angle = (int) playerPositionService.getYaw()+45;
        angle %= 360;
        while( angle < 0 ) {
            angle += 360;
        }

        int dir = 0;
        if( angle < 90 ) {
            dir = 0;
        } else if( angle < 180 ) {
            dir = 1;
        } else if( angle < 270 ) {
            dir = 2;
        } else {
            dir = 3;
        }
        
        schematic.inject(worldGraphics, x, y, z, dir);
        running   = false;
        finished  = true;
    }
}
