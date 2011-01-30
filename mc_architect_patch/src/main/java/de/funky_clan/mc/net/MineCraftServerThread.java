package de.funky_clan.mc.net;

import net.minecraft.client.Minecraft;

import java.io.ObjectOutputStream;

/**
 * @author paul.fritsche@googlemail.com
 */
public class MineCraftServerThread extends ServerThread {
    private Minecraft game;

    private int lastX;
    private int lastY;
    private int lastZ;
    private float lastRadius;

    public MineCraftServerThread(int port) {
        super(port);
    }

    public void render( Minecraft mc ) {
        game = mc;

        int x = xCoord();
        int y = yCoord();
        int z = zCoord();
        float radius = radius();

        if( lastX!=x || lastY!=y || lastZ!=z || radius!=lastRadius) {
            sendPlayerPosition( x, y, z, radius );
        }
        lastX = x;
        lastY = y;
        lastZ = z;
        lastRadius = radius;
    }

    private int xCoord()
    {
        int x = (int)game.g.aF;
        if(game.g.aF < 0.0D)
            x--;
        return x;
    }

    private int yCoord()
    {
        int y = (int)game.g.aH;
        if(game.g.aH < 0.0D)
            y--;
        return y;
    }

    private int zCoord()
    {
        return (int)game.g.aG;
    }

    private float radius() {
        return game.g.aL;
    }

    @Override
    protected void onNewClient(ObjectOutputStream client) {
        sendPlayerPosition(client, lastX, lastY, lastZ, lastRadius );
    }
}
