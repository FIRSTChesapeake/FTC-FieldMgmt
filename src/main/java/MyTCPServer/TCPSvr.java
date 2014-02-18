/**
 * 
 */
package MyTCPServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FTCMgrShared.*;
import MgrMain.Main;
/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class TCPSvr {
    final public static Logger          logger          = LoggerFactory.getLogger(Main.class);
    private static ServerSocket         sSocket         = null;
    private static serverThread         sThread         = null;

    private static Socket               cSocket         = null;
    private static final int            maxClientsCount = 5;
    private static final clientThread[] threads         = new clientThread[maxClientsCount];

    private static int ServerPort = 0;
    
    private static boolean              stopRequested   = false;

    public void sendToAllClients(final TCPPack pack) {
        for (final clientThread thread : threads) {
            if (thread != null) {
                thread.SendPack(pack);
            }
        }
    }

    public TCPSvr(final int Port) {
        try {
            ServerPort = Port;
        	sThread = new serverThread();
            sThread.start();
        } catch (final Exception e) {
            logger.error("Error Claiming port for Client Listener");
        }

    }

    public void abort() {
        stopRequested = true;
        for (final clientThread thread : threads) {
            if (thread != null) {
                thread.interrupt();
            }
        }
        try {
            sSocket.close();
            sThread.interrupt();
        } catch (final IOException e) {
            logger.error("Unable to close Client Listner");
        }
    }

    private class clientThread extends Thread {

        private ObjectOutputStream    ObjOutStream  = null;
        private ObjectInputStream    ObjInStream    = null;
        private Socket               cSocket = null;
        private final clientThread[] threads;

        public clientThread(final Socket clientSocket, final clientThread[] threads) {
            this.cSocket = clientSocket;
            this.threads = threads;
        }

        @Override
        public void run() {
            final clientThread[] threads = this.threads;
            try {
                // Create input and output streams for this client.             
                ObjOutStream = new ObjectOutputStream(cSocket.getOutputStream());
                ObjOutStream.flush();
                ObjInStream = new ObjectInputStream(cSocket.getInputStream());
                while (!stopRequested) {
                    // Do the magic! ---------------------------------------
                    final Object inObj = ObjInStream.readObject();
                    final TCPPack inPack = (TCPPack) inObj;
                    switch (inPack.PackType) {
                        case NONE:

                            break;
                        case REFRESH_REQUEST:

                            break;
                        case BYE:
                        	stopRequested = true;
                        	break;
                    }
                }
                // Clean up.
                CleanThread(this);
                logger.info("Client from {} has disconnected.",cSocket.getRemoteSocketAddress().toString());
                // Close up shop.
                ObjInStream.close();
                ObjOutStream.close();
                cSocket.close();
            } catch (final ClassNotFoundException e) {
            	logger.error("Class Not Found Error init Client Port: {}", e.getMessage());
            } catch (final SocketException e) {
                logger.info("Client Connection Aborted!");
            } catch (final IOException e) {
            	logger.error("IO Error init Client Port: {}", e.getMessage());
            }
        }
        private void CleanThread(Thread thread){
        	for (int i=0; i < maxClientsCount; i++) {
                if (threads[i] == thread) {
                    threads[i] = null;
                }
            }
        }
        public void SendPack(final TCPPack pack) {
            if(cSocket.isConnected()){
	        	try {
	            	logger.info("Writing Object: " + this.toString());
	                ObjOutStream.writeObject(pack);
	            } catch (final IOException e) {
	            	logger.error("Couldn't write object: {}", e.getMessage());
	            	CleanThread(this);
	            }
            } else {
            	logger.info("Port is dead!");
            	stopRequested = true;
            }
        }
    }

    private class serverThread extends Thread {

        @Override
        public void run() {
        	SocketAddress sAddress = new InetSocketAddress("0.0.0.0",ServerPort);
            try {
				sSocket = new ServerSocket();
				sSocket.bind(sAddress);
				logger.info("Client Port Bound");
			} catch (IOException e) {
				logger.error("Unable to Bind Client Listening Port");
			}
            
            while (!stopRequested) {
                try {
                	logger.info("Waiting for Next Client Connection..");
                    cSocket = sSocket.accept();
                    logger.info("New Client Received from {}",sSocket.accept().getRemoteSocketAddress().toString());
                    boolean availThread = false;
                    for (int i=0; i < maxClientsCount; i++) {
                        if (threads[i] == null) {
                            (threads[i] = new clientThread(cSocket, threads)).start();
                            availThread = true;
                            break;
                        }
                    }
                    if (!availThread) {
                        // FIXME Tell the client we don't have space?
                        cSocket.close();
                    }
                } catch (final SocketException e) {
                    logger.info("Client Listner Socket Aported - We must be quitting!");
                } catch (final IOException e) {
                    logger.error("Unhandled IO Exception while accepting client");
                }
            }
        }
    }
}