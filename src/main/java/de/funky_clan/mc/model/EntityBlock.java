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
public class EntityBlock extends SelectedBlock {
    @Inject
    private Colors  colors;
    @Inject
    private ImageService imageService;
    private int     direction;
    private boolean drawViewCone;
    private int     z;
    private EntityType type = EntityType.PLAYER;

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

    @Override
    public Type getType() {
        return SelectedBlock.Type.CENTERED;
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

    @Override
    public Color getColor() {
        return type.getColor();
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
}
