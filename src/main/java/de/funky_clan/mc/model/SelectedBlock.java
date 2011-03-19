package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * @author synopia
 */
public class SelectedBlock {
    public enum Type {
        ON_GRID,
        CENTERED
    }
    private Color color;
    private int   thickness;
    private double positionX;
    private double positionY;
    private double positionZ;
    private Type type = Type.ON_GRID;
    private final Logger log = LoggerFactory.getLogger(SelectedBlock.class);

    public SelectedBlock() {
        thickness = 2;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness( int thickness ) {
        this.thickness = thickness;
    }

    public void setPosition(double x, double y, double z) {
        positionX = x;
        positionY = y;
        positionZ = z;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getPositionZ() {
        return positionZ;
    }

    public Color getColor() {
        return color;
    }

    public void setColor( Color color ) {
        this.color = color;
    }
}
