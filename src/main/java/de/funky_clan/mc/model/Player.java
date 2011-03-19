package de.funky_clan.mc.model;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author paul.fritsche@googlemail.com
 */
public class Player extends SelectedBlock {
    private int     direction;
    private boolean drawViewCone;
    private int     z;

    public void setDrawViewCone(boolean drawViewCone) {
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
        this.direction = direction+90;
    }



/*
    todo verify
    public void repaint( JComponent component, RenderContext c ) {
        component.repaint(
                c.modelToScreenX(getX() - 15), c.modelToScreenX(getY() - 15),
                c.screenUnitX(getX()-15, getX()+15), c.screenUnitY(getY()-15, getY()+15)
        );
    }
*/
}
