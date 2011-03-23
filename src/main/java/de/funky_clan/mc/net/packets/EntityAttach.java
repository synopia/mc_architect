package de.funky_clan.mc.net.packets;

import de.funky_clan.mc.net.BasePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author synopia
 */
public class EntityAttach extends BasePacket {
    public static final int ID = 0x27;
    private int entityId;
    private int vehicleId;

    @Override
    public int getPacketId() {
        return ID;
    }

    @Override
    public void decode(DataInputStream in) throws IOException {
        entityId = in.readInt();
        vehicleId = in.readInt();
    }

    @Override
    public void encode(DataOutputStream out) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getEntityId() {
        return entityId;
    }

    public int getVehicleId() {
        return vehicleId;
    }
}
