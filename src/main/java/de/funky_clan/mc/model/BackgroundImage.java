package de.funky_clan.mc.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author synopia
 */
public class BackgroundImage implements Renderable {
    private Image image;

    public BackgroundImage(String filename) {
        try {
            File file = new File(filename);
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            image = null;
        }
    }

    @Override
    public void render(RenderContext c) {
        if (image != null) {
            int width  = c.getScreenWidth();
            int height = c.getScreenHeight();

            // todo is clipping rectangle applied to drawImage? is manual clipping required?
            c.getGraphics().drawImage(image, 0, 0, width, height, null);
        }
    }
}
