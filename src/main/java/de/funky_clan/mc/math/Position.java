package de.funky_clan.mc.math;

import de.funky_clan.mc.model.RenderContext;
import de.funky_clan.mc.model.Slice;
import de.funky_clan.mc.model.SliceType;

import java.awt.*;

/**
 * @author synopia
 */
public class Position {
    private int screenX;
    private int screenY;
    private double sliceX;
    private double sliceY;
    private int sliceNo;
    private double worldX;
    private double worldY;
    private double worldZ;

    private RenderContext rc;
    private Slice slice;

    public Position() {
    }

    public Position(int screenX, int screenY) {
        setScreen(screenX, screenY);
    }

    public Position(double sliceX, double sliceY, int sliceNo) {
        setSlice(sliceX, sliceY, sliceNo);
    }

    public Position(double worldX, double worldY, double worldZ) {
        setWorld(worldX, worldY, worldZ);
    }

    public void setWorld(double worldX, double worldY, double worldZ) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.worldZ = worldZ;

        if( slice!=null ) {
            worldToSlice(slice.getType());

            if( rc!=null ) {
                screenX = rc.sliceToScreenX( sliceX );
                screenY = rc.sliceToScreenY( sliceY);
            }
        }
    }

    public void setSlice(double sliceX, double sliceY, int sliceNo) {
        this.sliceX = sliceX;
        this.sliceY = sliceY;
        this.sliceNo = sliceNo;

        if( rc!=null ) {
            screenX = rc.sliceToScreenX( sliceX );
            screenY = rc.sliceToScreenY( sliceY);
        }
        if( slice!=null ) {
            sliceToWorld(slice.getType());
        }
    }

    public void setScreen( int screenX, int screenY) {
        setScreen(screenX, screenY, sliceNo);
    }

    public void setScreen( int screenX, int screenY, int sliceNo ) {
        this.screenX = screenX;
        this.screenY = screenY;
        this.sliceNo = sliceNo;

        if( rc!=null ) {
            sliceX = rc.screenToSliceX( screenX );
            sliceY = rc.screenToSliceY( screenY );

            if( slice!=null ) {
                sliceToWorld(slice.getType());
            }
        }
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public double getSliceX() {
        return sliceX;
    }

    public double getSliceY() {
        return sliceY;
    }

    public int getSliceNo() {
        return sliceNo;
    }

    public double getWorldX() {
        return worldX;
    }

    public double getWorldY() {
        return worldY;
    }

    public double getWorldZ() {
        return worldZ;
    }

    public int getBlockX() {
        return (int) worldX;
    }

    public int getBlockY() {
        return (int) worldY;
    }

    public int getBlockZ() {
        return (int) worldZ;
    }

    public void sliceToWorld( SliceType type ) {
        switch (type) {
            case X:
                worldX = sliceNo;
                worldY = -sliceY;
                worldZ = sliceX;
                break;
            case Y:
                worldX = sliceX;
                worldY = -sliceY;
                worldZ = sliceNo;
                break;
            case Z:
                worldX = sliceX;
                worldY = sliceNo;
                worldZ = sliceY;
                break;
        }
    }

    public double distToSlice() {
        return sliceNo - slice.getSlice();
    }

    public Color fadeOut( Color color ) {
        double damp = Math.max(0, Math.min(1, (1 / Math.abs(distToSlice()))));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(255*damp));
    }

    public void worldToSlice( SliceType type ) {
        switch (type) {
            case X:
                sliceX = worldZ;
                sliceY = -worldY;
                sliceNo = (int) worldX;
                break;
            case Y:
                sliceX = worldX;
                sliceY = -worldY;
                sliceNo = (int) worldZ;
                break;
            case Z:
                sliceX = worldX;
                sliceY = worldZ;
                sliceNo = (int) worldY;
                break;
        }
    }

    public void setRenderContext(RenderContext rc) {
        this.rc = rc;
    }

    public void setSlice(Slice slice) {
        this.slice = slice;
    }

    public void setSliceNo(int currentSlice) {
        this.sliceNo = currentSlice;
    }

    @Override
    public String toString() {
        return "Position{" +
                "screenX=" + screenX +
                ", screenY=" + screenY +
                ", sliceX=" + sliceX +
                ", sliceY=" + sliceY +
                ", sliceNo=" + sliceNo +
                ", worldX=" + worldX +
                ", worldY=" + worldY +
                ", worldZ=" + worldZ +
                ", rc=" + rc +
                ", slice=" + slice +
                '}';
    }
}
