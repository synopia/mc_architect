package de.funky_clan.mc.ui.renderer;

import com.google.inject.Inject;
import de.funky_clan.mc.config.Colors;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.math.Position;
import de.funky_clan.mc.model.Chunk;
import de.funky_clan.mc.model.Model;
import de.funky_clan.mc.model.RenderContext;
import de.funky_clan.mc.model.Slice;
import de.funky_clan.mc.model.SliceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.HashMap;

/**
 * @author synopia
 */
public class SliceRenderer implements Renderer<Slice> {
    private final float[]                      blueprint  = new float[4];
    private float                              color[]    = new float[4];
    private final int[]                        colori     = new int[4];
    private final Logger                       logger     = LoggerFactory.getLogger( SliceRenderer.class );
    private final HashMap<Long, BufferedImage> imageCache = new HashMap<Long, BufferedImage>();
    private boolean                            cacheValid = false;
    @Inject
    private Colors                             colors;
    @Inject
    private Model                              model;

    public void invalidate() {
        cacheValid = false;
    }

    @Override
    public void render( Slice slice, RenderContext c ) {
        int sx = c.getWindowStartX() - 1;
        int sy = c.getWindowStartY() - 1;
        int ex = c.getWindowEndX() + 1;
        int ey = c.getWindowEndY() + 1;

        if( slice.getSlice() == -999 ) {
            return;
        }

        Position pos = c.getPosition();

        pos.setSlice( sx, sy, slice.getSlice() );

        int chunkStartX = pos.getBlockX() >> 4;
        int chunkStartZ = pos.getBlockZ() >> 4;
        int endY        = pos.getBlockY();

        pos.setSlice( ex, ey, slice.getSlice() );

        int chunkEndX = pos.getBlockX() >> 4;
        int chunkEndZ = pos.getBlockZ() >> 4;
        int startY    = pos.getBlockY();

        assert chunkStartX <= chunkEndX;
        assert chunkStartZ <= chunkEndZ;
        assert startY <= endY;

        SliceType sliceType = slice.getType();

        startY = Math.max( 0, Math.min( 127, startY ));
        endY   = Math.max( 0, Math.min( 127, endY ));

        if(( sliceType == SliceType.Y ) && (( slice.getSlice() < 0 ) || ( slice.getSlice() > 127 ))) {
            return;
        }

        Color bc = colors.getColorForBlock( DataValues.BLUEPRINT.getId() );

        blueprint[0] = bc.getRed() / 255.f;
        blueprint[1] = bc.getGreen() / 255.f;
        blueprint[2] = bc.getBlue() / 255.f;
        blueprint[3] = bc.getAlpha() / 255.f;

        for( int chunkX = chunkStartX; chunkX <= chunkEndX; chunkX++ ) {
            for( int chunkZ = chunkStartZ; chunkZ <= chunkEndZ; chunkZ++ ) {
                Chunk chunk = model.getChunk( chunkX, chunkZ );

                if( chunk != null ) {
                    BufferedImage image;
                    boolean       newImage = true;

                    if( imageCache.containsKey( chunk.getId() )) {
                        image    = imageCache.get( chunk.getId() );
                        newImage = false;
                    } else {
                        image = new BufferedImage( 16, ( sliceType == SliceType.Y )
                                                       ? 16
                                                       : 128, BufferedImage.TYPE_INT_ARGB );
                        imageCache.put( chunk.getId(), image );
                    }

                    if( newImage || !cacheValid ) {
                        renderChunkSlice( image, chunk, sliceType, slice.getSlice() );
                    }

                    drawImage( c, image, chunk, pos, sliceType, startY, endY );
                }
            }
        }

        cacheValid = true;
    }

    protected void renderChunkSlice( BufferedImage image, Chunk chunk, SliceType sliceType, int sliceNo ) {
        WritableRaster raster = image.getRaster();
        int            offset;

        switch( sliceType ) {
        case Y:
            for( int x = 0; x < 16; x++ ) {
                for( int z = 0; z < 16; z++ ) {
                    renderBlock( raster, chunk, x, sliceNo, z, x, z, true );
                }
            }

            break;

        case X:
            offset = sliceNo - chunk.getStartX();

            for( int z = 0; z < 16; z++ ) {
                for( int y = 0; y < 128; y++ ) {
                    renderBlock( raster, chunk, offset, y, z, z, y, false );
                }
            }

            break;

        case Z:
            offset = sliceNo - chunk.getStartZ();

            for( int x = 0; x < 16; x++ ) {
                for( int y = 0; y < 128; y++ ) {
                    renderBlock( raster, chunk, x, y, offset, x, y, false );
                }
            }

            break;
        }
    }

