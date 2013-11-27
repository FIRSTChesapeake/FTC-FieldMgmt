/**
 * 
 */
package MgrMain;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 * 
 *         FCS and Underlying UDP stream format (c) John Toebes
 */
public class FCSMsg {
    // Team Status Bytes
    public static final int TEAM_STATUS_OK                  = 0x0000;
    public static final int TEAM_STATUS_WARNING             = 0x0001;
    public static final int TEAM_STATUS_ERROR               = 0x0002;
    public static final int TEAM_STATUS_DISABLED            = 0x0003;
    // Match Types
    public static final int MATCH_TYPE_PRACTICE             = 0x0000;
    public static final int MATCH_TYPE_QUALIFICATION        = 0x0001;
    public static final int MATCH_TYPE_QUARTERFINAL         = 0x0002;
    public static final int MATCH_TYPE_SEMI_FINAL           = 0x0003;
    public static final int MATCH_TYPE_FINAL                = 0x0004;
    // Match Configs
    public static final int MATCH_CONFIG_AUTONOMOUS_ENABLED = 0x0001;
    public static final int MATCH_CONFIG_TELEOP_ENABLED     = 0x0002;
    public static final int MATCH_CONFIG_ENDGAME_ENABLED    = 0x0004;
    // Match States
    public static final int MATCH_STATE_AUTONOMOUS_WAITING  = 0x0000;
    public static final int MATCH_STATE_AUTONOMOUS_RUNNING  = 0x0001;
    public static final int MATCH_STATE_AUTONOMOUS_ENDED    = 0x0002;
    public static final int MATCH_STATE_TELEOP_WAITING      = 0x0010;
    public static final int MATCH_STATE_TELEOP_RUNNING      = 0x0011;
    public static final int MATCH_STATE_TELEOP_ENDED        = 0x0012;
    public static final int MATCH_STATE_ENDGAME_RUNNING     = 0x0020;
    public static final int MATCH_STATE_ENDGAME_ENDED       = 0x0040;

    public static final int BuildDword(final byte[] b, final int i) {
        final byte[] bb = { b[i], b[i + 1], b[i + 2], b[i + 3] };
        final int ret = java.nio.ByteBuffer.wrap(bb).getInt();
        return ret;
    }

    public int   iMessageID;
    public int   iKeyPart1;     // Using this field to identify the field
                                 // number (1 or 2)
    public int   iKeyPart2;
    public int   iDivisionID;
    public int   iMatchType;
    public int   iMatchConfig;
    public int   iMatchState;
    public int   iTimeRemaining;
    public int   iMatchNumber;

    public Robot R1;
    public Robot R2;
    public Robot B1;
    public Robot B2;

    public FCSMsg(final byte[] D) {
        iMessageID = BuildDword(D, 0);
        iKeyPart1 = BuildDword(D, 4);
        iKeyPart2 = BuildDword(D, 8);
        iDivisionID = BuildDword(D, 12);
        iMatchType = BuildDword(D, 16);
        iMatchConfig = BuildDword(D, 20);
        iMatchState = BuildDword(D, 24);
        iTimeRemaining = BuildDword(D, 28);
        iMatchNumber = BuildDword(D, 32);

        R1 = new Robot(BuildDword(D, 36), BuildDword(D, 40));
        R2 = new Robot(BuildDword(D, 44), BuildDword(D, 48));
        B1 = new Robot(BuildDword(D, 52), BuildDword(D, 56));
        B2 = new Robot(BuildDword(D, 60), BuildDword(D, 64));
    }

    public String MatchConfig() {
        switch (iMatchConfig) {
            case MATCH_CONFIG_AUTONOMOUS_ENABLED:
                return "AUTO";
            case MATCH_CONFIG_TELEOP_ENABLED:
                return "TELE";
            case MATCH_CONFIG_ENDGAME_ENABLED:
                return "ENDING";
            default:
                return "Unknown?";
        }
    }

    public String MatchState() {
        switch (iMatchState) {
            case MATCH_STATE_AUTONOMOUS_WAITING: // 1
                return "Waiting for Start";
            case MATCH_STATE_AUTONOMOUS_RUNNING: // 2
                return "Auto Running";
            case MATCH_STATE_AUTONOMOUS_ENDED: // Not Sent
                return "Auto End";
            case MATCH_STATE_TELEOP_WAITING: // 3
                return "Tele Waiting";
            case MATCH_STATE_TELEOP_RUNNING: // 4
                return "Tele Running";
            case MATCH_STATE_ENDGAME_RUNNING: // 5
                return "Endgame";
            case MATCH_STATE_TELEOP_ENDED: // Not Sent
                return "Tele End";
            case MATCH_STATE_ENDGAME_ENDED: // 6
                return "Game End";
            default:
                return "Unknown State = " + String.valueOf(iMatchState);
        }
    }

    public String MatchType() {
        switch (iMatchType) {
            case MATCH_TYPE_PRACTICE:
                return "Practice";
            case MATCH_TYPE_QUALIFICATION:
                return "Qualification";
            case MATCH_TYPE_QUARTERFINAL:
                return "Quarterfinal";
            case MATCH_TYPE_SEMI_FINAL:
                return "Semifinal";
            case MATCH_TYPE_FINAL:
                return "Final";
            default:
                return "Unknown Type = " + String.valueOf(iMatchType);
        }
    }

    public boolean SmoothMatch(final int Last) {
        int Next = -1;
        switch (Last) {
            case MATCH_STATE_AUTONOMOUS_WAITING:
                Next = MATCH_STATE_AUTONOMOUS_RUNNING;
                break;
            case MATCH_STATE_AUTONOMOUS_RUNNING:
                Next = MATCH_STATE_TELEOP_WAITING;
                break;
            case MATCH_STATE_TELEOP_WAITING:
                Next = MATCH_STATE_TELEOP_RUNNING;
                break;
            case MATCH_STATE_TELEOP_RUNNING:
                Next = MATCH_STATE_ENDGAME_RUNNING;
                break;
            case MATCH_STATE_ENDGAME_RUNNING:
                Next = MATCH_STATE_ENDGAME_ENDED;
                break;
            case MATCH_STATE_ENDGAME_ENDED:
                Next = MATCH_STATE_AUTONOMOUS_WAITING;
                break;
            default:
                Next = -1;
                break;
        }
        if (iMatchState == Next) {
            return true;
        } else {
            return false;
        }
    }

    public String TimeRemaining() {
        int mins = 0;
        int secs = iTimeRemaining / 1000;
        if (secs >= 60) {
            mins = secs / 60;
            secs = secs % 60;
        }
        final String m = String.format("%02d", mins);
        final String s = String.format("%02d", secs);
        return String.valueOf(m + ":" + s);
    }
}
