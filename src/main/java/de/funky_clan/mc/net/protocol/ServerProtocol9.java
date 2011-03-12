package de.funky_clan.mc.net.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * @author synopia
 */
public class ServerProtocol9 extends Protocol9 {
    private final Logger log = LoggerFactory.getLogger(ServerProtocol9.class);

    public interface ServerHandler extends ProtocolHandler {
        void onChunkUpdate( int sx, int sy, int sz, int sizeX, int sizeY, int sizeZ, byte[] data );
    }

    private Inflater inflater;
    private byte[] compressedData;
    private byte[] data;
    private ServerHandler handler;

    public ServerProtocol9() {
        this(null);
    }

    public ServerProtocol9(ServerHandler handler) {
        super(handler);
        this.handler = handler;
    }

    @Override
    protected void load() {
        super.load();

        inflater = new Inflater();
        compressedData = new byte[1<<17];
        data = new byte[1<<17];

        setDecoder(0x33, new MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {

                int x = in.readInt();
                int y = in.readShort();
                int z = in.readInt();
                int sizeX = in.read() + 1;
                int sizeY = in.read() + 1;
                int sizeZ = in.read() + 1;
                int compressedSize = in.readInt();
                in.readFully(compressedData, 0, compressedSize);
                int uncompressedSize = (sizeX * sizeY * sizeZ * 5) / 2;
                inflater.reset();
                inflater.setInput(compressedData);
                try {
                    inflater.inflate(data, 0, uncompressedSize);
                } catch (DataFormatException dataformatexception) {
                    throw new IOException("Bad compressed data format");
                }

                if( handler!=null ) {
                    handler.onChunkUpdate( x, y, z, sizeX, sizeY, sizeZ, data );
                }
            }
        });
    }
}
