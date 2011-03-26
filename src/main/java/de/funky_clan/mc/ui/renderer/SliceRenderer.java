package de.funky_clan.mc.ui.renderer;

import com.google.inject.Inject;
import com.sun.corba.se.spi.monitoring.LongMonitoredAttributeBase;
import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.eventbus.EventHandler;
import de.funky_clan.mc.eventbus.ModelEventBus;
import de.funky_clan.mc.events.model.ModelUpdate;
import de.funky_clan.mc.math.Position;
import de.funky_clan.mc.model.Chunk;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.RenderContext;
import de.funky_clan.mc.model.Slice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author synopia
 */
public class SliceRenderer implements Renderer<Slice> {
    private final Logger logger = LoggerFactory.getLogger(SliceRenderer.class);

    @Inject
    private Model model;
    @Inject
    private Colors colors;

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
        int chunkStartX = pos.getBlockX()>>4;
        int chunkStartZ = pos.getBlockZ()>>4;
        int endY = pos.getBlockY();
        pos.setSlice(ex,ey,slice.getSlice());
        int chunkEndX = pos.getBlockX()>>4;
        int chunkEndZ = pos.getBlockZ()>>4;
        int startY = pos.getBlockY();
//        logger.info(String.format("%d, %d, %d -> %d, %d, %d", chunkStartX, startY, chunkStartZ, chunkEndX, endY, chunkEndZ));
        assert chunkStartX<=chunkEndX;
        assert chunkStartZ<=chunkEndZ;
        assert startY<=endY;

        int chunkSizeX = chunkEndX-chunkStartX;
        int chunkSizeZ = chunkEndZ-chunkStartZ;

        startY = Math.max(0, Math.min(127, startY) );
        endY   = Math.max(0, Math.min(127, endY));

        for(int chunkX=chunkStartX; chunkX<=chunkEndX; chunkX++ ) {
            for( int chunkZ=chunkStartZ; chunkZ<=chunkEndZ; chunkZ++ ) {
                renderChunk( c, pos, pos.getBlockX(), pos.getBlockZ(), chunkX, chunkZ, chunkSizeX, chunkSizeZ, startY, endY );
            }
        }

    }

    protected void renderChunk(RenderContext c, Position pos, int worldStartX, int worldStartZ, int chunkX, int chunkZ, int chunkSizeX, int chunkSizeZ, int startY, int endY) {
        Chunk chunk = model.getChunk(chunkX, chunkZ);
        if( chunk==null ) {
            return;
        }

        if( startY==endY ) {
            for( int x=0; x<16; x++ ) {
                for( int z=0; z<16; z++ ) {
                    int wx = x + chunk.getStartX();
                    int wz = z + chunk.getStartZ();

                    renderBlock(c, pos, chunk, wx, startY, wz, true);
                }
            }
        } else if( chunkSizeX==0 ) {
            for( int z=0; z<16; z++ ) {
                for( int y=startY; y<endY; y++ ) {
                    int wz = z + chunk.getStartZ();

                    renderBlock(c, pos, chunk, worldStartX, y, wz, false);
                }
            }

        } else if( chunkSizeZ==0 ) {
            for( int x=0; x<16; x++ ) {
                for( int y=startY; y<endY; y++ ) {
                    int wx = x + chunk.getStartX();

                    renderBlock(c, pos, chunk, wx, y, worldStartZ, false);
                }
            }

        }
    }

    private void renderBlock(RenderContext c, Position pos, Chunk chunk, int wx, int wy, int wz, boolean alpha ) {
        int blockId = chunk.getPixel(wx, wy, wz, 0);

        if( blockId==-1 ) {
            return;
        }
        Color color = findColor( chunk, wx, wy, wz, alpha );
        if( color!=null ) {
            pos.setWorld(wx, wy, wz);
            drawBlock(c, color, pos);
        }

        if( chunk.getPixel(wx, wy, wz, 1)>0 ) {
            pos.setWorld(wx, wy, wz);
            Color blueprint = colors.getBlueprintColor();
            drawRect(c,  blueprint, pos );
        }
    }

    protected Color findColor( Chunk chunk, int wx, int wy, int wz, boolean useAlpha ) {
        int x = wx-chunk.getStartX();
        int z = wz-chunk.getStartZ();

        int offset = (z *128) + (x *128*16);
        byte[] map = chunk.getMap();
        Color result = null;

        if( !useAlpha ) {
            int blockId = map[offset + wy];
            Color color = colors.getColorForBlock(blockId);
            if( color.getAlpha()>0 ) {
                result = color;
            }
        } else {
            float ax = 0;
            float rx = 0;
            float gx = 0;
            float bx = 0;
            int depth = 0;
            for( int y=wy; y>0; y-- ) {
                int blockId = map[offset + y];
                Color color = colors.getColorForBlock(blockId);

                if( color.getAlpha()==255 ) {
                    depth = y;
                    ax = 1;
                    rx = color.getRed()/256.f;
                    gx = color.getGreen()/256.f;
                    bx = color.getBlue()/256.f;
                    break;
                }
            }

            for( int y=depth+1; y<=wy; y++ ) {
                int blockId = map[offset + y];
                Color color = colors.getColorForBlock(blockId);
                float A = ax;
                float R = rx;
                float G = gx;
                float B = bx;
                float a = color.getAlpha()/256.f;
                float r = color.getRed()/256.f ;
                float g = color.getGreen()/256.f;
                float b = color.getBlue()/256.f;

                ax = 1- (1-a)*(1-A);
                rx = r * a/ax + R*A*(1-a)/ax;
                gx = g * a/ax + G*A*(1-a)/ax;
                bx = b * a/ax + B*A*(1-a)/ax;
            }
            result = new Color(rx,gx,bx,ax);
        }

        return result;
    }

    protected void drawBlock( RenderContext c, Color color, Position position) {
        Graphics2D g  = c.getGraphics();

        g.setColor(color);
        int x = position.getScreenX();
        int y = position.getScreenY();
        int w = position.getScreenUnitX();
        int h = position.getScreenUnitY();

        g.fillRect( x,y, w, h);
    }

    protected void drawBlock( RenderContext c, Color color, double darken, double alpha, Position position) {
        Graphics2D g  = c.getGraphics();

        Color col = new Color((int)(darken*color.getRed()), (int)(darken*color.getGreen()), (int)(darken*color.getBlue()), (int)(alpha * color.getAlpha()) );
        g.setColor(col);
        int x = position.getScreenX();
        int y = position.getScreenY();
        int w = position.getScreenUnitX();
        int h = position.getScreenUnitY();

        g.fillRect( x,y, w, h);
    }

    protected void drawRect( RenderContext c, Color blueprint, Position position ) {
        Graphics2D g  = c.getGraphics();

        int x = position.getScreenX();
        int y = position.getScreenY();
        int w = position.getScreenUnitX();
        int h = position.getScreenUnitY();
        g.setColor(blueprint);
        g.drawRect( x,y, w-1, h-1);
    }
}