    protected void drawImage( RenderContext c, Image image, Chunk chunk, Position pos, SliceType sliceType, int startY,
                              int endY ) {
        int sx;
        int sy;
        int ex;
        int ey;

        switch( sliceType ) {
        case Y:
            pos.setWorld( chunk.getStartX(), startY, chunk.getStartZ() );
            sx = pos.getScreenX();
            sy = pos.getScreenY();
            pos.setWorld( chunk.getStartX() + 16, endY, chunk.getStartZ() + 16 );
            ex = pos.getScreenX();
            ey = pos.getScreenY();
            c.getGraphics().drawImage( image, sx, sy, ex, ey, 0, 0, 16, 16, null );

            break;

        case X:
            pos.setWorld( chunk.getStartX(), startY, chunk.getStartZ() );
            sx = pos.getScreenX();
            sy = pos.getScreenY();
            pos.setWorld( chunk.getStartX(), endY, chunk.getStartZ() + 16 );
            ex = pos.getScreenX();
            ey = pos.getScreenY();
            c.getGraphics().drawImage( image, sx, sy, ex, ey, 0, startY, 16, endY, null );

            break;

        case Z:
            pos.setWorld( chunk.getStartX(), startY, chunk.getStartZ() );
            sx = pos.getScreenX();
            sy = pos.getScreenY();
            pos.setWorld( chunk.getStartX() + 16, endY, chunk.getStartZ() );
            ex = pos.getScreenX();
            ey = pos.getScreenY();
            c.getGraphics().drawImage( image, sx, sy, ex, ey, 0, startY, 16, endY, null );

            break;
        }
    }

    private void renderBlock( WritableRaster raster, Chunk chunk, int cx, int cy, int cz, int pixelX, int pixelY,
                              boolean useAlpha ) {
        byte[]  map    = chunk.getMap();
        int     offset = ( cz * 128 ) + ( cx * 128 * 16 );
        float[] color  = null;

        if( map[offset + cy] >= 0 ) {
            color = findColor( chunk, cx, cy, cz, useAlpha );
        }

        if(( map[offset + cy + Chunk.CHUNK_ARRAY_SIZE] > 0 ) && ( map[offset + cy] == 0 )) {
            color = blueprint;
        }

        if( color != null ) {
            colori[0] = (int) ( color[0] * 255 );
            colori[1] = (int) ( color[1] * 255 );
            colori[2] = (int) ( color[2] * 255 );
            colori[3] = (int) ( color[3] * 255 );
            raster.setPixel( pixelX, pixelY, colori );
        }
    }

    protected float[] findColor( Chunk chunk, int x, int wy, int z, boolean useAlpha ) {
        int     offset = ( z * 128 ) + ( x * 128 * 16 );
        byte[]  map    = chunk.getMap();
        float[] result = null;

        if( !useAlpha ) {
            int blockId = map[offset + wy];

            color = colors.getColorForBlock( blockId, color );

            if( color[3] > 0 ) {
                result = color;
            }
        } else {
            float ax    = 0;
            float rx    = 0;
            float gx    = 0;
            float bx    = 0;
            int   depth = 0;

            for( int y = wy; y > 0; y-- ) {
                int blockId = map[offset + y];

                color = colors.getColorForBlock( blockId, color );

                if( color[3] == 1 ) {
                    depth = y;
                    ax    = 1;
                    rx    = color[0];
                    gx    = color[1];
                    bx    = color[2];

                    break;
                }
            }

            for( int y = depth + 1; y <= wy; y++ ) {
                int blockId = map[offset + y];

                color = colors.getColorForBlock( blockId, color );

                float A = ax;
                float R = rx;
                float G = gx;
                float B = bx;
                float a = color[3];
                float r = color[0];
                float g = color[1];
                float b = color[2];

                ax = 1 - ( 1 - a ) * ( 1 - A );
                rx = r * a / ax + R * A * ( 1 - a ) / ax;
                gx = g * a / ax + G * A * ( 1 - a ) / ax;
                bx = b * a / ax + B * A * ( 1 - a ) / ax;
            }

            result    = color;
            result[0] = rx;
            result[1] = gx;
            result[2] = bx;
            result[3] = ax;
        }

        return result;
    }
}
