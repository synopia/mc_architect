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
    private int imageWidth;
    private int imageHeight;
    private Image scaledImage;

    public BackgroundImage(String filename) {
        try {
            File file = new File(filename);
            image = ImageIO.read(file);
            imageWidth = image.getWidth(null);
            imageHeight = image.getHeight(null);
            scaledImage = image;
        } catch (IOException e) {
            e.printStackTrace();
            image = null;
        }
    }

    @Override
    public void render(RenderContext c) {
        if (image != null) {
            int width = c.getWindowWidth();
            int height = c.getWindowHeight();

            if (width != imageWidth || height != imageHeight) {
                scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                imageWidth = width;
                imageHeight = height;
            }
            c.getGraphics().drawImage(scaledImage, 0, 0, null);
        }
    }
}
