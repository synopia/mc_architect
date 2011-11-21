package de.funky_clan.mc.services;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.events.model.ModelUpdate;
import de.funky_clan.mc.events.model.OreFound;
import de.funky_clan.mc.model.Chunk;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.Ore;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author synopia
 */
public abstract class BaseOreDetectorService {
    private final boolean[] closedMap = new boolean[16 * 128 * 16];
    private ArrayList<Ore>  ores      = new ArrayList<Ore>();
    private byte[]          data;
    @Inject
    private EventDispatcher eventDispatcher;
    @Inject
    private Model           model;
    private int             startX;
    private int             startY;
    private int             startZ;
    @Inject
    private Provider<Ore>   oreProvider;

    @Inject
    public BaseOreDetectorService( final ModelEventBus eventBus ) {
        eventBus.subscribe(ModelUpdate.class, new EventHandler<ModelUpdate>() {
            @Override
            public void handleEvent(ModelUpdate event) {
                Arrays.fill(closedMap, false);
                ores = new ArrayList<Ore>();
                startX = event.getStartX();
                startY = event.getStartY();
                startZ = event.getStartZ();
                model.interate(event, new Model.BlockUpdateCallable() {
                            @Override
                            public void updateChunk(Chunk chunk) {
                                BaseOreDetectorService.this.data = chunk.getMap();

                                int len = 16 * 128 * 16;

                                for (int i = 0; i < len; i++) {
                                    int x = (i >> 11);
                                    int y = i & 0x7f;
                                    int z = ((i & 0x780) >> 7);

                                    if (isOre(data[i])) {
                                        if (!closedMap[i]) {
                                            Ore ore = findOre(startX + x, startY + y, startZ + z, data[i]);

                                            if (ore == null) {
                                                ore = oreProvider.get();
                                                ore.setOreType(data[i]);
                                            }

                                            ore = followOre(x, y, z, ore);

                                            if ((ore != null) && !ores.contains(ore)) {
                                                ores.add(ore);
                                            }
                                        }
                                    }
                                }

                                eventDispatcher.publish(new OreFound(chunk.getId(), ores));
                            }

                            @Override
                            public void updateBlock(Chunk chunk, int x, int y, int z, int value) {

                                // todo
                            }
                        });
            }
        });
    }

    public boolean isOre(int data) {
        return( data == DataValues.IRONORE.getId() ) || ( data == DataValues.COALORE.getId() )
              || ( data == DataValues.DIAMONDORE.getId() ) || ( data == DataValues.GOLDORE.getId() )
              || ( data == DataValues.LAPIZLAZULIORE.getId() ) || ( data == DataValues.REDSTONEORE.getId() );
    }

    public Ore findOre(int x, int y, int z, int dataValue ) {
        for( Ore ore : ores ) {
            if( ore.contains( x, y, z, dataValue )) {
                return ore;
            }
        }

        return null;
    }

    protected Ore followOre(int x, int y, int z, Ore ore) {
        Ore result = ore;
        Ore otherChunk;

        if(( x < 0 ) || ( y < 0 ) || ( z < 0 ) || ( x > 15 ) || ( y > 127 ) || ( z > 15 )) {
            int pixel = model.getPixel( startX + x, startY + y, startZ + z, 0 );

            if( isOre( pixel )) {
                otherChunk = findOre( startX + x, startY + y, startZ + z, ore.getOreType().getBlockId() );

                if( otherChunk != null ) {
                    result = otherChunk;
                    result.addOre( ore );
                } else {
                    ore.addOre( startX + x, startY + y, startZ + z);
                    result = ore;
                }
            }

            return result;
        }

        int index = y + ( z * 128 + ( x * 128 * 16 ));

        if( closedMap[index] ) {
            return ore;
        }

        if( data[index]==ore.getOreType().getBlockId() ) {
            otherChunk = findOre( startX + x, startY + y, startZ + z, ore.getOreType().getBlockId() );

            if( otherChunk != null ) {
                result = otherChunk;
                result.addOre( ore );
            } else {
                ore.addOre( startX + x, startY + y, startZ + z);
                result = ore;
            }

            closedMap[index] = true;
        } else {
            if( !isOre(data[index]) ) {
                closedMap[index] = true;
            }
            return ore;
        }

        for( int nx=-1; nx<=1; nx ++ ) {
            for( int ny=-1; ny<=1; ny ++ ) {
                for( int nz=-1; nz<=1; nz ++ ) {
                    if( nx==0 && ny==0 && nz==0 ) {
                        continue;
                    }
                    result = followOre(x+nx, y+ny, z+nz, result);
                }
            }
        }

        return result;
    }
}
