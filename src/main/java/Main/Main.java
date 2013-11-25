/**
 * 
 */
package Main;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.ftpserver.*;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.*;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.UserFactory;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.ftpserver.*;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 * https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class Main {

	final public static Logger logger = LoggerFactory.getLogger(Main.class);
	private static FtpServer FTPserver;
	private static NioDatagramAcceptor UDPserver;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			int FTPport = 2211;
			int UDPport = 2212;
			StartFTP(FTPport);
			StartUDP(UDPport);
		} catch(IOException ex){
			logger.error("UDP Start failed with error {}",ex.getMessage());
		} catch(FtpException ex){
			logger.info("FTP Start failed with error {}",ex.getMessage());
		}
	}

	public static void StartFTP(int FTPport) throws FtpException{
		logger.info("Starting FTP Server on port {}",FTPport);
		FtpServerFactory serverFactory = new FtpServerFactory();
		ListenerFactory factory = new ListenerFactory();
		factory.setPort(FTPport);
		serverFactory.addListener("default", factory.createListener());
		UserManager um = SetupPassword("apple");
		serverFactory.setUserManager(um);
		FTPserver = serverFactory.createServer();
		FTPserver.start();
	}
	public static void StopFTP(){
		FTPserver.stop();
	}
	private static UserManager SetupPassword(String pass) throws FtpException{
		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        UserManager um = userManagerFactory.createUserManager();
        um.save(MakeUser("field1",pass));
        um.save(MakeUser("field2",pass));
        um.save(MakeUser("field3",pass));
        um.save(MakeUser("field4",pass));
        return um;
	}
	private static BaseUser MakeUser(String username, String password){
		BaseUser Buser = new BaseUser();
		//TODO: This isn't working properly. No Write Access?
        Buser.setName(username);
        Buser.setPassword(password);
        Buser.setHomeDirectory("ftproot");
        return Buser;
	}
	private static void StartUDP(int port) throws IOException{
		logger.info("Starting UDP Listener on port {}",port);
		UDPserver = new NioDatagramAcceptor();	
		UDPserver.setHandler(new UDPServerHandler());
		DatagramSessionConfig dgsc = UDPserver.getSessionConfig();
		InetSocketAddress p = new InetSocketAddress(port);
		UDPserver.bind(p);
		logger.info("Listener Started!");
	}
}