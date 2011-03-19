package de.funky_clan.mc.net.protocol;

import com.google.inject.Inject;
import de.funky_clan.mc.eventbus.EventBus;
import de.funky_clan.mc.events.ConnectionEstablished;
import de.funky_clan.mc.events.ConnectionLost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author synopia
 */
public abstract class Protocol {
    private final Logger log = LoggerFactory.getLogger(Protocol.class);

    public interface MessageDecoder {
        void decode( DataInputStream in ) throws IOException;
    }

    private HashMap<Integer, MessageDecoder> decoders = new HashMap<Integer, MessageDecoder>();
    @Inject
    private EventBus eventBus;

    public Protocol() {
        load();
    }

    public void decode( DataInputStream in ) throws IOException {
        int type = in.readByte() & 0xff;
        if( decoders.containsKey(type) ) {
            MessageDecoder decoder = decoders.get(type);
            decoder.decode(in);
        } else {
            throw new IllegalArgumentException("Unknown message type " + type);
        }
    }


    protected void load() {
        setDecoder( 0x01, new MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                int version = in.readInt();
                String username = in.readUTF();
                String password = in.readUTF();
                long seed = in.readLong();
                int dim = in.readByte();

                eventBus.fireEvent(new ConnectionEstablished());
            }
        });

        setDecoder( 0xff, new MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                String reason = in.readUTF();
                eventBus.fireEvent(new ConnectionLost());
            }
        });
    }

    protected void setDecoder( int id, MessageDecoder decoder ) {
        decoders.put(id, decoder );
    }

    protected MessageDecoder getDecoder( int id ) {
        MessageDecoder decoder = null;
        if( decoders.containsKey(id) ) {
            decoder = decoders.get(id);
        }
        return decoder;
    }

}
