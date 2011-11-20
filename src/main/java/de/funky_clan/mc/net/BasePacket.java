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
    private static boolean []damageableList = new boolean[32000];

    static {
        damageableList[256] = true;
        damageableList[257] = true;
        damageableList[258] = true;
        damageableList[259] = true;
        damageableList[261] = true;
        damageableList[267] = true;
        damageableList[268] = true;
        damageableList[269] = true;
        damageableList[270] = true;
        damageableList[271] = true;
        damageableList[272] = true;
        damageableList[273] = true;
        damageableList[274] = true;
        damageableList[275] = true;
        damageableList[276] = true;
        damageableList[277] = true;
        damageableList[278] = true;
        damageableList[279] = true;
        damageableList[283] = true;
        damageableList[284] = true;
        damageableList[285] = true;
        damageableList[286] = true;
        damageableList[290] = true;
        damageableList[291] = true;
        damageableList[292] = true;
        damageableList[293] = true;
        damageableList[294] = true;
        damageableList[298] = true;
        damageableList[299] = true;
        damageableList[300] = true;
        damageableList[301] = true;
        damageableList[302] = true;
        damageableList[303] = true;
        damageableList[304] = true;
        damageableList[305] = true;
        damageableList[306] = true;
        damageableList[307] = true;
        damageableList[308] = true;
        damageableList[309] = true;
        damageableList[310] = true;
        damageableList[311] = true;
        damageableList[312] = true;
        damageableList[313] = true;
        damageableList[314] = true;
        damageableList[315] = true;
        damageableList[316] = true;
        damageableList[317] = true;
        damageableList[346] = true;
        damageableList[359] = true;
    }

    protected BasePacket() {}

    protected BasePacket( byte source ) {
        this.source = source;
    }

    public void dropPacket() {
        source = -1;
    }

    protected ItemStack readItem( DataInputStream in ) throws IOException {
        ItemStack result = null;
        short itemId     = in.readShort();

        if( itemId >= 0 ) {
            byte itemCount = in.readByte();
            short itemUses  = in.readShort();

            result = new ItemStack(itemId, itemCount, itemUses);
            if( damageableList[itemId] ) {
                byte[] data = null;
                short len = in.readShort();
                if( len>=0 ) {
                    data = new byte[len];
                    in.readFully(data);
                }
                result.setData(data);
            }
        }

        return result;
    }

    protected void writeItem( DataOutputStream out, ItemStack itemStack ) throws IOException {
        if( itemStack==null ) {
            out.writeShort(-1);
        } else {
            out.writeShort( itemStack.getItemId() );
            out.writeByte( itemStack.getItemCount() );
            out.writeShort( itemStack.getItemUses() );
            if( damageableList[itemStack.getItemId()] ) {
                byte[] data = itemStack.getData();
                if( data==null ) {
                    out.writeShort(-1);
                } else {
                    out.writeShort(data.length);
                    out.write(data);
                }
            }
        }
    }

    @SuppressWarnings( "unchecked" )
    protected void writeMetadata( DataOutputStream out, ArrayList meta ) throws IOException {
        Iterator it = meta.iterator();

        while( it.hasNext() ) {
            byte r = (Byte) it.next();

            out.writeByte( r );

            int i = ( r & 0xe0 ) >> 5;

            switch( i ) {
            case 0:
                out.writeByte( (Byte) it.next() );

                break;

            case 1:
                out.writeShort( (Short) it.next() );

                break;

            case 2:
                out.writeInt( (Integer) it.next() );

                break;

            case 3:
                out.writeFloat( (Float) it.next() );

                break;

            case 4:
                writeString(out, (String) it.next());

                break;

            case 5:
                out.writeShort( (Short) it.next() );
                out.writeByte( (Byte) it.next() );
                out.writeShort( (Short) it.next() );

                break;

            case 6:
                out.writeInt( (Integer) it.next() );
                out.writeInt( (Integer) it.next() );
                out.writeInt( (Integer) it.next() );

                break;
            }
        }

        out.writeByte( 0x7f );
    }

    @SuppressWarnings( "unchecked" )
    protected ArrayList readMetadata( DataInputStream in ) throws IOException {
        ArrayList result = new ArrayList();

        for( byte r = in.readByte(); r != 0x7f; r = in.readByte() ) {
            int i = ( r & 0xe0 ) >> 5;

            result.add( r );

            switch( i ) {
            case 0:
                result.add( in.readByte() );

                break;

            case 1:
                result.add( in.readShort() );

                break;

            case 2:
                result.add( in.readInt() );

                break;

            case 3:
                result.add( in.readFloat() );

                break;

            case 4:
                String texts = readString(in, 64);

                result.add( texts );

                break;

            case 5:
                result.add( in.readShort() );
                result.add( in.readByte() );
                result.add( in.readShort() );

                break;

            case 6:
                result.add( in.readInt() );
                result.add( in.readInt() );
                result.add( in.readInt() );

                break;
            }
        }

        return result;
    }

    public String readString( DataInputStream in, int maxLen ) throws IOException {
        short len = in.readShort();
        if( len>maxLen ) {
            throw new IOException("Received string length longer than maxlen");
        }
        if( len<0 ) {
            throw new IOException("Received string length < 0");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
              sb.append(in.readChar());
        }

        return sb.toString();
    }

    public void writeString(DataOutputStream out, String str) throws IOException {
        writeString(out, str, -1);
    }
    public void writeString(DataOutputStream out, String str, int maxLen) throws IOException {
        if( str.length()>32767 ) {
            throw new IOException("String to long");
        } else {
            if( maxLen!=-1 && str.length()>=maxLen ) {
                str = str.substring(0, maxLen);
            }
            out.writeShort(str.length());
            out.writeChars(str);
        }
    }

    @Override
    public byte getSource() {
        return source;
    }

    public String getSourceName() {
        return( source == NetworkEvent.CLIENT )
              ? "client"
              : "server";
    }

    @Override
    public void setSource( byte source ) {
        this.source = source;
    }
}
