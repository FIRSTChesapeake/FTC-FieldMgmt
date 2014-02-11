/**
 * 
 */
package MgrMain;

import java.awt.GridLayout;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class CalcPanel extends JPanel {
    final Logger                 logger           = LoggerFactory.getLogger(Main.class);
    /**
     * 
     */
    private static final long    serialVersionUID = 1L;
    private final JLabel         mainlbl          = new JLabel("");

    private final CheckOption    chkEnabled       = new CheckOption("Enable Timer", false);
    private final JPanel         blank            = new JPanel();

    private final NumericDisplay CycleTime        = new NumericDisplay("Cycle Time", 6, 4, 10, 1);
    private final NumericDisplay MatchCount       = new NumericDisplay("Match Count", 33, 1, 100, 1);

    private final DateDisplay    DayStart         = new DateDisplay("Day Start", 11, 0);
    private final DateDisplay    LunchStart       = new DateDisplay("Lunch Start", 12, 0);
    private final DateDisplay    LunchEnd         = new DateDisplay("Lunch End", 12, 5);
    private final DateDisplay    DayEnd           = new DateDisplay("Day End", 15, 0);

    private final Timer          TickTime         = new Timer();

    private boolean              initDone         = false;

    private int                  LastMatch        = 0;

    public CalcPanel() {
        TickTime.schedule(new TimerTask() {
            @Override
            public void run() {
                CalcPanel.this.CalcTime(LastMatch);
            }
        }, 1000, 10000);

        this.setLayout(new GridLayout(0, 2, 0, 0));

        this.add(chkEnabled);
        this.add(blank);
        this.add(CycleTime);
        this.add(MatchCount);
        this.add(DayStart);
        this.add(DayEnd);
        this.add(LunchStart);
        this.add(LunchEnd);
        this.add(mainlbl);

        initDone = true;
    }

    public boolean CalcTime(final int CurrentMatch) {
        if (!initDone) {
            logger.info("Scheduler Waiting for Window to be ready..");
            return false;
        }
        if (!chkEnabled.GetValue()) {
            // Skip the calc.. we're disabled.
            return true;
        }
        logger.info("Schedule Update Requested..");
        LastMatch = CurrentMatch;
        int i = 0;
        final int MatchCnt = MatchCount.getValue();
        final int CycleTm = CycleTime.getValue();
        final MatchTime[] Matches = new MatchTime[MatchCnt];
        try {
            final Date Start = DayStart.getValue();
            DayEnd.getValue();
            final Date LStart = LunchStart.getValue();
            final Date LEnd = LunchEnd.getValue();
            Date LastTime = Start;
            final Date now = new Date();
            logger.info("Building Schedule..");
            for (i = 0; i < MatchCnt; i++) {
                final Calendar cal = Calendar.getInstance();
                // TODO: Make this selectable.
                cal.setTimeZone(TimeZone.getTimeZone("EST"));
                cal.setTime(LastTime);
                if (i == 0) {
                    cal.add(Calendar.MINUTE, (CycleTm * -1));
                }
                cal.add(Calendar.MINUTE, CycleTm);
                LastTime = cal.getTime();
                // If we're in lunch, do it after
                if (LastTime.after(LStart) && LastTime.before(LEnd)) {
                    logger.info("Next Match Would start during Lunch. Moving on..");
                    LastTime = LEnd;
                }
                // If we're ending inside lunch, do it after
                cal.add(Calendar.MINUTE, CycleTm);
                LastTime = cal.getTime();
                if (LastTime.after(LStart) && LastTime.before(LEnd)) {
                    logger.info("Next Match Would end during Lunch. Moving on..");
                    LastTime = LEnd;
                }
                final MatchTime m = new MatchTime(i + 1, LastTime);
                Matches[i] = m;
                logger.info("Match " + m.MatchID + " = " + m.MatchStart);
            }
            logger.info("Current match: {}", CurrentMatch);
            final String Offset = this.timeDiff(now, Matches[CurrentMatch - 1].MatchStart);
            logger.info("Off schedule: {}", Offset);
            mainlbl.setText("Schedule offset: " + String.valueOf(Offset));
            return true;
        } catch (final IndexOutOfBoundsException e) {
            logger.error("FCS Sent Match Number that we aren't aware of.. We'll just wait to see what happens next time.");
            mainlbl.setText("Unavailable. Match ID too high!");
            return true;
        } catch (final ParseException e) {
            logger.error("Scheduler Parser Exception");
            mainlbl.setText("Unavailable. Parse Failed!");
            return true;
        } catch (final NullPointerException e) {
            logger.error("Scheduler Null Exception");
            mainlbl.setText("Unavailable. Null Pointer?");
            return true;
        }
    }

    private String timeDiff(final Date Date1, final Date Date2) {
        String str = "";
        if (Date1 == null || Date2 == null) {
            return "Can Not Calculate";
        }
        int mins = (int) ((Date2.getTime() / 60000) - (Date1.getTime() / 60000));
        final int hours = mins / 60;
        mins = mins % 60;
        if (mins < 10) {
            str = hours + ":0" + mins;
        }
        str = hours + ":" + mins;
        return str;
    }
}
