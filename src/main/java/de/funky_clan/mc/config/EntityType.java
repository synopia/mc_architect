package de.funky_clan.mc.config;

import java.awt.*;

/**
 * @author synopia
 */
public enum EntityType {
//    MINECART(40, Color.GRAY),
//    BOAT(41,     Color.GREEN.darker()),
//    MOB(48,      Color.LIGHT_GRAY),
//    MONSTER(49,  Color.LIGHT_GRAY),
    CREEPER("Creeper", 50,  "CreeperFace",  2),
    SKELETON("Skeleton", 51, "SkeletonFace", 2),
    SPIDER("Spider", 52,   "SpiderFace",   1),
    ZOMBIE("Zombie", 54,   "ZombieFace",   2),
    SLIME("Slime", 55,    "SlimeFace",    1),
    GHAST("Ghast", 56,    "GhastFace",    1),
    PIG_ZOMBIE("Pigman", 57, "ZombiePigmanFace", 2),
    ENDERMAN("Enderman", 58, "EndermanFace", 2),
    CAVE_SPIDER("Cave Spider", 59, "CaveSpiderFace", 1),
    SILVERFISH("Silverfish", 60, "SilverfishFace",1),
    BLAZE("Blaze", 61,   "Blaze_Face", 2),
    LAVA_SLIME("Magma Cube", 62, "Magma_Cube_Face", 1),
    ENDER_DRAGON("Ender Dragon", 63, "Enderdragon_Face", 2),
    PIG("Pig", 90, "PigFace", 1),
    SHEEP("Sheep", 91, "SheepFace", 1),
    COW("Cow", 92, "CowFace", 1),
    CHICKEN("Chicken", 93, "ChickenFace", 1),
    SQUID("Squid", 94, "Squidface", 1),
    WOLF("Wolf", 95, "WolfFace", 1),
    MUSHROOM_COW("Mushroom Cow", 96, "MooshroomFace", 1),
    SNOW_MAN("Snowman Golem", 97, "Snowgolemhead", 2),
    VILLAGER("Villager", 120, "Villagerhead", 2),
    PLAYER("Player", 255, Color.YELLOW, 2),
    SMP_PLAYER("SMP Player", 256, Color.YELLOW.darker(), 2)
    ;
    
    private final int typeId;
    private final Color color;
    private final String imageName;
    private final int height;
    private final String name;
    
    public static final EntityType[] MAP;
    
    static {
        MAP = new EntityType[257];
        EntityType[] values = EntityType.values();
        for (EntityType value : values) {
            MAP[value.getTypeId()] = value;
        }
    }

    private EntityType(String name, int typeId, Color color, String imageName, int height) {
        this.name = name;
        this.typeId = typeId;
        this.color = color;
        this.imageName = imageName;
        this.height = height;
    }

    private EntityType(String name, int typeId, Color color) {
        this( name, typeId, color, null, 1);
    }
    private EntityType(String name, int typeId, Color color, int height) {
        this( name, typeId, color, null, height );
    }
    private EntityType(String name, int typeId, String imageName ) {
        this( name, typeId, null, imageName, 1);
    }
    private EntityType(String name, int typeId, String imageName, int height) {
        this( name, typeId, null, imageName, height );
    }

    public int getTypeId() {
        return typeId;
    }

    public Color getColor() {
        return color;
    }

    public String getImageName() {
        return imageName!=null ? "mobs/"+imageName+".png" : null;
    }

    public double getHeight() {
        return height*.9;
    }

    public double getSize() {
        return 0.7;
    }


    @Override
    public String toString() {
        return name;
    }
}
