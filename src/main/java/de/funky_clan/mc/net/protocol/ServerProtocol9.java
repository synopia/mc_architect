package de.funky_clan.mc.net.protocol;

import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.events.BlockUpdate;
import de.funky_clan.mc.events.ChunkUpdate;
import de.funky_clan.mc.events.ConnectionEstablished;
import de.funky_clan.mc.events.UnloadChunk;
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

    private Inflater inflater;
    private byte[] compressedData;
    @Inject
    private EventBus eventBus;

    @Inject
    private PlayerPositionProtocol playerPositionProtocol;

    @Override
    public void load() {
        super.load();

        inflater = new Inflater();
        compressedData = new byte[1<<17];

        playerPositionProtocol.loadServer(this);

        setDecoder(0x01, new MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                int entityId = in.readInt();
                String username = in.readUTF();
                String password = in.readUTF();
                long seed = in.readLong();
                int dim = in.readByte();

                playerPositionProtocol.setEntityId(entityId);
            }
        });


        setDecoder(0x32, new MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                int x = in.readInt();
                int y = in.readInt();
                boolean mode = in.readBoolean();
                if( !mode ) {
                    eventBus.fireEvent(new UnloadChunk(x,y) );
                }
            }
        });

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
                byte[] data = new byte[uncompressedSize];
                inflater.reset();
                inflater.setInput(compressedData);
                try {
                    inflater.inflate(data, 0, uncompressedSize);
                } catch (DataFormatException dataformatexception) {
                    throw new IOException("Bad compressed data format");
                }

                eventBus.fireEvent(new ChunkUpdate(x, y, z, sizeX, sizeY, sizeZ, data));
            }
        });

        setDecoder(0x35, new MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                int x = in.readInt();
                int y = in.readByte();
                int z = in.readInt();
                byte type = in.readByte();
                byte meta = in.readByte();

                eventBus.fireEvent( new BlockUpdate(x,y,z,type,meta));
            }
        });

        setDecoder(0x34, new MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                int chunkX = in.readInt();
                int chunkZ = in.readInt();
                short size = in.readShort();
                short[] coords = new short[size];
                for (int i = 0; i < size; i++) {
                    coords[i] = in.readShort();
                }
                byte[] type = new byte[size];
                byte[] meta = new byte[size];
                in.readFully(type);
                in.readFully(meta);

                for (int i = 0; i < size; i++) {
                    int x = (chunkX<<4) + (coords[i]>>12);
                    int z = (chunkZ<<4) + ((coords[i]>>8) & ((1<<4)-1));
                    int y = coords[i] & ((1<<8)-1);
                    eventBus.fireEvent( new BlockUpdate(x,y,z,type[i], meta[i]));
                }
            }
        });
    }
}
