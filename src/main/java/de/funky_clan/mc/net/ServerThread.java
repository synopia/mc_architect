package de.funky_clan.mc.net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * PlayerPosition-Server, just send current playerposition to all connected clients.
 * This class needs to be patched into minecraft.jar
 *
 * @author synopia
 */
public class ServerThread extends Thread {
    private ServerSocket socket;
    private int port;
    private final List<ObjectOutputStream> clients = new ArrayList<ObjectOutputStream>();

    public ServerThread(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (socket == null) {
                    socket = new ServerSocket(port);
                }
                accept();
            } catch (IOException e) {
                e.printStackTrace();
                socket = null;
            }
        }
    }

    private void accept() throws IOException {
        Socket clientSocket = null;
        clientSocket = socket.accept();
        ObjectOutputStream client = new ObjectOutputStream(clientSocket.getOutputStream());
        synchronized (clients) {
            clients.add(client);
        }
        System.out.println("New client connected");
    }

    public void sendPlayerPosition(int x, int y, int z, float radius) {
        List<ObjectOutputStream> myClients = getClients();
        for (ObjectOutputStream client : myClients) {
            try {
                client.writeInt(x);
                client.writeInt(y);
                client.writeInt(z);
                client.writeFloat(radius);
                client.flush();
            } catch (IOException e) {
                removeClient(client);
            }
        }
    }

    private void removeClient(ObjectOutputStream client) {
        System.out.println("Client disconnected");
        synchronized (clients) {
            clients.remove(client);
        }
    }

    public List<ObjectOutputStream> getClients() {
        List<ObjectOutputStream> myClients;
        synchronized (clients) {
            myClients = new ArrayList<ObjectOutputStream>(clients);
        }
        return myClients;
    }

    public static void main(String[] args) {
        ServerThread thread = new ServerThread(12345);
        thread.start();
        while (true) {
            thread.sendPlayerPosition(1, 2, 3, 2);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {

            }
        }
    }
}
