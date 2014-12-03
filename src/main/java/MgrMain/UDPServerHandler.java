/**
 * 
 */
package MgrMain;

import java.net.SocketAddress;

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
    public enum udpPackType {
        FCS,
        Client
    }

    final public static Logger logger = LoggerFactory.getLogger(Main.class);

    final private udpPackType typeFlag;

    public UDPServerHandler(final udpPackType type) {
        this.typeFlag = type;
    }

    @Override
    public void exceptionCaught(final IoSession session, final Throwable cause) throws Exception {
        logger.error("ERROR: {}", cause.getClass() + " - " + cause.getMessage());
    }

    @Override
    public void messageReceived(final IoSession session, final Object message) throws Exception {
        final SocketAddress RemoteIP = session.getRemoteAddress();
        final String StrAddress = RemoteIP.toString();
        switch (this.typeFlag) {
            case FCS:
                final byte[] by = ((IoBuffer) message).array();
                final FCSMsg fMsg = new FCSMsg(by, StrAddress);
                Main.MWind.UpdateField(fMsg);
                break;
            case Client:
                // final ClientPack cMsg = (ClientPack) message;
                // Main.MWind.UpdateField(cMsg);
        }

    }

    public void SendData(final Object message) {

        this.SendData(message);
    }
}
