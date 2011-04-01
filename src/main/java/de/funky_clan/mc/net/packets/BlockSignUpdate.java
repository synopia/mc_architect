package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class BlockSignUpdate extends BasePacket {
    public static final int ID = 0x82;
    private String          text1;
    private String          text2;
    private String          text3;
    private String          text4;
    private int             x;
    private short           y;
    private int             z;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        x     = in.readInt();
        y     = in.readShort();
        z     = in.readInt();
        text1 = in.readUTF();
        text2 = in.readUTF();
        text3 = in.readUTF();
        text4 = in.readUTF();
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( x );
        out.writeShort( y );
        out.writeInt( z );
        out.writeUTF( text1 );
        out.writeUTF( text2 );
        out.writeUTF( text3 );
        out.writeUTF( text4 );
    }
}
