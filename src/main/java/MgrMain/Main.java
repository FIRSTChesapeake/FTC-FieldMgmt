/**
 * 
 */
package MgrMain;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class Main {

    final public static Logger         logger = LoggerFactory.getLogger(Main.class);
    private static FtpServer           FTPserver;
    private static NioDatagramAcceptor UDPserver;
    public static MainWindow           MWind  = new MainWindow();

    /**
     * @param args
     */
    public static void main(final String[] args) {
        try {
        	// MAIN SETTINGS
            final int 		FTPport 		= 2211;
            final int 		UDPport 		= 2212;
            final int 		fieldCount 		= 2;
            final String 	FTPpass 		= "apple";
            // END MAIN SETTINGS
            
            // Build Main Objects!
            StartFTP(FTPport, FTPpass, fieldCount);
            StartUDP(UDPport);
            MWind.setVisible(true);
            
        } catch (final IOException ex) {
            logger.error("UDP Start failed with error {}", ex.getMessage());
        } catch (final FtpException ex) {
            logger.info("FTP Start failed with error {}", ex.getMessage());
        }
    }

    private static BaseUser MakeUser(final String username, final String password) {
        final BaseUser Buser = new BaseUser();
        // TODO: This isn't working properly. No Write Access?
        Buser.setName(username);
        Buser.setPassword(password);
        Buser.setHomeDirectory("ftproot");
        return Buser;
    }

    public static void Quit() {
        logger.info("Stopping FTP..");
        FTPserver.stop();
        logger.info("Stopping UDP..");
        UDPserver.unbind();
        logger.info("Done. Quitting!");
        System.exit(0);
    }

    private static UserManager SetupPassword(final String pass, final int fieldCount) throws FtpException {
        logger.info("Creating FTP Users..");
    	final PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        final UserManager um = userManagerFactory.createUserManager();
        for(int i=1;i!=fieldCount+1;i++){
        	logger.info("FTP User #{} created!",i);
        	um.save(MakeUser("field"+i, pass));
        }
        return um;
    }

    public static void StartFTP(final int FTPport, final String FTPPass, int fieldCount) throws FtpException {
        logger.info("Starting FTP Server on port {}", FTPport);
        final FtpServerFactory serverFactory = new FtpServerFactory();
        final ListenerFactory factory = new ListenerFactory();
        factory.setPort(FTPport);
        serverFactory.addListener("default", factory.createListener());
        final UserManager um = SetupPassword(FTPPass,fieldCount);
        serverFactory.setUserManager(um);
        FTPserver = serverFactory.createServer();
        FTPserver.start();
    }

    private static void StartUDP(final int port) throws IOException {
        logger.info("Starting UDP Listener on port {}", port);
        UDPserver = new NioDatagramAcceptor();
        UDPserver.setHandler(new UDPServerHandler());
        UDPserver.getSessionConfig();
        final InetSocketAddress p = new InetSocketAddress(port);
        UDPserver.bind(p);
        logger.info("Listener Started!");
    }
}