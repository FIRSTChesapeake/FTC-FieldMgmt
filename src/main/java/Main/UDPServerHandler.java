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
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class UDPServerHandler extends IoHandlerAdapter {
    final public static Logger logger    = LoggerFactory.getLogger(Main.class);
    
    private int                LastState = -1;

    @Override
    public void exceptionCaught(final IoSession session, final Throwable cause) throws Exception {
        logger.error("ERROR: {}", cause.getClass()+" - "+cause.getMessage());
    }

    @Override
    public void messageReceived(final IoSession session, final Object message) throws Exception {
        final byte[] by = ((IoBuffer) message).array();
        final FCSMsg msg = new FCSMsg(by);
        if (msg.iMatchState != LastState) {
            if (LastState != -1) {
                if (!msg.SmoothMatch(LastState)) {
                    SoundGen.playSound("FOGHORN");
                    logger.info("BAD MATCH!");
                }
            }
            LastState = msg.iMatchState;
            logger.info("Field "+String.valueOf(msg.iKeyPart1)+" says: "+msg.MatchState());
        }
        Main.MWind.UpdateField(msg);
    }
}
