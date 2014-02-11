/**
 * 
 */
package MyTCPServer;

import java.io.Serializable;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class TCPPack implements Serializable {
    private static final long serialVersionUID = 1L;
    public eAnswerStatus      AnswerState      = eAnswerStatus.NONE;
    public ePackType          PackType         = ePackType.NONE;

    public static enum eAnswerStatus {
        NONE, OK, FAIL
    }

    public static enum ePackType {
        NONE, REFRESH_REQUEST
    }
}
