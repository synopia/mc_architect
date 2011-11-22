package de.funky_clan.mc.config;

import com.google.inject.Singleton;

import java.awt.Color;

/**
 * A class to keep track of various colors (especially for all block ids).
 *
 * @author synopia
 */
@Singleton
public class Colors {
    public static final Color EMPTY     = new Color( 255, 255, 255, 0 );
    private final Color[] blockColors = new Color[255];
    private final Color[] blueprintColors = new Color[255];

    public Colors() {
        blockColors[DataValues.AIR.getId()]                = new Color( 0, 0, 0, 15 );
        blockColors[DataValues.STONE.getId()]              = new Color( 120, 120, 120, 255 );
        blockColors[DataValues.GRASS.getId()]              = new Color( 100, 150, 62, 255 );
        blockColors[DataValues.DIRT.getId()]               = new Color( 134, 96, 67, 255 );
        blockColors[DataValues.COBBLESTONE.getId()]        = new Color( 115, 115, 115, 255 );
        blockColors[DataValues.WOOD.getId()]               = new Color( 157, 128, 79, 255 );
        blockColors[DataValues.SAPLING.getId()]            = new Color( 120, 120, 120, 0 );
        blockColors[DataValues.BEDROCK.getId()]            = new Color( 84, 84, 84, 255 );
        blockColors[DataValues.WATER.getId()]              = new Color( 38, 65, 128, 50 );
        blockColors[DataValues.STATIONARYWATER.getId()]    = blockColors[DataValues.WATER.getId()];
        blockColors[DataValues.LAVA.getId()]               = new Color( 255, 255, 0, 255 );
        blockColors[DataValues.STATIONARYLAVA.getId()]     = blockColors[DataValues.LAVA.getId()];
        blockColors[DataValues.SAND.getId()]               = new Color( 218, 210, 158, 255 );
        blockColors[DataValues.GRAVEL.getId()]             = new Color( 136, 126, 126, 255 );
        blockColors[DataValues.GOLDORE.getId()]            = new Color( 143, 140, 125, 255 );
        blockColors[DataValues.IRONORE.getId()]            = new Color( 235, 235, 235, 255 );
        blockColors[DataValues.COALORE.getId()]            = new Color( 0, 0, 0, 255 );
        blockColors[DataValues.LOG.getId()]                = new Color( 102, 81, 51, 255 );
        blockColors[DataValues.LEAVES.getId()]             = new Color( 60, 192, 41, 100 );
        blockColors[DataValues.GLASS.getId()]              = new Color( 255, 255, 255, 64 );
        blockColors[DataValues.WOOL.getId()]               = new Color( 222, 222, 222, 255 );
        blockColors[DataValues.YELLOWFLOWER.getId()]       = new Color( 255, 255, 0, 255 );
        blockColors[DataValues.REDROSE.getId()]            = new Color( 255, 0, 0, 255 );
        blockColors[DataValues.GOLDBLOCK.getId()]          = new Color( 231, 165, 45, 255 );
        blockColors[DataValues.IRONBLOCK.getId()]          = new Color( 191, 191, 191, 255 );
        blockColors[DataValues.DOUBLESLAB_STONE.getId()]   = new Color( 200, 200, 200, 255 );
        blockColors[DataValues.SLAB_STONE.getId()]         = blockColors[DataValues.DOUBLESLAB_STONE.getId()];
        blockColors[DataValues.BRICK.getId()]              = new Color( 170, 86, 62, 255 );
        blockColors[DataValues.TNT.getId()]                = new Color( 160, 83, 65, 255 );
        blockColors[DataValues.MOSSYCOBBLESTONE.getId()]   = new Color( 115, 115, 115, 255 );
        blockColors[DataValues.OBSIDIAN.getId()]           = new Color( 26, 11, 43, 255 );
        blockColors[DataValues.TORCH.getId()]              = new Color( 245, 220, 50, 200 );
        blockColors[DataValues.FIRE.getId()]               = new Color( 255, 170, 30, 200 );
        blockColors[DataValues.WOODENSTAIRS.getId()]       = new Color( 157, 128, 79, 255 );
        blockColors[DataValues.CHEST.getId()]              = new Color( 125, 91, 38, 255 );
        blockColors[DataValues.DIAMONDORE.getId()]         = new Color( 255, 50, 255, 255 );
        blockColors[DataValues.DIAMONDBLOCK.getId()]       = new Color( 45, 166, 152, 255 );
        blockColors[DataValues.WORKBENCH.getId()]          = new Color( 114, 88, 56, 255 );
        blockColors[DataValues.CROPS.getId()]              = new Color( 146, 192, 0, 255 );
        blockColors[DataValues.SOIL.getId()]               = new Color( 95, 58, 30, 255 );
        blockColors[DataValues.FURNACE.getId()]            = new Color( 96, 96, 96, 255 );
        blockColors[DataValues.BURNINGFURNACE.getId()]     = blockColors[DataValues.FURNACE.getId()];
        blockColors[DataValues.SIGNPOST.getId()]           = new Color( 111, 91, 54, 255 );
        blockColors[DataValues.WOODENDOOR.getId()]         = new Color( 136, 109, 67, 255 );
        blockColors[DataValues.LADDER.getId()]             = new Color( 181, 140, 64, 32 );
        blockColors[DataValues.MINECARTTRACK.getId()]      = new Color( 150, 134, 102, 180 );
        blockColors[DataValues.COBBLESTONESTAIRS.getId()]  = new Color( 115, 115, 115, 255 );
        blockColors[DataValues.IRONDOOR.getId()]           = new Color( 191, 191, 191, 255 );
        blockColors[DataValues.REDSTONEORE.getId()]        = new Color( 255,  50,  50, 255 );
        blockColors[DataValues.GLOWINGREDSTONEORE.getId()] = blockColors[DataValues.REDSTONEORE.getId()];
        blockColors[DataValues.REDSTONETORCHON.getId()]    = new Color( 181, 140, 64, 32 );
        blockColors[DataValues.REDSTONETORCHOFF.getId()]   = new Color( 255, 0, 0, 200 );
        blockColors[DataValues.SNOW.getId()]               = new Color( 255, 255, 255, 255 );
        blockColors[DataValues.ICE.getId()]                = new Color( 83, 113, 163, 51 );
        blockColors[DataValues.SNOWBLOCK.getId()]          = new Color( 250, 250, 250, 255 );
        blockColors[DataValues.CACTUS.getId()]             = new Color( 25, 120, 25, 255 );
        blockColors[DataValues.CLAY.getId()]               = new Color( 151, 157, 169, 255 );
        blockColors[DataValues.REED.getId()]               = new Color( 193, 234, 150, 255 );
        blockColors[DataValues.JUKEBOX.getId()]            = new Color( 100, 67, 50, 255 );
        blockColors[DataValues.PUMPKIN.getId()]            = new Color( 192, 181, 21, 255 );
        blockColors[DataValues.BLOODSTONE.getId()]         = new Color( 110, 53, 51, 255 );
        blockColors[DataValues.SLOWSAND.getId()]           = new Color( 84, 64, 51, 255 );
        blockColors[DataValues.LIGHTSTONE.getId()]         = new Color( 137, 112, 64, 255 );
        blockColors[DataValues.PORTAL.getId()]             = new Color( 201, 119, 239, 255 );
        blockColors[DataValues.JACKOLANTERN.getId()]       = blockColors[DataValues.PUMPKIN.getId()];
        blockColors[DataValues.LAPIZLAZULIORE.getId()]     = new Color( 20, 69, 143, 255 );
        blockColors[DataValues.LAPIZLAZULIBLOCK.getId()]   = new Color( 27, 75, 180, 255 );
        blockColors[DataValues.DISPENSER.getId()]          = blockColors[DataValues.FURNACE.getId()];
        blockColors[DataValues.SANDSTONE.getId()]          = blockColors[DataValues.SAND.getId()];
        blockColors[DataValues.NOTEBLOCK.getId()]          = blockColors[DataValues.JUKEBOX.getId()];
        blockColors[DataValues.CAKE.getId()]               = new Color( 229, 201, 202, 255 );
        blockColors[DataValues.REPEATERON.getId()]         = blockColors[DataValues.REDSTONETORCHON.getId()];
        blockColors[DataValues.REPEATEROFF.getId()]        = blockColors[DataValues.REDSTONETORCHOFF.getId()];
        blockColors[DataValues.BED.getId()]                = new Color( 142, 22, 22, 255 );

        blueprintColors[0] = EMPTY;
        for (int i = 1; i < blueprintColors.length; i++) {
            blueprintColors[i] = new Color( 40, 40, 255, 255 );
        }
    }

