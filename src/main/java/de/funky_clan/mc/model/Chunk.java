package de.funky_clan.mc.model;

import de.funky_clan.mc.math.Point3i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author synopia
 */
public class Chunk {
    private int                               map[][][];
    private Point3i size;
    private Point3i start;

    private Logger logger = LoggerFactory.getLogger(Chunk.class);

    public static Chunk EMPTY = new Chunk(new Point3i(0,0,0), new Point3i(1<<4, 1<<7, 1<<4)) {

        @Override
        public void setPixelLocal(Point3i pos, PixelType type, int value) {
        }

        @Override
        public int getPixelLocal(Point3i pos, PixelType type) {
            return -1;
        }

        @Override
        public void setPixelGlobal(Point3i pos, PixelType type, int value) {
        }

        @Override
        public int getPixelGlobal(Point3i pos, PixelType type) {
            return -1;
        }
    };

    public Chunk(Point3i start, Point3i size) {
        logger.info("Creating chunk "+start.x()+", "+start.y()+", "+start.z());
        this.start = start;
        this.size = size;

        map        = new int[size.z()][][];

        for( int z = 0; z < size.z(); z++ ) {
            map[z] = new int[size.y()][];

            for( int y = 0; y < size.y(); y++ ) {
                map[z][y] = new int[size.x()];
            }
        }
    }

    public Point3i getSize() {
        return size;
    }

    public boolean isInRange( int x, int y, int z ) {
        return (x >= 0) && (y >= 0) && (z >= 0) && (x < size.x()) && (y < size.y()) && (z < size.z());
    }

    public void setPixelLocal(Point3i pos, PixelType type, int value) {
        int x = pos.x();
        int y = pos.y();
        int z = pos.z();
        if( isInRange( x, y, z )) {
            map[z][y][x] = type.set(map[z][y][x], value);
        }
    }

    public int getPixelLocal(Point3i pos, PixelType type) {
        int x = pos.x();
        int y = pos.y();
        int z = pos.z();
        int result = -1;

        if( isInRange( x, y, z )) {
            result = type.get(map[z][y][x]);
        }

        return result;
    }

    public void setPixelGlobal(Point3i pos, PixelType type, int value) {
        setPixelLocal( pos.sub(start), type, value );
    }

    public int getPixelGlobal(Point3i pos, PixelType type) {
        return getPixelLocal(pos.sub(start), type);
    }

}
