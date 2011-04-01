package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author synopia
 */
public class ChunkData extends BasePacket {
    public static final int ID = 0x33;
    private byte[]          compressedData;
    private byte[]          data;
    private int             sizeX;
    private int             sizeY;
    private int             sizeZ;
    private int             x;
    private int             y;
    private int             z;

    public ChunkData() {}

    public ChunkData( byte source, int x, int y, int z, int sizeX, int sizeY, int sizeZ, byte[] data ) {
        super( source );
        this.x     = x;
        this.y     = y;
        this.z     = z;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.data  = data;
    }

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        x     = in.readInt();
        y     = in.readShort();
        z     = in.readInt();
        sizeX = in.read() + 1;
        sizeY = in.read() + 1;
        sizeZ = in.read() + 1;

        int compressedSize = in.readInt();

        compressedData = new byte[compressedSize];
        in.readFully( compressedData );
        inflateData();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        int size = deflateData();

        out.writeInt( x );
        out.writeShort( y );
        out.writeInt( z );
        out.write( sizeX - 1 );
        out.write( sizeY - 1 );
        out.write( sizeZ - 1 );
        out.writeInt( size );
        out.write( compressedData, 0, size );
    }

    private int deflateData() {
        Deflater deflater = new Deflater();

        compressedData = new byte[data.length];
        deflater.setInput( data );
        deflater.finish();

        return deflater.deflate( compressedData );
    }

    private void inflateData() throws IOException {
        Inflater inflater         = new Inflater();
        int      uncompressedSize = ( sizeX * sizeY * sizeZ * 5 ) / 2;

        data = new byte[uncompressedSize];
        inflater.setInput( compressedData );

        try {
            inflater.inflate( data, 0, uncompressedSize );
        } catch( DataFormatException dataformatexception ) {
            throw new IOException( "Bad compressed data format" );
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    public byte[] getData() {
        return data;
    }
}
