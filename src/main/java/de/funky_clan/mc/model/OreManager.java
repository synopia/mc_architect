package de.funky_clan.mc.model;

import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.OreDisplayUpdate;
import de.funky_clan.mc.events.OreFound;
import de.funky_clan.mc.events.PlayerMoved;
import de.funky_clan.mc.events.UnloadChunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author synopia
 */
public class OreManager {
    private EventBus eventBus;
    private HashMap<Long, List<Ore>> ores = new HashMap<Long, List<Ore>>();
    private List<Long> chunksForPlayer = new ArrayList<Long>();

    @Inject
    public OreManager(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.registerCallback(PlayerMoved.class, new EventHandler<PlayerMoved>() {
            @Override
            public void handleEvent(PlayerMoved event) {
                if( event.isChunkChanged() ) {
                    buildPlayerList((int) event.getX(), (int) event.getZ());
                    sendPlayerList();
                }
            }
        });
        eventBus.registerCallback(UnloadChunk.class, new EventHandler<UnloadChunk>() {
            @Override
            public void handleEvent(UnloadChunk event) {
                ores.remove(Chunk.getChunkId(event.getChunkX(), event.getChunkZ()));
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
        List<Ore> all = new ArrayList<Ore>();
        for (Long id : chunksForPlayer) {
            if( ores.containsKey(id) ) {
                all.addAll( ores.get(id) );
            }
        }

        eventBus.fireEvent( new OreDisplayUpdate(all));
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
