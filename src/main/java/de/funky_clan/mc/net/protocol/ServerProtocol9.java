package de.funky_clan.mc.net.protocol;

import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.eventbus.EventDispatcher;
import de.funky_clan.mc.net.protocol.events.ChunkUpdate;
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
    private EventBus eventBus = EventDispatcher.getDispatcher().getModelEventBus();

    @Override
    protected void load() {
        super.load();

        inflater = new Inflater();
        compressedData = new byte[1<<17];

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

                eventBus.fireEvent( new ChunkUpdate(x,y,z,sizeX,sizeY,sizeZ,data ));
            }
        });
    }
}
