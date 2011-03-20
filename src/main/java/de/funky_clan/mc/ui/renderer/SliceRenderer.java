package de.funky_clan.mc.ui.renderer;

import de.funky_clan.mc.math.Position;
import de.funky_clan.mc.model.RenderContext;
import de.funky_clan.mc.model.Slice;

import java.awt.*;

/**
 * @author synopia
 */
public class SliceRenderer implements Renderer<Slice> {
    @Override
    public void render(Slice slice, RenderContext c) {
        Graphics2D g  = c.getGraphics();
        int        sx = c.getWindowStartX() - 1;
        int        sy = c.getWindowStartY() - 1;
        int        ex = c.getWindowEndX() + 1;
        int        ey = c.getWindowEndY() + 1;

        if( slice.getSlice()==-999 ) {
            return;
        }

        Position pos = c.getPosition();
        pos.setSlice(sx,sy,slice.getSlice());
        int chunkStartX = (int)pos.getWorldX()>>4;
        int chunkStartY = (int)pos.getWorldZ()>>4;
        int startZ = (int)pos.getWorldY();
        pos.setSlice(ex,ey,slice.getSlice());
        int chunkEndX = (int)pos.getWorldX()>>4;
        int chunkEndY = (int)pos.getWorldZ()>>4;
        int endZ = (int)pos.getWorldY();


        Position position = c.getPosition();
        for( int y = sy; y < ey; y++ ) {
            for( int x = sx; x < ex; x++ ) {
                int currentSlice = slice.getSlice();

                int blockId;
                double alphaFactor = 1;
                double darkenFactor = 1;
                do {
                    position.setSlice(x, y, currentSlice);
                    blockId     = slice.getPixel(position);
                    if( blockId==-1 ) {
                        break;
                    }
                    Color color = c.getColors().getColorForBlock(blockId);
                    if( color.getAlpha()==255 ) {
                        drawBlock(c, color, darkenFactor, alphaFactor, position);
                        break;
                    } else if( color.getAlpha()>0 ) {
                        drawBlock(c, color, darkenFactor, alphaFactor, position);
                        alphaFactor *= (255.-color.getAlpha())/255.;
                    }
                    currentSlice--;
                    darkenFactor *= 0.92;
                } while( slice.getSlice()-currentSlice<20 );

/*                if( blueprint==1 ) {
                    if( colorForBlock==null ) {
                        colorForBlock = c.getColors().getColorForBlock(DataValues.AIR.getId());
                    }
                    colorForBlock = colorForBlock.darker().darker().darker();
                }
*/
            }
        }
    }

    protected void renderChunk() {

    }

    protected void drawBlock( RenderContext c, Color color, double darken, double alpha, Position position ) {
        Graphics2D g  = c.getGraphics();

        Color col = new Color((int)(darken*color.getRed()), (int)(darken*color.getGreen()), (int)(darken*color.getBlue()), (int)(alpha * color.getAlpha()) );
        g.setColor(col);
        int x = position.getScreenX();
        int y = position.getScreenY();
        int w = c.screenUnitX(1);
        int h = c.screenUnitY(1);

        g.fillRect( x,y, w, h);

    }
}
