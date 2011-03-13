package de.funky_clan.mc.net.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author synopia
 */
public class Protocol9 extends Protocol {
    private final Logger log = LoggerFactory.getLogger(Protocol9.class);
    private static List<Integer> packetIds = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,27,28,29,30,31,32,33,34,38,39,40,50,51,52,53,54,60,100,101,102,103,104,105,106,130,255);

    public static class MinecraftMessageDecoder implements MessageDecoder {
        private Object mcInstance;
        private Method decoderMethod;

        public MinecraftMessageDecoder(Object mcInstance) {
            this.mcInstance = mcInstance;

            try {
                decoderMethod = mcInstance.getClass().getMethod("a", DataInputStream.class );
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(mcInstance+" does not implement a(DataInputStream.class)" );
            }
        }

        @Override
        public void decode(DataInputStream in) throws IOException {
            try {
                decoderMethod.invoke(mcInstance, in);
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvocationTargetException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    @Override
    protected void load() {
        try {
            Class<?> hz = Class.forName("hz");
            Method method = hz.getMethod("a", Integer.TYPE );

            for (Integer packetId : packetIds) {
                Object mcInstance = method.invoke(null, packetId );
                setDecoder(packetId, new MinecraftMessageDecoder(mcInstance));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
