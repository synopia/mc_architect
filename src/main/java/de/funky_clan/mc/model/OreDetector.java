package de.funky_clan.mc.model;

import com.google.inject.Inject;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.events.network.ChunkUpdate;
import de.funky_clan.mc.events.background.OreFound;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author synopia
 */
public class OreDetector {
    private EventBus eventBus;
    @Inject
    private Model model;
    private ArrayList<Ore> ores = new ArrayList<Ore>();
    private boolean[] closedMap = new boolean[16*128*16];
    private byte[] data;
    private int startX;
    private int startY;
    private int startZ;

    @Inject
    public OreDetector(final EventBus eventBus) {
        this.eventBus = eventBus;

        eventBus.registerCallback(ChunkUpdate.class, new EventHandler<ChunkUpdate>() {
            @Override
            public void handleEvent(ChunkUpdate event) {
                Arrays.fill(closedMap,false);
                ores = new ArrayList<Ore>();

                startX = event.getSx();
                startY = event.getSy();
                startZ = event.getSz();
                int sizeX = event.getSizeX();
                int sizeY = event.getSizeY();
                int sizeZ = event.getSizeZ();
                data = event.getData();

                if( sizeX==16 && sizeY==128 && sizeZ==16 ) {
                    Chunk chunk = model.getOrCreateChunk(startX, startY, startZ);
                    int len = 16*128*16;
                    for (int i = 0; i < len; i++) {
                        int x = (i>>11);
                        int y = i & 0x7f;
                        int z = ((i&0x780)>>7);

                        if( isOre( data[i] ) ) {
                            if( !closedMap[i] ) {
                                Ore ore = findOre(startX + x, startY + y, startZ + z);
                                if( ore==null ) {
                                    ore = new Ore(startX + x, startY + y, startZ + z);
                                }

                                ore = followOre(x,y,z, ore);
                                if( ore!=null && !ores.contains(ore) ) {
                                    ores.add(ore);
                                }
                            }
                        }
                    }
                    eventBus.fireEvent(new OreFound(chunk.getId(), ores));
                } else {
//                    throw new RuntimeException(sizeX+", "+sizeY+", "+sizeZ);
                }
            }
        });
    }

    public boolean isOre( int data ) {
        return data == DataValues.IRONORE.getId() ||
                data == DataValues.COALORE.getId() ||
                data == DataValues.DIAMONDORE.getId() ||
                data == DataValues.GOLDORE.getId() ||
                data == DataValues.LAPIZLAZULIORE.getId() ||
                data == DataValues.REDSTONEORE.getId();
    }

    public Ore findOre( int x, int y, int z) {
        for (Ore ore : ores) {
            if( ore.contains(x, y, z) ) {
                return ore;
            }
        }
        return null;
    }

    protected Ore followOre( int x, int y, int z, Ore ore ) {
        Ore result = ore;
        Ore otherChunk = null;
        if( x<0 || y<0 || z<0 || x>15 || y>127 || z>15 ) {
            int pixel = model.getPixel(startX + x, startY + y, startZ + z, 0);
            if( isOre(pixel) ) {
                otherChunk = findOre( startX + x, startY + y, startZ + z );
                if( otherChunk !=null ) {
                    result = otherChunk ;
                    result.addOre( ore );

                } else {
                    ore.addOre(startX + x, startY + y, startZ + z, pixel);
                    result = ore;
                }
            }
            return result;
        }
        int index = y + (z * 128 + (x * 128 * 16));
        if( closedMap[index] ){
            return ore;
        }
        closedMap[index] = true;
        if( isOre(data[index]) ) {
            result.addOre(startX + x, startY + y, startZ + z, data[index]);
        } else {
            return ore;
        }

        result = followOre( x+1, y, z, result );
        result = followOre( x, y+1, z, result );
        result = followOre( x, y, z+1, result );
        result = followOre( x-1, y, z, result );
        result = followOre( x, y-1, z, result );
        result = followOre( x, y, z-1, result );

        return result;
    }
}
