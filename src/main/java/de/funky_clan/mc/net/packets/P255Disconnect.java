package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class P255Disconnect extends BasePacket {
    public static final int ID = 0xff;
    private String          reason;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode( DataInputStream in ) throws IOException {
        reason = readString(in, 100);
    }

    @Override
    public void encode( DataOutputStream out ) throws IOException {
        writeString(out, reason, 50);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
