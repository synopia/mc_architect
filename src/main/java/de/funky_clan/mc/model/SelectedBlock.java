package de.funky_clan.mc.model;

import com.google.inject.Inject;
import de.funky_clan.mc.config.Colors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;

/**
 * @author synopia
 */
public class SelectedBlock extends MoveableBlock {
    private Type         type = Type.ON_GRID;
    private final Logger log  = LoggerFactory.getLogger( SelectedBlock.class );
    private Color        color;
    @Inject
    private Colors       colors;
    private int          thickness;

    public enum Type {ON_GRID, CENTERED}

    public SelectedBlock() {
        thickness = 2;
    }

    public Type getType() {
        return type;
    }

    public void setType( Type type ) {
        this.type = type;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness( int thickness ) {
        this.thickness = thickness;
    }

    @Override
    public Color getColor() {
        return colors.getSelectedBlockColor();
    }
}
