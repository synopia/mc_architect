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
        text1 = readString(in, 15);
        text2 = readString(in, 15);
        text3 = readString(in, 15);
        text4 = readString(in, 15);
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        out.writeInt( x );
        out.writeShort( y );
        out.writeInt( z );
        writeString(out, text1, 15);
        writeString(out, text2, 15);
        writeString(out, text3, 15);
        writeString(out, text4, 15);
    }
}
