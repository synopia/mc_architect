package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class EntityUse extends BasePacket {
    public static final int ID = 0x07;
    private boolean leftClick;
    private int target;
    private int user;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        user = in.readInt();
        target = in.readInt();
        leftClick = in.readBoolean();
    }

    @Override
    public void encode(DataOutputStream out) throws IOException {
        out.writeInt(user);
        out.writeInt(target);
        out.writeBoolean(leftClick);
    }
}
