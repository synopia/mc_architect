package de.funky_clan.mc.net.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author synopia
 */
public abstract class Protocol {
    private final Logger log = LoggerFactory.getLogger(Protocol.class);

    public interface ProtocolHandler {
        void onConnect();
        void onDisconnect();
    }

    public interface MessageDecoder {
        void decode( DataInputStream in ) throws IOException;
    }

    private Pattern messagePattern = Pattern.compile("\\((\\d+), '.*?'\\)");
    private HashMap<Integer, MessageDecoder> decoders = new HashMap<Integer, MessageDecoder>();
    private ProtocolHandler handler;

    protected Protocol() {
        this(null);
    }

    public Protocol(ProtocolHandler handler) {
        this.handler = handler;
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

                if( handler!=null ) {
                    handler.onConnect();
                }
            }
        });

        setDecoder( 0xff, new MessageDecoder() {
            @Override
            public void decode(DataInputStream in) throws IOException {
                String reason = in.readUTF();
                if( handler!=null ) {
                    handler.onDisconnect();
                }
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
