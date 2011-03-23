package de.funky_clan.mc.net;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author synopia
 */
@Singleton
public class MinecraftBinding {
    private final Logger log = LoggerFactory.getLogger(MinecraftBinding.class);
    private static List<Integer> PACKET_IDS = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,27,28,29,30,31,32,33,34,38,39,40,50,51,52,53,54,60,100,101,102,103,104,105,106,130,255);
    private HashMap<Integer, MinecraftMessageDecoder> decoderMethods = new HashMap<Integer, MinecraftMessageDecoder>();

    public MinecraftBinding() {
        try {
            load();
            log.info("done");
        } catch (NetworkException e) {

        }
    }

    public static class MinecraftMessageDecoder {
        private Method decoderMethod;
        private Object mcInstance;

        public MinecraftMessageDecoder(Object mcInstance) {
            this.mcInstance = mcInstance;

            try {
                decoderMethod = mcInstance.getClass().getMethod("a", DataInputStream.class );

            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(mcInstance+" does not implement a(DataInputStream.class)" );
            }
        }

        public void decode(DataInputStream in) {
            try {
                decoderMethod.invoke(mcInstance, in);
            } catch (IllegalAccessException e) {
                throw new NetworkException(e);
            } catch (InvocationTargetException e) {
                throw new NetworkException(e);
            }
        }
    }

    public void decode( int packetId, DataInputStream in ) {
        if( decoderMethods.containsKey(packetId) ) {
            MinecraftMessageDecoder decoder = decoderMethods.get(packetId);
            decoder.decode(in);
        }
    }

    private void loadMinecraftJar() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, MalformedURLException {
        String minecraftJarFilename = System.getenv("APPDATA") + "/.minecraft/bin/minecraft.jar";
        log.info("Trying to load "+minecraftJarFilename);
        URLClassLoader cl = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
        method.setAccessible(true);
        File minecraftJar = new File(minecraftJarFilename);
        method.invoke(cl, minecraftJar.toURI().toURL());
        log.info(" success!");

    }

    public void load() {
        try {
            loadMinecraftJar();

            log.info("Accessing class 'hz'");
            Class<?> hz = Class.forName("hz");
            Method method = hz.getMethod("a", Integer.TYPE );

            for (Integer packetId : PACKET_IDS) {
                Object mcInstance = method.invoke(null, packetId );
                decoderMethods.put(packetId, new MinecraftMessageDecoder(mcInstance));
                log.info("packet id="+packetId+" registered successfully.");
            }
        } catch (ClassNotFoundException e) {
            throw new NetworkException(e);
        } catch (NoSuchMethodException e) {
            throw new NetworkException(e);
        } catch (IllegalAccessException e) {
            throw new NetworkException(e);
        } catch (InvocationTargetException e) {
            throw new NetworkException(e);
        } catch (MalformedURLException e) {
            throw new NetworkException(e);
        }
    }
}
