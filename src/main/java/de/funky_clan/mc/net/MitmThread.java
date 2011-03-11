package de.funky_clan.mc.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author synopia
 */
public class MitmThread extends Thread {

    private Filter response;
    private Filter request;
    private ServerSocket socket;

    public interface ConnectionHandler {
        void onConnect();
        void onDisconnect();
        Handler createRequestHandler();
        Handler createResponseHandler();
    }

    public interface Handler {
        void onData( byte[] buffer, int length );
    }

    private class Filter extends Thread {
        private InputStream input;
        private OutputStream output;
        private Handler handler;

        private Filter(InputStream input, OutputStream output) {
            this.input = input;
            this.output = output;
        }

        private Filter(InputStream input, OutputStream output, Handler handler) {
            this.input = input;
            this.output = output;
            this.handler = handler;
        }

        @Override
        public void run() {
            byte []buffer = new byte[4096];

            try {
                while( !interrupted() ) {
                    int read = input.read(buffer);
                    if( read == -1 ) {
                        break;
                    }
                    if( handler!=null ) {
                        handler.onData(buffer, read);
                    }
                    output.write( buffer, 0, read );
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
    private ConnectionHandler connectionHandler;
    private boolean            connected;
    private Logger logger = LoggerFactory.getLogger(MitmThread.class);

    public MitmThread(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public MitmThread(int sourcePort, ConnectionHandler connectionHandler) {
        this.sourcePort = sourcePort;
        this.connectionHandler = connectionHandler;
    }

    public MitmThread(String targetHost, int targetPort, int sourcePort) {
        this.targetHost = targetHost;
        this.targetPort = targetPort;
        this.sourcePort = sourcePort;
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
                connectionHandler.onConnect();

                logger.info("MITM Server: connecting to server " + targetHost + " : "+targetPort);
                targetSocket = new Socket( targetHost, targetPort );

                OutputStream toSource = sourceSocket.getOutputStream();
                InputStream fromSource = sourceSocket.getInputStream();

                OutputStream toTarget = targetSocket.getOutputStream();
                InputStream fromTarget = targetSocket.getInputStream();

                request = new Filter(fromSource, toTarget, connectionHandler.createRequestHandler() );
                response = new Filter(fromTarget, toSource, connectionHandler.createResponseHandler() );

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
                connectionHandler.onDisconnect();
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

    public void setConnectionHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
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
