package de.funky_clan.mc.file;

import com.google.inject.Singleton;
import de.funky_clan.mc.config.DataValues;
import de.funky_clan.mc.scripts.Graphics;
import org.jnbt.ByteArrayTag;
import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.ShortTag;
import org.jnbt.Tag;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * @author synopia
 */
@Singleton
public class SchematicLoader {
    public static class Schematic {
        private int sizeX;
        private int sizeY;
        private int sizeZ;
        private byte[] blocks;
        
        public void inject( Graphics g, int mid_x, int mid_y, int mid_z, int dir) {
            int startX;
            int startY = mid_y;
            int startZ;
            switch (dir) {
                case 0:
                    startX = mid_x - sizeX/2;
                    startZ = mid_z - sizeZ/2;
                break;
                case 1:
                    startX = mid_x - sizeZ/2;
                    startZ = mid_z + sizeX/2;
                    break;
                case 2:
                    startX = mid_x + sizeX/2;
                    startZ = mid_z + sizeZ/2;
                    break;
                case 3:
                    startX = mid_x + sizeZ/2;
                    startZ = mid_z - sizeX/2;
                    break;
                default:
                    throw new IllegalArgumentException("dir must be between 0 and 3");
            }
            
            for( int x = 0; x < sizeX; x++ ) {
                for( int y = 0; y < sizeY; y++ ) {
                    for( int z = 0; z < sizeZ; z++ ) {
                        int   i     = x + ( y*sizeZ+z ) * sizeX;
                        byte value = blocks[i];
                        if(value>0) {
                            int tx = 0;
                            int ty = y+startY;
                            int tz = 0;
                            switch (dir) {
                                case 0:
                                    tx = startX+x;
                                    tz = startZ+z;
                                    break;
                                case 1:
                                    tx = startX+z;
                                    tz = startZ-x;
                                    break;
                                case 2:
                                    tx = startX-x;
                                    tz = startZ-z;
                                    break;
                                case 3:
                                    tx = startX-z;
                                    tz = startZ+x;
                                    break;
                            }
                            g.setPixel(tx, ty, tz, value);
                        }
                    }
                }
            }
        }
    }
    
    public Schematic load( String filename ) {
        try {
            InputStream fileInput;
            fileInput = getClass().getResourceAsStream( "/" + filename );
            if( fileInput==null ) {
                fileInput = new FileInputStream(filename);
            }
            Schematic schematic = new Schematic();
            
            DataInputStream in = new DataInputStream( new GZIPInputStream( fileInput ));
            NBTInputStream nbt    = new NBTInputStream( in );
            CompoundTag root   = (CompoundTag) nbt.readTag();
            ShortTag width = (ShortTag) root.getValue().get("Width");
            ShortTag length = (ShortTag) root.getValue().get("Length");
            ShortTag height = (ShortTag) root.getValue().get("Height");
            ByteArrayTag byteArrayTag = (ByteArrayTag) root.getValue().get("Blocks");
            schematic.sizeX = width.getValue();
            schematic.sizeZ = length.getValue();
            schematic.sizeY = height.getValue();
            schematic.blocks = byteArrayTag.getValue();
            
            return schematic;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public void load(Graphics g, String filename, int mid_x, int mid_y, int mid_z, int dirX, int dirY, int dirZ) {
        try {
            InputStream fileInput;
            fileInput = getClass().getResourceAsStream( "/" + filename );
            if( fileInput==null ) {
                fileInput = new FileInputStream(filename);
            }
            DataInputStream in = new DataInputStream( new GZIPInputStream( fileInput ));
            NBTInputStream nbt    = new NBTInputStream( in );
            CompoundTag root   = (CompoundTag) nbt.readTag();
            ShortTag width = (ShortTag) root.getValue().get("Width");
            ShortTag length = (ShortTag) root.getValue().get("Length");
            ShortTag height = (ShortTag) root.getValue().get("Height");
            int sizeX = width.getValue();
            int sizeZ = length.getValue();
            int sizeY = height.getValue();

            int startX = mid_x - sizeX/2;
            int startY = mid_y;
            int startZ = mid_z - sizeZ/2;

            ByteArrayTag byteArrayTag = (ByteArrayTag) root.getValue().get("Blocks");
            byte[] blocks = byteArrayTag.getValue();
            for( int x = 0; x < sizeX; x++ ) {
                for( int y = 0; y < sizeY; y++ ) {
                    for( int z = 0; z < sizeZ; z++ ) {
                        int   i     = x + ( y*sizeZ+z ) * sizeX;
                        if( blocks[i]>0 && blocks[i]!= DataValues.GLASS.getId() && blocks[i]!=DataValues.GLASS_PANE.getId()) {
                            int xx = x;
                            int yy = y;
                            int zz = z;
                            if( dirX<0 ){
                                xx = sizeX-x-1;
                            }
                            if( dirY<0 ) {
                                yy = sizeY-y-1;
                            }
                            if( dirZ<0 ) {
                                zz = sizeZ-z-1;
                            }
                            g.setPixel(startX+xx, startY+yy, startZ+zz, blocks[i]);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
