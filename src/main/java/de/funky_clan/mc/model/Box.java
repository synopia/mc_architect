package de.funky_clan.mc.model;

/**
 * @author synopia
 */
public class Box {
    private double startX;
    private double startY;
    private double startZ;
    private double endX;
    private double endY;
    private double endZ;

    public Box() {
    }

    public void union(double startX, double startY, double startZ, double endX, double endY, double endZ) {
        startX = Math.min(startX, endX);
        startY = Math.min(startY, endY);
        startZ = Math.min(startZ, endZ);
        endX = Math.max(startX, endX);
        endY = Math.max(startY, endY);
        endZ = Math.max(startZ, endZ);
        this.startX = Math.min( this.startX, startX );
        this.startY = Math.min( this.startY, startY );
        this.startZ = Math.min( this.startZ, startZ );
        this.endX = Math.max( this.endX, endX );
        this.endY = Math.max( this.endY, endY );
        this.endZ = Math.max( this.endZ, endZ );
    }
    
    public void set(double startX, double startY, double startZ, double endX, double endY, double endZ) {
        this.startX = Math.min(startX, endX);
        this.startY = Math.min(startY, endY);
        this.startZ = Math.min(startZ, endZ);
        this.endX = Math.max(startX, endX);
        this.endY = Math.max(startY, endY);
        this.endZ = Math.max(startZ, endZ);
    }

    public boolean contains( double worldX, double worldY, double worldZ ) {
        return startX<=worldX && startY<=worldY && startZ<=worldZ && worldX<=endX && worldY<=endY && worldZ<=endZ;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getStartZ() {
        return startZ;
    }

    public void setStartZ(double startZ) {
        this.startZ = startZ;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public double getEndZ() {
        return endZ;
    }

    public void setEndZ(double endZ) {
        this.endZ = endZ;
    }
}
