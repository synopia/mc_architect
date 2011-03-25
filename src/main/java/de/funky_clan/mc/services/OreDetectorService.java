package de.funky_clan.mc.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.events.swing.OreFilterChanged;
import de.funky_clan.mc.events.model.OreFound;
import de.funky_clan.mc.events.model.PlayerPositionUpdate;
import de.funky_clan.mc.events.swing.OreDisplayUpdate;
import de.funky_clan.mc.model.Chunk;
import de.funky_clan.mc.model.Ore;
import de.funky_clan.mc.net.packets.ChunkPreparation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author synopia
 */
@Singleton
public class OreDetectorService extends BaseOreDetectorService {
    private final Logger logger = LoggerFactory.getLogger(OreDetectorService.class);

    @Inject
    private EventDispatcher eventDispatcher;
    private HashMap<Long, List<Ore>> ores = new HashMap<Long, List<Ore>>();
    private List<Long> chunksForPlayer = new ArrayList<Long>();
    private HashMap<JComponent, boolean[]> oreTypes = new HashMap<JComponent, boolean[]>();

    @Inject
    public OreDetectorService(ModelEventBus eventBus) {
        super(eventBus);
        logger.info("Starting OreDetectorService...");

        eventBus.registerCallback(PlayerPositionUpdate.class, new EventHandler<PlayerPositionUpdate>() {
            @Override
            public void handleEvent(PlayerPositionUpdate event) {
                if( event.isChunkChanged() ) {
                    buildPlayerList((int) event.getX(), (int) event.getZ());
                    sendPlayerList();
                }
            }
        });
        eventBus.registerCallback(ChunkPreparation.class, new EventHandler<ChunkPreparation>() {
            @Override
            public void handleEvent(ChunkPreparation event) {
                if( !event.isLoad() ) {
                    ores.remove(Chunk.getChunkId(event.getX(), event.getZ()));
                }
            }
        });

        eventBus.registerCallback(OreFound.class, new EventHandler<OreFound>() {
            @Override
            public void handleEvent(OreFound event) {
                List<Ore> list = getOre(event.getChunkId());
                list.clear();
                list.addAll(event.getOres());

                if( chunksForPlayer.contains(event.getChunkId()) ) {
                    sendPlayerList();
                }
            }
        });
        eventBus.registerCallback(OreFilterChanged.class, new EventHandler<OreFilterChanged>() {
            @Override
            public void handleEvent(OreFilterChanged event) {
                oreTypes.put(event.getComponent(), event.getFilter());
                sendPlayerList(event.getComponent());
            }
        });
    }

    protected void buildPlayerList(int x, int z) {
        int chunkX = x>>4;
        int chunkZ = z>>4;
        chunksForPlayer.clear();
        for( int cx=chunkX-2; cx<=chunkX+2; cx++ ) {
            for( int cz=chunkZ-2; cz<chunkZ+2; cz++ ) {
                long id = Chunk.getChunkId(cx, cz);
                chunksForPlayer.add(id);
            }
        }
    }

    protected void sendPlayerList() {
        for (JComponent component : oreTypes.keySet()) {
            sendPlayerList(component);
        }
    }
    protected void sendPlayerList(JComponent component) {
        List<Ore> all = new ArrayList<Ore>();
        int total = 0;
        for (Long id : chunksForPlayer) {
            if( ores.containsKey(id) ) {
                List<Ore> source = ores.get(id);
                total += source.size();
                addFiltered(component, all, source);
            }
        }
        eventDispatcher.fire(new OreDisplayUpdate(component, all, total));
    }

    protected void addFiltered( JComponent component, List<Ore> target, List<Ore> source) {
        boolean[] oreTypes = this.oreTypes.get(component);

        for (Ore ore : source) {
            if( ore.matches(oreTypes) ) {
                target.add(ore);
            }
        }
    }

    protected List<Ore> getOre( long id ) {
        if( ores.containsKey(id) ) {
            return ores.get(id);
        }
        ArrayList<Ore> list = new ArrayList<Ore>();
        ores.put( id, list );
        return list;
    }


}
