package de.funky_clan.mc.model;

import java.awt.*;

/**
 * @author synopia
 */
public class Slice implements Renderable {
    private int width;
    private int height;
    private int level;
    private BackgroundImage image;

    private Model     model;
    private SliceType type;
    public enum SliceType {
        X, // y-z
        Y, // x-z
        Z, // x-y
    }

    public Slice(Model model, SliceType type, int level) {
        this.model = model;
        this.type  = type;
        this.level = level;

        setWidthAndHeight();
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

    public void setPixel(int x, int y, int value) {
        int mapX=-1, mapY=-1, mapZ=-1;

        switch( type ) {
            case X:
                mapX = level;
                mapZ = y;
                mapY = x;
                break;
            case Y:
                mapY = level;
                mapZ = y;
                mapX = x;
                break;
            case Z:
                mapZ = level;
                mapX = x;
                mapY = y;
                break;
        }
        model.setPixel( mapX, mapY, mapZ, value );
    }

    public int getPixel(int x, int y) {
        int mapX=-1, mapY=-1, mapZ=-1;

        switch( type ) {
            case X:
                mapX = level;
                mapZ = y;
                mapY = x;
                break;
            case Y:
                mapY = level;
                mapZ = y;
                mapX = x;
                break;
            case Z:
                mapZ = level;
                mapX = x;
                mapY = y;
                break;
        }
        return model.getPixel(mapX, mapY, mapZ);
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

}
