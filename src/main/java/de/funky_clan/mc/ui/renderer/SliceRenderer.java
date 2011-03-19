package de.funky_clan.mc.ui.renderer;

import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.math.Point2d;
import de.funky_clan.mc.math.Point2i;
import de.funky_clan.mc.model.PixelType;
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
        int        sx = c.getWindowStart().x() - 1;
        int        sy = c.getWindowStart().y() - 1;
        int        ex = c.getWindowEnd().x() + 2;
        int        ey = c.getWindowEnd().y() + 2;

        for( int y = sy; y < ey; y++ ) {
            for( int x = sx; x < ex; x++ ) {
                Point2i position = new Point2i(x, y);
                Point2d point2d = position.toPoint2d();
                int blueprint = slice.getPixel(position, PixelType.BLUEPRINT);

                int currentSlice = slice.getSlice();
                int blockId;
                double alphaFactor = 1;
                do {
                    blockId     = slice.getPixel(position, currentSlice, PixelType.BLOCK_ID );
                    if( blockId==-1 ) {
                        break;
                    }
                    Color color = c.getColors().getColorForBlock(blockId);
                    if( color.getAlpha()==255 ) {
                        drawBlock(c, color, 1, alphaFactor, point2d);
                        break;
                    } else if( color.getAlpha()>0 ) {
                        drawBlock(c, color, 1, alphaFactor, point2d);
                        alphaFactor *= (255.-color.getAlpha())/255.;
                    }
                    currentSlice--;
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

    protected void drawBlock( RenderContext c, Color color, double darken, double alpha, Point2d position ) {
        Graphics2D g  = c.getGraphics();

        Color col = new Color((int)(darken*color.getRed()), (int)(darken*color.getGreen()), (int)(darken*color.getBlue()), (int)(alpha * color.getAlpha()) );
        g.setColor(col);
        Point2i curr = c.sliceToScreen(position);
        Point2i size = c.screenUnit(position);

        g.fillRect( curr.x(), curr.y(), size.x(), size.y());

    }
}
