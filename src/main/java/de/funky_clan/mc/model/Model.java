package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;

/**
 * @author synopia
 */
public class Model {
    private HashMap<Integer, BackgroundImage> zSliceImages = new HashMap<Integer, BackgroundImage>();
    private HashMap<Integer, HashMap<Integer, Chunk>> chunks = new HashMap<Integer, HashMap<Integer, Chunk>>();

    public Model() {
    }

    public void setBlock( int sx, int sy, int sz, int sizeX, int sizeY, int sizeZ, byte[] data ) {
        if( sizeX==16 && sizeY==128 && sizeZ==16 ) {
            Chunk chunk = getChunkFor(sx, sy, sz);
            int len = 16*128*16;
            for (int i = 0; i < len; i++) {
                int x = sx + (i>>11);
                int y = i & 0x7f;
                int z = sz + ((i&0x780)>>7);
                if( data[i]==14 || data[i]==15 || data[i]==16 || data[i]==56 ) {
                    chunk.setPixelGlobal(x,y,z, 1);
                }
            }
        } else {
            for( int x=0; x<sizeX; x++ ) {
                for( int y=0; y<sizeY; y++ ) {
                    for( int z=0; z<sizeZ; z++ ) {
                        int i = y + (z*sizeY) + x * sizeY * sizeZ;
                        if( data[i]==14 || data[i]==15 || data[i]==16 || data[i]==56 ) {
                            setPixel(sx+x,sy+y,sz+z, data[i]);
                        }
                    }
                }
            }
        }
    }

    public void setPixel( int x, int y, int z, int value ) {
        getChunkFor(x, y, z).setPixelGlobal(x, y, z, value);
    }

    public int getPixel( int x, int y, int z ) {
        int chunkX = x>>4;
        int chunkY = y>>7;
        int chunkZ = z>>4;

        HashMap<Integer, Chunk> zChunks;
        if(chunks.containsKey(chunkZ)) {
            zChunks = chunks.get(chunkZ);
        } else {
            return 0;
        }
        if( zChunks.containsKey(chunkX) ) {
            Chunk chunk = zChunks.get(chunkX);
            return chunk.getPixelGlobal(x,y,z);
        } else {
            return 0;
        }
    }

    public Chunk getChunkFor( int x, int y, int z ) {
        int chunkX = x>>4;
        int chunkY = y>>7;
        int chunkZ = z>>4;

        Chunk chunk;
        HashMap<Integer, Chunk> zChunks;

        if( chunks.containsKey(chunkZ) ) {
            zChunks = chunks.get(chunkZ);
        } else {
            zChunks = new HashMap<Integer, Chunk>();
            chunks.put(chunkZ, zChunks);
        }

        if( zChunks.containsKey(chunkX) ) {
            chunk = zChunks.get(chunkX);
        } else {
            chunk = new Chunk(chunkX<<4, chunkY<<7, chunkZ<<4, 1<<4, 1<<7, 1<<4 );
            zChunks.put(chunkX, chunk);
        }

        return chunk;
    }

    public void addImage( Slice.SliceType type, int slice, BackgroundImage image ) {
        zSliceImages.put( slice, image );
    }

    public BackgroundImage getImage( Slice.SliceType type, int slice ) {
        BackgroundImage result;

        switch( type ) {
        case Z:
            result = zSliceImages.get( slice );

            break;

        default:
            result = null;
        }

        return result;
    }
}
