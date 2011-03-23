package de.funky_clan.mc.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
* @author synopia
*/
public class MitmInputStream extends InputStream {
    private InputStream source;
    private OutputStream target;
    MitmInputStream(InputStream in, OutputStream target) {
        this.source = in;
        this.target = target;
    }

    @Override
    public int read() throws IOException {
        int read = source.read();
        target.write(read);
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = source.read(b, off, len);
        target.write(b, off, read);
        return read;
    }

    @Override
    public int available() throws IOException {
        return source.available();
    }

}