    public final float[] getColorForBlock( int id, float[] color ) {
        Color c = getColorForBlock( id );

        color[0] = c.getRed() / 255.f;
        color[1] = c.getGreen() / 255.f;
        color[2] = c.getBlue() / 255.f;
        color[3] = c.getAlpha() / 255.f;

        return color;
    }

    public final Color getColorForBlock( int id ) {
        return(( id >= 0 ) && ( id < blockColors.length ) && ( blockColors[id] != null ))
              ? blockColors[id]
              : EMPTY;
    }
    
    public final Color getColorForBlueprint( int id ) {
        return(( id >= 0 ) && ( id < blueprintColors.length ) && ( blueprintColors[id] != null ))
              ? blueprintColors[id]
              : EMPTY;
    }

    public final float[] getColorForBlueprint( int id, float[] color ) {
        Color c = getColorForBlueprint(id);

        color[0] = c.getRed() / 255.f;
        color[1] = c.getGreen() / 255.f;
        color[2] = c.getBlue() / 255.f;
        color[3] = c.getAlpha() / 255.f;

        return color;
    }

    public Color getBackgroundColor() {
        return Color.LIGHT_GRAY;
    }

    public Color getPlayerBlockColor() {
        return Color.YELLOW.brighter();
    }

    public Color getSelectedBlockColor() {
        return Color.BLUE;
    }

    public void setColorForBlock( int rowIndex, Color aValue ) {
        blockColors[rowIndex] = aValue;
    }

    public void setColorForBlueprint( int rowIndex, Color aValue ) {
        blueprintColors[rowIndex] = aValue;
    }
}
