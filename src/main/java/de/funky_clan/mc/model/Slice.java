package de.funky_clan.mc.model;

import java.awt.*;

/**
 * @author synopia
 */
public class Slice implements Renderable {
    private int width;
    private int height;
    private int slice;
    private BackgroundImage image;

    private Model     model;
    private SliceType type;
    public enum SliceType {
        X, // y-z
        Y, // x-z
        Z, // x-y
    }

    public Slice(Model model, SliceType type) {
        this.model = model;
        this.type  = type;

        setWidthAndHeight();
    }

    public void setSlice(int slice) {
        this.slice = slice;
    }

    private void setWidthAndHeight() {
        switch( type ) {
            case X:
                this.width  = model.getSizeY();
                this.height = model.getSizeZ();
                break;
            case Y:
                this.width  = model.getSizeX();
                this.height = model.getSizeZ();
                break;
            case Z:
                this.width  = model.getSizeX();
                this.height = model.getSizeY();
                break;
        }
    }

    public int[] mapSliceToWorld( int x, int y, int slice ) {
        int mapX=-1, mapY=-1, mapZ=-1;

        switch( type ) {
            case X:
                mapX = slice;
                mapZ = height-y;
                mapY = x;
                break;
            case Y:
                mapY = slice;
                mapZ = height-y;
                mapX = x;
                break;
            case Z:
                mapZ = slice;
                mapX = x;
                mapY = y;
                break;
        }
        return new int[]{ mapX, mapY, mapZ };
    }

    public int[] mapWorldToSlice( int x, int y, int z ) {
        int wx = -1, wy = -1, wz = -1;

        switch (getType()) {
            case X:
                wx = y;
                wy = model.getSizeZ()-z;
                wz = x;
                break;
            case Y:
                wx = x;
                wy = model.getSizeZ()-z;
                wz = y;
                break;
            case Z:
                wx = x;
                wy = y;
                wz = z;
                break;
        }
        return new int[]{ wx, wy, wz };
    }

    public int getPixel(int x, int y) {
        int[] map = mapSliceToWorld( x, y, slice );
        return model.getPixel(map[0], map[1], map[2]);
    }

    public BackgroundImage getImage() {
        return image;
    }

    public void setImage(BackgroundImage image) {
        this.image = image;
    }

    public void render(RenderContext context) {
        if (image != null) {
            image.render(context);
        }

        Graphics2D g = context.getGraphics();

        int sx = context.getStartX()-1;
        int sy = context.getStartY()-1;
        int ex = context.getEndX()+2;
        int ey = context.getEndY()+2;

        for (int y = sy; y < ey; y++) {
            for (int x = sx; x < ex; x++) {
                int pixel = getPixel(x, y);
                if (pixel > 0) {

                    g.setColor(context.getColors().getBlockColor());
                    int next_x = context.worldToPixelX(x+1);
                    int next_y = context.worldToPixelY(y+1);
                    int curr_x = context.worldToPixelX(x);
                    int curr_y = context.worldToPixelY(y);

                    g.fillRect(curr_x, curr_y, next_x-curr_x-1, next_y-curr_y-1);
                }
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public SliceType getType() {
        return type;
    }
}
