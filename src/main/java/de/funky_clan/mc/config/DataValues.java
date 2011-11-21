package de.funky_clan.mc.config;

/**
 * <p>Definition of BlockIds.</p>
 *
 * <p><b>Notice:</b> To get the real block ids used by minecraft, use
 * <code>DataValues.STONE.getId()</code>, instead of the enum's ordinal!</p>
 *
 * @author synopia
 */
public enum DataValues {
    //J-
    AIR(0),
    STONE,
    GRASS,
    DIRT,
    COBBLESTONE,
    WOOD,
    SAPLING,
    BEDROCK,
    WATER,
    STATIONARYWATER,
    LAVA,
    STATIONARYLAVA,
    SAND,
    GRAVEL,
    GOLDORE,
    IRONORE,
    COALORE,
    LOG,
    LEAVES,
    SPONGE,
    GLASS,
    LAPIZLAZULIORE,
    LAPIZLAZULIBLOCK,
    DISPENSER,
    SANDSTONE,
    NOTEBLOCK,
    BED,
    WOOL(35),
    YELLOWFLOWER(37),
    REDROSE,
    BROWNMUSHROOM,
    REDMUSHROOM,
    GOLDBLOCK,
    IRONBLOCK,
    DOUBLESLAB_STONE,
    SLAB_STONE,
    BRICK,
    TNT,
    BOOKSHELF,
    MOSSYCOBBLESTONE,
    OBSIDIAN,
    TORCH,
    FIRE,
    MOBSPAWNER,
    WOODENSTAIRS,
    CHEST,
    REDSTONEWIRE,
    DIAMONDORE,
    DIAMONDBLOCK,
    WORKBENCH,
    CROPS,
    SOIL,
    FURNACE,
    BURNINGFURNACE,
    SIGNPOST,
    WOODENDOOR,
    LADDER,
    MINECARTTRACK,
    COBBLESTONESTAIRS,
    WALLSIGN,
    LEVER,
    STONEPRESSUREPLATE,
    IRONDOOR,
    WOODPRESSUREPLATE,
    REDSTONEORE,
    GLOWINGREDSTONEORE,
    REDSTONETORCHON,
    REDSTONETORCHOFF,
    STONEBUTTON,
    SNOW,
    ICE,
    SNOWBLOCK,
    CACTUS,
    CLAY,
    REED,
    JUKEBOX,
    FENCE,
    PUMPKIN,
    BLOODSTONE,
    SLOWSAND,
    LIGHTSTONE,
    PORTAL,
    JACKOLANTERN,
    CAKE,
    REPEATEROFF,
    REPEATERON,
    LOCKED_CHEST,

    TRAPDOOR,
    HIDDEN_SILVERFISH,
    STONE_BRICKS,
    HUGE_BROWN_MUSHROOM,
    HUGE_RED_MUSHROOM,
    IRON_BARS,
    GLASS_PANE,
    MELON,
    PUMPKIN_STEM,
    MELON_STEM,
    VINES,
    FENCE_GATE,
    BRICK_STAIRS,
    STONE_BRICK_STAIRS,
    MYCELIUM,
    LILY_PAD,
    NETHER_BRICK,
    NETHER_BRICK_FENCE,
    NETHER_BRICK_STAIRS,
    NETHER_WART,
    ENCHANTMENT_TABLE,
    BREWING_STAND,
    CAULDRON,
    END_PORTAL,
    END_PORTAL_FRAME,
    END_STONE,
    DRAGON_EGG,

    BLUEPRINT,
    NOT_LOADED,
    ;
    //J+

    private static int lastId;
    private final int  id;

    DataValues() {
        this.id = nextId();
    }

    DataValues( int id ) {
        this.id = setId( id );
    }

    public static int setId( int id ) {
        lastId = id;

        return nextId();
    }

    public static int nextId() {
        return lastId++;
    }

    public int getId() {
        return id;
    }

    public static DataValues find( int blockId ) {
        for( DataValues values : DataValues.values() ) {
            if( values.getId() == blockId ) {
                return values;
            }
        }

        return NOT_LOADED;
    }
}
