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
    private OreType         oreType;
    private int             startX;
    private int             startY;
    private int             startZ;
    @Inject 
    private Colors          colors;
    private int             numberOfBlocks;

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

    public void addOre( int x, int y, int z ) {
        startX                                          = Math.min( startX, x );
        startY                                          = Math.min( startY, y );
        startZ                                          = Math.min( startZ, z );
        endX                                            = Math.max( endX, x );
        endY                                            = Math.max( endY, y );
        endZ                                            = Math.max( endZ, z );
        numberOfBlocks ++;
    }

    public boolean contains( int x, int y, int z, int dataValue ) {
        return oreType.getBlockId()==dataValue && ( x >= startX ) && ( y >= startY ) && ( z >= startZ ) && ( x <= endX ) && ( y <= endY ) && ( z <= endZ );
    }

    public void addOre( Ore ore ) {
        startX                                          = Math.min( startX, ore.getStartX() );
        startY                                          = Math.min(startY, ore.getStartY());
        startZ                                          = Math.min(startZ, ore.getStartZ());
        endX                                            = Math.max( endX,   ore.getEndX() );
        endY                                            = Math.max( endY,   ore.getEndX() );
        endZ                                            = Math.max( endZ,   ore.getEndX() );
        numberOfBlocks += ore.numberOfBlocks;
    }

    public OreType getOreType() {
        return oreType;
    }

    public boolean matches( boolean[] oreTypes ) {
        return oreTypes[oreType.ordinal()];
    }
    
    public Color getColor() {
        return colors.getColorForBlock(oreType.getBlockId());
    }

    public void setOreType( int blockId ) {
        oreType = OreType.forBlockId(blockId);
    }

    public int getNumberOfBlocks() {
        return numberOfBlocks;
    }
}
