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
    CREEPER(50,  "CreeperFace",  2),
    SKELETON(51, "SkeletonFace", 2),
    SPIDER(52,   "SpiderFace",   1),
    ZOMBIE(54,   "ZombieFace",   2),
    SLIME(55,    "SlimeFace",    1),
    GHAST(56,    "GhastFace",    1),
    PIG_ZOMBIE(57, "ZombiePigmanFace", 2),
    ENDERMAN(58, "EndermanFace", 2),
    CAVE_SPIDER(59, "CaveSpiderFace", 1),
    SILVERFISH(60, "SilverfishFace",1),
    BLAZE(61,   "Blaze_Face", 2),
    LAVA_SLIME(62, "Magma_Cube_Face", 1),
    ENDER_DRAGON(63, "Enderdragon_Face", 2),
    PIG(90, "PigFace", 1),
    SHEEP(91, "SheepFace", 1),
    COW(92, "CowFace", 1),
    CHICKEN(93, "ChickenFace", 1),
    SQUID(94, "Squidface", 1),
    WOLF(95, "WolfFace", 1),
    MUSHROOM_COW(96, "MooshroomFace", 1),
    SNOW_MAN(97, "Snowgolemhead", 2),
    VILLAGER(120, "Villagerhead", 2),
    PLAYER(255, Color.YELLOW, 2),
    SMP_PLAYER(256, Color.YELLOW.darker(), 2)
    ;
    
    private final int typeId;
    private final Color color;
    private final String imageName;
    private final int height;
    
    public static final EntityType[] MAP;
    
    static {
        MAP = new EntityType[257];
        EntityType[] values = EntityType.values();
        for (EntityType value : values) {
            MAP[value.getTypeId()] = value;
        }
    }

    private EntityType(int typeId, Color color, String imageName, int height) {
        this.typeId = typeId;
        this.color = color;
        this.imageName = imageName;
        this.height = height;
    }

    private EntityType(int typeId, Color color) {
        this( typeId, color, null, 1);
    }
    private EntityType(int typeId, Color color, int height) {
        this( typeId, color, null, height );
    }
    private EntityType(int typeId, String imageName ) {
        this( typeId, null, imageName, 1);
    }
    private EntityType(int typeId, String imageName, int height) {
        this( typeId, null, imageName, height );
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

}
