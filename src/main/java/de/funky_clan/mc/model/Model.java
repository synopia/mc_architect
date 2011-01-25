package de.funky_clan.mc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author synopia
 */
public class Model {
    private int width;
    private int height;
    private List<Slice> slices;

    public Model(int width, int height, int numberOfSlices) {
        this.width = width;
        this.height = height;
        slices = new ArrayList<Slice>(numberOfSlices);

        for (int i = 0; i < numberOfSlices; i++) {
            slices.add(new Slice(width, height, i));
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSlices() {
        return slices.size();
    }

    public Slice getSlice(int slice) {
        return slices.get(slice % getSlices());
    }
}
