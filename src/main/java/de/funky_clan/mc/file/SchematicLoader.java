package de.funky_clan.mc.file;

import com.google.inject.Singleton;
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
    public void load(Graphics g, String filename, int mid_x, int mid_y, int mid_z) {
        try {
            InputStream fileInput = getClass().getResourceAsStream( "/" + filename );
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
            int startY = mid_y - sizeY/2;
            int startZ = mid_z - sizeZ/2;

            ByteArrayTag byteArrayTag = (ByteArrayTag) root.getValue().get("Blocks");
            byte[] blocks = byteArrayTag.getValue();
            for( int x = 0; x < sizeX; x++ ) {
                for( int y = 0; y < sizeY; y++ ) {
                    for( int z = 0; z < sizeZ; z++ ) {
                        int   i     = x + ( y*sizeZ+z ) * sizeX;

                        g.setPixel(startX+x, startY+y, startZ+z, blocks[i]);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
