package de.funky_clan.mc.net;

import de.funky_clan.mc.eventbus.NetworkEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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

    @SuppressWarnings("unchecked")
    protected void writeMetadata(DataOutputStream out, ArrayList meta) throws IOException {
        Iterator it = meta.iterator();
        while( it.hasNext() ){
            byte r = (Byte)it.next();
            out.writeByte(r);
            int i = (r & 0xe0)>>5;
            switch (i) {
                case 0:
                    out.writeByte((Byte)it.next());
                    break;
                case 1:
                    out.writeShort((Short)it.next());
                    break;
                case 2:
                    out.writeInt((Integer)it.next());
                    break;
                case 3:
                    out.writeFloat((Float)it.next());
                    break;
                case 4:
                    out.writeUTF((String)it.next());
                    break;
                case 5:
                    out.writeShort((Short)it.next());
                    out.writeByte((Byte)it.next());
                    out.writeShort((Short)it.next());
                    break;
                case 6:
                    out.writeInt((Integer)it.next());
                    out.writeInt((Integer)it.next());
                    out.writeInt((Integer)it.next());
                    break;
            }
        }
        out.writeByte(0x7f);
    }

    @SuppressWarnings("unchecked")
    protected ArrayList readMetadata(DataInputStream in) throws IOException {
        ArrayList result = new ArrayList();
        int index = 0;
        for( byte r=in.readByte(); r!=0x7f; r=in.readByte() ) {
            int i = (r & 0xe0)>>5;
            int j = r & 0x1f;
            result.add( r );
            switch (i) {
                case 0:
                    result.add(in.readByte());
                    break;
                case 1:
                    result.add(in.readShort());
                    break;
                case 2:
                    result.add(in.readInt());
                    break;
                case 3:
                    result.add(in.readFloat());
                    break;
                case 4:
                    String texts = in.readUTF();
                    System.out.println(texts);
                    result.add(texts);
                    break;
                case 5:
                    result.add(in.readShort());
                    result.add(in.readByte());
                    result.add(in.readShort());
                    break;
                case 6:
                    result.add(in.readInt());
                    result.add(in.readInt());
                    result.add(in.readInt());
                    break;
            }
        }
        return result;
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
