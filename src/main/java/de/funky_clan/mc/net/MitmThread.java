package de.funky_clan.mc.net;

import de.funky_clan.mc.net.protocol.ClientProtocol9;
import de.funky_clan.mc.net.protocol.Protocol9;
import de.funky_clan.mc.net.protocol.ServerProtocol9;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author synopia
 */
public class MitmThread extends Thread {

    private Filter response;
    private Filter request;
    private ServerSocket socket;

    private class MitmInputStream extends InputStream {
        private InputStream source;
        private OutputStream target;
        private MitmInputStream(InputStream in, OutputStream target) {
            this.source = in;
            this.target = target;
        }

        @Override
        public int read() throws IOException {
            int read = source.read();
            target.write(read);
            return read;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int read = source.read(b, off, len);
            target.write(b, off, read);
            return read;
        }

        @Override
        public int available() throws IOException {
            return source.available();
        }

    }

    private class Filter extends Thread {
        private InputStream input;
        private DataInputStream dataInput;
        private OutputStream output;
        private Protocol9 protocol;

        private Filter(InputStream input, OutputStream output, Protocol9 protocol) {
            this.input = new MitmInputStream(input, output);
            this.dataInput = new DataInputStream( this.input );
            this.output = output;
            this.protocol = protocol;
        }

        @Override
        public void run() {
            try {
                while( !interrupted() ) {
                    protocol.decode(dataInput);
                    output.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String            targetHost;
    private int               targetPort;
    private int               sourcePort;
    private ClientProtocol9.ClientHandler clientHandler;
    private ServerProtocol9.ServerHandler serverHandler;
    private boolean            connected;
    private Logger logger = LoggerFactory.getLogger(MitmThread.class);

    public MitmThread(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public MitmThread(int sourcePort, ClientProtocol9.ClientHandler clientHandler, ServerProtocol9.ServerHandler serverHandler) {
        this.sourcePort = sourcePort;
        this.clientHandler = clientHandler;
        this.serverHandler = serverHandler;
    }

    @Override
    public void run() {
        Socket targetSocket = null;
        while( true ) {
            try {
                if( socket==null ) {
                    socket = new ServerSocket( sourcePort );
                    logger.info("MITM Server is listening at port " + sourcePort);
                }

                logger.info("MITM Server is waiting for client ");

                Socket sourceSocket = socket.accept();
                logger.info("MITM Server: Client connected");

                logger.info("MITM Server: connecting to server " + targetHost + " : "+targetPort);
                targetSocket = new Socket( targetHost, targetPort );

                OutputStream toSource = sourceSocket.getOutputStream();
                InputStream fromSource = sourceSocket.getInputStream();

                OutputStream toTarget = targetSocket.getOutputStream();
                InputStream fromTarget = targetSocket.getInputStream();

                request = new Filter(fromSource, toTarget, new ClientProtocol9(clientHandler) );
                response = new Filter(fromTarget, toSource, new ServerProtocol9(serverHandler) );

                logger.info("MITM Server: starting streaming threads");
                request.start();
                response.start();

                connected = true;

            } catch (IOException e) {
                e.printStackTrace();

                shutdownFilter(request);
                shutdownFilter(response);
                shutdownSocket(targetSocket);
                connected = false;
            }
        }
    }

    private void shutdownSocket(Socket targetSocket) {
        if( targetSocket!=null ) {
            try {
                targetSocket.close();
            } catch (IOException e1) {
            }
        }
    }

    private void shutdownFilter( Filter filter ) {
        if( filter!=null ) {
            filter.interrupt();
            try {
                filter.join();
            } catch (InterruptedException e) {
            }
        }
    }

    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    public void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }

    public boolean isConnected() {
        return connected;
    }
}
