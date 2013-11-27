package MgrMain;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldDataPanel extends JPanel {

    /**
     * 
     */
    private static final long    serialVersionUID = 1L;

    final public static Logger   logger           = LoggerFactory.getLogger(Main.class);

    private final GenericDisplay a1               = new GenericDisplay("Field ID");
    private final GenericDisplay a2               = new GenericDisplay("Match Type");
    private final GenericDisplay a3               = new GenericDisplay("Match State");
    private final GenericDisplay a4               = new GenericDisplay("Time Remaining");
    private final GenericDisplay a5               = new GenericDisplay("Match Number");
    
    private final CheckOption    PlaySounds       = new CheckOption("Play Sounds",true);

    private int                  LastState        = -1;
    private boolean              InitDone         = false;

    public FieldDataPanel() {
        this.setLayout(new GridLayout(0, 1, 0, 0));
        this.add(a1);
        this.add(a2);
        this.add(a3);
        this.add(a4);
        this.add(a5);
        
        this.add(PlaySounds);
    }

    public void UpdateField(final FCSMsg msg) {
        a1.UpdateDisplay(msg.iKeyPart1);
        a2.UpdateDisplay(msg.MatchType());

        Color StateColor = Color.yellow;

        if (msg.iMatchState != FCSMsg.MATCH_STATE_AUTONOMOUS_WAITING) {
            StateColor = Color.green;
        }

        if (msg.iMatchState != LastState) {
            // If we're playing sounds at all..
            if (InitDone && PlaySounds.GetValue()) {
                // FOG HORN
                if (!msg.SmoothMatch(LastState)) {
                    SoundGen.playSound("FOGHORN");
                    logger.info("BAD MATCH!");
                    a1.Blink(Color.red, 1000);
                } else {
                    // Generate the rest of the game sounds
                    switch(msg.iMatchState){
                        case FCSMsg.MATCH_STATE_AUTONOMOUS_WAITING:
                            // Acceptable mode - but we play no sound
                            break;
                        case FCSMsg.MATCH_STATE_AUTONOMOUS_RUNNING:
                            SoundGen.playSound("CHARGE");
                            break;
                        case FCSMsg.MATCH_STATE_TELEOP_WAITING:
                            SoundGen.playSound("ENDAUTON");
                            break;
                        case FCSMsg.MATCH_STATE_TELEOP_RUNNING:
                            SoundGen.playSound("3BELLS");
                            break;
                        case FCSMsg.MATCH_STATE_ENDGAME_RUNNING:
                            SoundGen.playSound("WARNEOM");
                            break;
                        case FCSMsg.MATCH_STATE_ENDGAME_ENDED:
                            SoundGen.playSound("ENDMATCH");
                            break;
                        default:
                            logger.error("Unexpected Mode received in the FieldDataPanel class");
                            break;
                    }
                }
            }
            LastState = msg.iMatchState;
            logger.info("Field " + String.valueOf(msg.iKeyPart1) + " says: " + msg.MatchState());
        }

        a3.UpdateDisplay("(" + msg.MatchConfig() + ") " + msg.MatchState(), StateColor);

        a4.UpdateDisplay(msg.TimeRemaining());
        a5.UpdateDisplay(msg.iMatchNumber);
        InitDone = true;
    }

}
