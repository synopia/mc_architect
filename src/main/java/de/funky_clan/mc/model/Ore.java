package de.funky_clan.mc.model;

import com.google.inject.Inject;
import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.config.DataValues;

import java.awt.*;

/**
 * @author synopia
 */
public class Ore {
    private int             endX;
    private int             endY;
    private int             endZ;
    private final boolean[] oreTypes;
    private int             startX;
    private int             startY;
    private int             startZ;
    @Inject 
    private Colors          colors;

    public Ore() {
        this( Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE );
    }
    public Ore( int startX, int startY, int startZ ) {
        this( startX, startY, startZ, startX, startY, startZ );
    }

    public Ore( int startX, int startY, int startZ, int endX, int endY, int endZ ) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.endX   = endX;
        this.endY   = endY;
        this.endZ   = endZ;
        oreTypes    = new boolean[OreType.values().length];
    }

    public enum OreType {
        NO_ORE( -1 ), COAL( DataValues.COALORE.getId(), "Coal" ), GOLD( DataValues.GOLDORE.getId(), "Gold" ),
        IRON( DataValues.IRONORE.getId(), "Iron" ), DIAMOND( DataValues.DIAMONDORE.getId(), "Diamond" ),
        REDSTONE( DataValues.REDSTONEORE.getId(), "Redstone" ),
        LAPIZLAZULI( DataValues.LAPIZLAZULIORE.getId(), "Lapizlazuli" )
        ;

        private final int    blockId;
        private final String text;

        OreType( int blockId ) {
            this( blockId, null );
        }

        OreType( int blockId, String text ) {
            this.blockId = blockId;
            this.text    = text;
        }

        public int getBlockId() {
            return blockId;
        }

        public static OreType forBlockId( int id ) {
            for( OreType type : OreType.values() ) {
                if( type.getBlockId() == id ) {
                    return type;
                }
            }

            return NO_ORE;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartZ() {
        return startZ;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getEndZ() {
        return endZ;
    }

    public void addOre( int x, int y, int z, int value ) {
        oreTypes[OreType.forBlockId( value ).ordinal()] = true;
        startX                                          = Math.min( startX, x );
        startY                                          = Math.min( startY, y );
        startZ                                          = Math.min( startZ, z );
        endX                                            = Math.max( endX, x );
        endY                                            = Math.max( endY, y );
        endZ                                            = Math.max( endZ, z );
    }

    public boolean contains( int x, int y, int z ) {
        return( x >= startX ) && ( y >= startY ) && ( z >= startZ ) && ( x <= endX ) && ( y <= endY ) && ( z <= endZ );
    }

    public void addOre( Ore ore ) {
        for( int i = 0; i < oreTypes.length; i++ ) {
            oreTypes[i] |= ore.oreTypes[i];
        }

        addOre( ore.getStartX(), ore.getStartY(), ore.getStartZ(), -1 );
        addOre( ore.getEndX(), ore.getEndY(), ore.getEndZ(), -1 );
    }

    public boolean hasOreType( OreType type ) {
        return oreTypes[type.ordinal()];
    }

    public boolean matches( boolean[] oreTypes ) {
        for( int i = 0; i < oreTypes.length; i++ ) {
            if( oreTypes[i] != this.oreTypes[i] ) {
                if( !oreTypes[i] ) {
                    return false;
                }
            }
        }

        return true;
    }
    
    public Color getColor() {
        Color result = null;
        for (int i = 0; i < oreTypes.length; i++) {
            if( oreTypes[i] ) {
                result = colors.getColorForBlock(OreType.values()[i].getBlockId());
                break;
            }
        }
        return result;
    }
}
