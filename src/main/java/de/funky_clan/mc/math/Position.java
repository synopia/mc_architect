package de.funky_clan.mc.math;

import de.funky_clan.mc.model.RenderContext;
import de.funky_clan.mc.model.Slice;
import de.funky_clan.mc.model.SliceType;

import java.awt.*;

/**
 * @author synopia
 */
public final class Position {
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

    public final void setWorld(double worldX, double worldY, double worldZ) {
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

    public final void setSlice(double sliceX, double sliceY, int sliceNo) {
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

    public final void setScreen( int screenX, int screenY) {
        setScreen(screenX, screenY, sliceNo);
    }

    public final void setScreen( int screenX, int screenY, int sliceNo ) {
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

    public final int getScreenX() {
        return screenX;
    }

    public final int getScreenY() {
        return screenY;
    }

    public final double getSliceX() {
        return sliceX;
    }

    public final double getSliceY() {
        return sliceY;
    }

    public final int getSliceNo() {
        return sliceNo;
    }

    public final double getWorldX() {
        return worldX;
    }

    public final double getWorldY() {
        return worldY;
    }

    public final double getWorldZ() {
        return worldZ;
    }

    public final int getBlockX() {
        return (int) worldX;
    }

    public final int getBlockY() {
        return (int) worldY;
    }

    public final int getBlockZ() {
        return (int) worldZ;
    }

    public final void sliceToWorld( SliceType type ) {
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

    public final double distToSlice() {
        return sliceNo - slice.getSlice();
    }

    public final Color fadeOut( Color color ) {
        double damp = Math.max(0, Math.min(1, (3 / Math.abs(distToSlice()))));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(255*damp));
    }

    public final void worldToSlice( SliceType type ) {
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

    public final void setRenderContext(RenderContext rc) {
        this.rc = rc;
    }

    public final void setSlice(Slice slice) {
        this.slice = slice;
    }

    public final void setSliceNo(int currentSlice) {
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
