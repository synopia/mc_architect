package de.funky_clan.mc.model;

/**
 * Contains methods to "draw" into the model
 *
 * @author synopia
 */
public class Graphics {
    private Model model;

    public Graphics(Model model) {
        this.model = model;
    }

    public void ellipse(int level, int xm, int ym, int width, int height, int type) {
        Slice slice = model.getSlice(level);

        int a = width;
        int b = height;
        int dx = 0;
        int dy = b;
        int a2 = a * a;
        int b2 = b * b;
        int err = b2 - (2 * b - 1) * a2;
        int e2 = 0;

        do {
            slice.setPixel(xm + dx, ym + dy, type);
            slice.setPixel(xm - dx, ym + dy, type);
            slice.setPixel(xm - dx, ym - dy, type);
            slice.setPixel(xm + dx, ym - dy, type);

            e2 = 2 * err;
            if (e2 < (2 * dx + 1) * b2) {
                dx++;
                err += (2 * dx + 1) * b2;
            }
            if (e2 > -(2 * dy - 1) * a2) {
                dy--;
                err -= (2 * dy - 1) * a2;
            }
        } while (dy >= 0);

        while (dx + 1 < a) {
            dx++;
            slice.setPixel(xm + dx, ym, type);
            slice.setPixel(xm - dx, ym, type);
        }
    }

    public void hLine(int x, int y, int width, int level, int type) {
        Slice slice = model.getSlice(level);
        for (int i = 0; i < width; i++) {
            slice.setPixel(x + i, y, type);
        }
    }

    public void vLine(int x, int y, int height, int level, int type) {
        Slice slice = model.getSlice(level);
        for (int i = 0; i < height; i++) {
            slice.setPixel(x, y + i, type);
        }
    }

    public void hLine(int x, int y, int width, int type) {
        for (int level = 0; level < model.getSlices(); level++) {
            hLine(x, y, width, level, type);
        }
    }

    public void vLine(int x, int y, int width, int type) {
        for (int level = 0; level < model.getSlices(); level++) {
            vLine(x, y, width, level, type);
        }
    }
}
