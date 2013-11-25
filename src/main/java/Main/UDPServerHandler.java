/**
 * 
 */
package Main;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 * https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class UDPServerHandler extends IoHandlerAdapter{
	final public static Logger logger = LoggerFactory.getLogger(Main.class);
	
	private int LastState = -1;
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception{
		logger.error("ERROR: {}",cause);
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception{
		byte[] by = ((IoBuffer) message).array();
		FCSMsg msg = new FCSMsg(by);
		if(msg.iMatchState != LastState){
			if(LastState != -1){
				if(!msg.SmoothMatch(LastState)) logger.info("BAD MATCH!");
			}
			LastState = msg.iMatchState;
			logger.info(msg.MatchState());
		}
	}
}
