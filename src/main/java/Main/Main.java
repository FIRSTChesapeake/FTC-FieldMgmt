/**
 * 
 */
package Main;

import org.apache.ftpserver.*;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 * https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class Main {

	final static Logger logger = LoggerFactory.getLogger(Main.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			StartFTP();
		} catch(FtpException ex){
			System.out.println("FTP failed");
		}
	}

	public static void StartFTP() throws FtpException{
		FtpServerFactory serverFactory = new FtpServerFactory();
        
		ListenerFactory factory = new ListenerFactory();
		         
		// set the port of the listener
		factory.setPort(2221);
		 
		// replace the default listener
		serverFactory.addListener("default", factory.createListener());
		                 
		// start the server
		FtpServer server = serverFactory.createServer(); 
		
		server.start();
	}
}