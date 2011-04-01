package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author synopia
 */
public class BlockMultiUpdate extends BasePacket {
    public static final int ID = 0x34;
    private short[]         coords;
    private List<Item>      items;
    private byte[]          meta;
    private short           size;
    private byte[]          type;
    private int             x;
    private int             y;

    public BlockMultiUpdate() {}

    public BlockMultiUpdate( byte source, int x, int y ) {
        super( source );
        this.x = x;
        this.y = y;
    }

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        x      = in.readInt();
        y      = in.readInt();
        size   = in.readShort();
        coords = new short[size];

        for( int i = 0; i < size; i++ ) {
            coords[i] = in.readShort();
        }

        type = new byte[size];
        meta = new byte[size];
        in.readFully( type );
        in.readFully( meta );
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( x );
        out.writeInt( y );

        if( coords != null ) {
            out.writeShort( size );

            for( int i = 0; i < size; i++ ) {
                out.writeShort( coords[i] );
            }

            out.write( type );
            out.write( meta );
        } else {
            out.writeShort( items.size() );

            for( Item item : items ) {
                int v = ( item.x << 12 ) | ( item.z << 8 ) | ( item.y );

                out.writeShort( v );
            }

            for( Item item : items ) {
                out.writeByte( item.type );
            }

            for( Item item : items ) {
                out.writeByte( item.meta );
            }
        }
    }

    public void each( Each block ) {
        for( int i = 0; i < size; i++ ) {
            int x = ( this.x << 4 ) + ( coords[i] >> 12 );
            int z = ( this.y << 4 ) + (( coords[i] >> 8 ) & (( 1 << 4 ) - 1 ));
            int y = coords[i] & (( 1 << 8 ) - 1 );

            block.update( x, y, z, type[i], meta[i] );
        }
    }

    public void add( int x, int y, int z, byte blockId, byte meta ) {
        if( items == null ) {
            items = new ArrayList<Item>();
        }

        items.add( new Item( x - ( this.x << 4 ), y, z - ( this.y << 4 ), blockId, meta ));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public short getSize() {
        return( items == null )
              ? size
              : (short) items.size();
    }

    public short[] getCoords() {
        return coords;
    }

    public byte[] getType() {
        return type;
    }

    public byte[] getMeta() {
        return meta;
    }

    public interface Each {
        public void update( int x, int y, int z, int type, int meta );
    }


    private static class Item {
        public final byte meta;
        public final byte type;
        public final int  x;
        public final int  y;
        public final int  z;

        private Item( int x, int y, int z, byte type, byte meta ) {
            this.x    = x;
            this.y    = y;
            this.z    = z;
            this.type = type;
            this.meta = meta;
        }
    }
}
