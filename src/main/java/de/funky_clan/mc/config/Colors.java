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
    private final Color[]     colorData = new Color[255];

    public Colors() {
        colorData[DataValues.AIR.getId()]                = new Color( 0, 0, 0, 15 );
        colorData[DataValues.STONE.getId()]              = new Color( 120, 120, 120, 255 );
        colorData[DataValues.GRASS.getId()]              = new Color( 100, 150, 62, 255 );
        colorData[DataValues.DIRT.getId()]               = new Color( 134, 96, 67, 255 );
        colorData[DataValues.COBBLESTONE.getId()]        = new Color( 115, 115, 115, 255 );
        colorData[DataValues.WOOD.getId()]               = new Color( 157, 128, 79, 255 );
        colorData[DataValues.SAPLING.getId()]            = new Color( 120, 120, 120, 0 );
        colorData[DataValues.BEDROCK.getId()]            = new Color( 84, 84, 84, 255 );
        colorData[DataValues.WATER.getId()]              = new Color( 38, 65, 128, 50 );
        colorData[DataValues.STATIONARYWATER.getId()]    = colorData[DataValues.WATER.getId()];
        colorData[DataValues.LAVA.getId()]               = new Color( 255, 255, 0, 255 );
        colorData[DataValues.STATIONARYLAVA.getId()]     = colorData[DataValues.LAVA.getId()];
        colorData[DataValues.SAND.getId()]               = new Color( 218, 210, 158, 255 );
        colorData[DataValues.GRAVEL.getId()]             = new Color( 136, 126, 126, 255 );
        colorData[DataValues.GOLDORE.getId()]            = new Color( 143, 140, 125, 255 );
        colorData[DataValues.IRONORE.getId()]            = new Color( 235, 235, 235, 255 );
        colorData[DataValues.COALORE.getId()]            = new Color( 0, 0, 0, 255 );
        colorData[DataValues.LOG.getId()]                = new Color( 102, 81, 51, 255 );
        colorData[DataValues.LEAVES.getId()]             = new Color( 60, 192, 41, 100 );
        colorData[DataValues.GLASS.getId()]              = new Color( 255, 255, 255, 64 );
        colorData[DataValues.WOOL.getId()]               = new Color( 222, 222, 222, 255 );
        colorData[DataValues.YELLOWFLOWER.getId()]       = new Color( 255, 255, 0, 255 );
        colorData[DataValues.REDROSE.getId()]            = new Color( 255, 0, 0, 255 );
        colorData[DataValues.GOLDBLOCK.getId()]          = new Color( 231, 165, 45, 255 );
        colorData[DataValues.IRONBLOCK.getId()]          = new Color( 191, 191, 191, 255 );
        colorData[DataValues.DOUBLESLAB_STONE.getId()]   = new Color( 200, 200, 200, 255 );
        colorData[DataValues.SLAB_STONE.getId()]         = colorData[DataValues.DOUBLESLAB_STONE.getId()];
        colorData[DataValues.BRICK.getId()]              = new Color( 170, 86, 62, 255 );
        colorData[DataValues.TNT.getId()]                = new Color( 160, 83, 65, 255 );
        colorData[DataValues.MOSSYCOBBLESTONE.getId()]   = new Color( 115, 115, 115, 255 );
        colorData[DataValues.OBSIDIAN.getId()]           = new Color( 26, 11, 43, 255 );
        colorData[DataValues.TORCH.getId()]              = new Color( 245, 220, 50, 200 );
        colorData[DataValues.FIRE.getId()]               = new Color( 255, 170, 30, 200 );
        colorData[DataValues.WOODENSTAIRS.getId()]       = new Color( 157, 128, 79, 255 );
        colorData[DataValues.CHEST.getId()]              = new Color( 125, 91, 38, 255 );
        colorData[DataValues.DIAMONDORE.getId()]         = new Color( 255, 50, 255, 255 );
        colorData[DataValues.DIAMONDBLOCK.getId()]       = new Color( 45, 166, 152, 255 );
        colorData[DataValues.WORKBENCH.getId()]          = new Color( 114, 88, 56, 255 );
        colorData[DataValues.CROPS.getId()]              = new Color( 146, 192, 0, 255 );
        colorData[DataValues.SOIL.getId()]               = new Color( 95, 58, 30, 255 );
        colorData[DataValues.FURNACE.getId()]            = new Color( 96, 96, 96, 255 );
        colorData[DataValues.BURNINGFURNACE.getId()]     = colorData[DataValues.FURNACE.getId()];
        colorData[DataValues.SIGNPOST.getId()]           = new Color( 111, 91, 54, 255 );
        colorData[DataValues.WOODENDOOR.getId()]         = new Color( 136, 109, 67, 255 );
        colorData[DataValues.LADDER.getId()]             = new Color( 181, 140, 64, 32 );
        colorData[DataValues.MINECARTTRACK.getId()]      = new Color( 150, 134, 102, 180 );
        colorData[DataValues.COBBLESTONESTAIRS.getId()]  = new Color( 115, 115, 115, 255 );
        colorData[DataValues.IRONDOOR.getId()]           = new Color( 191, 191, 191, 255 );
        colorData[DataValues.REDSTONEORE.getId()]        = new Color( 255,  50,  50, 255 );
        colorData[DataValues.GLOWINGREDSTONEORE.getId()] = colorData[DataValues.REDSTONEORE.getId()];
        colorData[DataValues.REDSTONETORCHON.getId()]    = new Color( 181, 140, 64, 32 );
        colorData[DataValues.REDSTONETORCHOFF.getId()]   = new Color( 255, 0, 0, 200 );
        colorData[DataValues.SNOW.getId()]               = new Color( 255, 255, 255, 255 );
        colorData[DataValues.ICE.getId()]                = new Color( 83, 113, 163, 51 );
        colorData[DataValues.SNOWBLOCK.getId()]          = new Color( 250, 250, 250, 255 );
        colorData[DataValues.CACTUS.getId()]             = new Color( 25, 120, 25, 255 );
        colorData[DataValues.CLAY.getId()]               = new Color( 151, 157, 169, 255 );
        colorData[DataValues.REED.getId()]               = new Color( 193, 234, 150, 255 );
        colorData[DataValues.JUKEBOX.getId()]            = new Color( 100, 67, 50, 255 );
        colorData[DataValues.PUMPKIN.getId()]            = new Color( 192, 181, 21, 255 );
        colorData[DataValues.BLOODSTONE.getId()]         = new Color( 110, 53, 51, 255 );
        colorData[DataValues.SLOWSAND.getId()]           = new Color( 84, 64, 51, 255 );
        colorData[DataValues.LIGHTSTONE.getId()]         = new Color( 137, 112, 64, 255 );
        colorData[DataValues.PORTAL.getId()]             = new Color( 201, 119, 239, 255 );
        colorData[DataValues.JACKOLANTERN.getId()]       = colorData[DataValues.PUMPKIN.getId()];
        colorData[DataValues.LAPIZLAZULIORE.getId()]     = new Color( 20, 69, 143, 255 );
        colorData[DataValues.LAPIZLAZULIBLOCK.getId()]   = new Color( 27, 75, 180, 255 );
        colorData[DataValues.DISPENSER.getId()]          = colorData[DataValues.FURNACE.getId()];
        colorData[DataValues.SANDSTONE.getId()]          = colorData[DataValues.SAND.getId()];
        colorData[DataValues.NOTEBLOCK.getId()]          = colorData[DataValues.JUKEBOX.getId()];
        colorData[DataValues.CAKE.getId()]               = new Color( 229, 201, 202, 255 );
        colorData[DataValues.REPEATERON.getId()]         = colorData[DataValues.REDSTONETORCHON.getId()];
        colorData[DataValues.REPEATEROFF.getId()]        = colorData[DataValues.REDSTONETORCHOFF.getId()];
        colorData[DataValues.BED.getId()]                = new Color( 142, 22, 22, 255 );
        colorData[DataValues.BLUEPRINT.getId()]          = new Color( 40, 40, 255, 255 );
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
        return(( id >= 0 ) && ( id < colorData.length ) && ( colorData[id] != null ))
              ? colorData[id]
              : EMPTY;
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
        colorData[rowIndex] = aValue;
    }
}
