package de.funky_clan.mc.model;

import com.google.inject.Inject;
import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.config.EntityType;
import de.funky_clan.mc.services.ImageService;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author synopia
 */
public class EntityBlock extends MoveableBlock {
    @Inject
    private Colors  colors;
    @Inject
    private ImageService imageService;
    private int     direction;
    private boolean drawViewCone;
    private int     z;
    private EntityType type = EntityType.PLAYER;
    private String name;

    public void setType(EntityType type) {
        this.type = type;
    }

    public void setDrawViewCone( boolean drawViewCone ) {
        this.drawViewCone = drawViewCone;
    }

    public boolean isDrawViewCone() {
        return drawViewCone;
    }

    public int getZ() {
        return z;
    }

    public void setZ( int z ) {
        this.z = z;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection( int direction ) {
        this.direction = direction + 90;
    }

    @Override
    public double getHeight() {
        return type.getHeight();
    }

    @Override
    public double getSizeX() {
        return type.getSize();
    }

    @Override
    public double getSizeY() {
        return type.getSize();
    }

    public EntityType getType() {
        return type;
    }

    @Override
    public Color getColor() {
        return type.getColor();
    }

    public boolean matches( boolean [] filter ) {
        return filter[type.ordinal()];
    }

    public void move( double dx, double dy, double dz ) {
        setPosition( getPositionX()+dx, getPositionY()+dy, getPositionZ()+dz );
    }
    
    public BufferedImage getImage() {
        if( type!=null ) {
            return imageService.getImage(type);
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
