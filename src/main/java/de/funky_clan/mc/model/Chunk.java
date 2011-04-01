package de.funky_clan.mc.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * A chunk is a map of 16x128x16 blocks. For performance reasons a byte array is used to store block information.
 *
 * @author synopia
 */
public final class Chunk {
    public static final int  CHUNK_ARRAY_SIZE = 16 * 16 * 128;
    private static final int NO_PIXEL_TYPES   = 2;
    private Logger           logger           = LoggerFactory.getLogger( Chunk.class );
    private final long       id;
    private final byte       map[];
    private final int        sizeX;
    private final int        sizeY;
    private final int        sizeZ;
    private final int        startX;
    private final int        startZ;

    public Chunk( final int startX, final int startZ, final int sizeX, final int sizeY, final int sizeZ ) {
        id          = getChunkId( startX >> 4, startZ >> 4 );
        this.startX = startX;
        this.startZ = startZ;
        this.sizeX  = sizeX;
        this.sizeY  = sizeY;
        this.sizeZ  = sizeZ;
        map         = new byte[CHUNK_ARRAY_SIZE * NO_PIXEL_TYPES];
    }

    public static int getChunkXForId( long id ) {
        return(int) ( id >> 32 );
    }

    public static int getChunkYForId( long id ) {
        return(int) ( id );
    }

    public static long getChunkId( int x, int y ) {
        return( (long) ( x ) << 32 ) | ( y & 0xffffffffL );
    }

    public static long getChunkId( double x, double y ) {
        return getChunkId( (int) x >> 4, (int) y >> 4 );
    }

    public long getId() {
        return id;
    }

    public final int getStartX() {
        return startX;
    }

    public final int getStartZ() {
        return startZ;
    }

    public final int getSizeX() {
        return sizeX;
    }

    public final int getSizeY() {
        return sizeY;
    }

    public final int getSizeZ() {
        return sizeZ;
    }

    public final void setPixel( int x, int y, int z, int type, int value ) {
        int sz    = z - startZ;
        int sx    = x - startX;
        int index = y + ( sz * sizeY ) + ( sx * sizeY * sizeZ );

        if(( index < 0 ) || ( index >= CHUNK_ARRAY_SIZE )) {
            throw new IllegalArgumentException( sx + ", " + y + ", " + sz + " is no valid chunk pos" );
        }

        map[index + type * CHUNK_ARRAY_SIZE] = (byte) value;
    }

    public final int getPixel( int x, int y, int z, int type ) {
        int sz    = z - startZ;
        int sx    = x - startX;
        int index = y + ( sz * sizeY ) + ( sx * sizeY * sizeZ );

        if(( index < 0 ) || ( index >= CHUNK_ARRAY_SIZE )) {
            throw new IllegalArgumentException( sx + ", " + y + ", " + sz + " is no valid chunk pos" );
        }

        return map[index + type * CHUNK_ARRAY_SIZE];
    }

    public void updateFullBlock( int type, byte[] data ) {
        System.arraycopy( data, 0, map, CHUNK_ARRAY_SIZE * type, CHUNK_ARRAY_SIZE );
    }

    public void clearBlueprint() {
        Arrays.fill( map, CHUNK_ARRAY_SIZE, 2 * CHUNK_ARRAY_SIZE, (byte) 0 );
    }

    public byte[] getMap() {
        return map;
    }
}
