package de.funky_clan.mc.net;

import de.funky_clan.mc.eventbus.NetworkEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public abstract class BasePacket implements NetworkEvent {

    private byte source;

    protected BasePacket(byte source) {
        this.source = source;
    }

    protected BasePacket() {
    }

    protected void writeMetadata(DataOutputStream out, byte[] meta, int len) throws IOException {
        out.write(meta, 0, len);
    }

    protected int readMetadata(DataInputStream in, byte[] meta) throws IOException {
        int index = 0;
        for( byte r=in.readByte(); r!=0x7f; r=in.readByte() ) {
            int i = (r & 0xe0)>>5;
            int j = r & 0x1f;
            meta[index++] = r;
            switch (i) {
                case 0:
                    meta[index++] = in.readByte();
                    break;
                case 1:
//                    in.readFully( meta, index, 2);
                    in.readShort();
                    index += 2;
                    break;
                case 2:case 3:
                    in.readFully( meta, index, 4);
                    index += 4;
                    break;
                case 4:
                    int len = in.readShort();
                    meta[index++] = (byte)((len >>> 8) & 0xFF);
                    meta[index++] = (byte)((len >>> 0) & 0xFF);
                    in.readFully( meta, index, len);
                    index+=len;
                    break;
                case 5:
                    in.readFully( meta, index, 5);
                    index += 5;
                    break;
                case 6:
                    in.readFully( meta, index, 12);
                    index += 12;
                    break;

            }
        }
        meta[index++] = 0x7f;
        return index;
    }

    @Override
    public byte getSource() {
        return source;
    }

    @Override
    public void setSource(byte source) {
        this.source = source;
    }
}
