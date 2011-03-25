package de.funky_clan.mc.scripts;

import com.google.inject.Inject;
import de.funky_clan.mc.config.EventDispatcher;
import de.funky_clan.mc.eventbus.EventBus;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author synopia
 */
public class BinvoxLoader {
    private Pattern header = Pattern.compile("#binvox (\\d+)");
    private Pattern dim = Pattern.compile("dim (\\d+) (\\d+) (\\d+)");
    private Pattern translate = Pattern.compile("translate (-?\\d+.\\d*) (-?\\d+.\\d*) (-?\\d+.\\d*)");
    private Pattern scale = Pattern.compile("scale (-?\\d+.\\d*)");
    private Pattern data = Pattern.compile("data");
    private EventDispatcher eventDispatcher;

    @Inject
    public BinvoxLoader(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    public void load( Graphics g, String filename, int sx, int sy, int sz, int ex, int ey, int ez, int blockId ) {
        try {
            FileInputStream fileInput = new FileInputStream(filename);
            DataInputStream input = new DataInputStream( new BufferedInputStream(fileInput));

            boolean done = false;
            int sizeX=0;
            int sizeY=0;
            int sizeZ=0;
            while( !done ) {
                String line = input.readLine();
                Matcher matcher = dim.matcher(line);
                if( matcher.matches() ) {
                    sizeX = new Integer(matcher.group(1));
                    sizeY = new Integer(matcher.group(2));
                    sizeZ = new Integer(matcher.group(3));
                }
                if( data.matcher(line).matches() ) {
                    done = true;
                }
            }
            done = false;
            int x=0, y=0, z=0;
            int halfSizeX = (ex-sx)/2;
            int halfSizeY = (ey-sy)/2;
            int halfSizeZ = (ez-sz)/2;
            while( !done ) {
                byte value = input.readByte();
                int count = input.readByte() & 0xff;
                for( int i=0; i<count; i++ ) {
                    if( value>0 && x>=sx && y>=sy && z>=sz && x<=ex && y<=ey && z<=ez ) {
                        g.setPixel(x - sx - halfSizeX, y - sy - halfSizeY, z - sz - halfSizeZ, blockId);
//                        g.setPixel(2 * sz - (z - sz), y - sy, x - sx, value);
                    }

                    y ++;
                    if( y>=sizeY ) {
                        y = 0;
                        z ++;
                    }
                    if( z>=sizeZ ) {
                        z = 0;
                        x ++;
                    }
                    if( x>=sizeX ) {
                        done = true;
                        break;
                    }
                }
            }
            fileInput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
