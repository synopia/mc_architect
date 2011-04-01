package de.funky_clan.mc.config;

/**
 * Definition BlockIds.
 *
 * <b>Notice:</b> To get the real block ids used by minecraft, use
 * <code>DataValues.STONE.getId()</code>, instead of the enum's ordinal!
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

    BLUEPRINT,
    NOT_LOADED,
    ;
    //J+

    private static int lastId;
    private int        id;

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
