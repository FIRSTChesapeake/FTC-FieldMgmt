/**
 * 
 */
package MgrMain;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class CalcPanel extends JPanel {
    protected class BtnPress implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            final Object obj = e.getSource();
            final JButton b = (JButton) obj;
            if (b.equals(btnRefresh)) {
                CalcPanel.this.CalcTime(LastMatch);
            }
        }

    }

    protected class OffsetAnswer {
        public int     intHours = 0;
        public int     intMins  = 0;
        public String  strText  = "";
        public boolean isNeg    = false;

        public OffsetAnswer() {

        }

        public OffsetAnswer(final int iHours, final int iMins, final String iText, final boolean iNeg) {
            intHours = iHours;
            intMins = iMins;
            strText = iText;
            isNeg = iNeg;
        }
    }

    final Logger                 logger           = LoggerFactory.getLogger(Main.class);
    /**
     * 
     */
    private static final long    serialVersionUID = 1L;

    // TODO: Make this adjustable
    private final String         TZ               = "EST";

    private final JLabel         mainlbl          = new JLabel("");
    private final JButton        btnRefresh       = new JButton("Refresh");
    private final CheckOption    chkEnabled       = new CheckOption("Enable Scheduling", false);

    private final CheckOption    chkDebug         = new CheckOption("Enable Debug Output", false);

    private final CheckOption    chkAuto          = new CheckOption("Refresh on Timer", false);
    private final JPanel         blank1            = new JPanel();
    private final JPanel         blank2            = new JPanel();
    
    private final NumericDisplay CycleTime        = new NumericDisplay("Cycle Time", 6, 4, 10, 1);
    private final NumericDisplay MatchCount       = new NumericDisplay("Match Count", 33, 1, 100, 1);
    private final DateDisplay    DayStart         = new DateDisplay("Day Start", 11, 0, TZ);
    private final DateDisplay    LunchStart       = new DateDisplay("Lunch Start", 12, 0, TZ);
    private final DateDisplay    LunchEnd         = new DateDisplay("Lunch End", 12, 30, TZ);


    private final Timer          TickTime         = new Timer();

    private boolean              initDone         = false;

    private int                  LastMatch        = 0;

    public CalcPanel() {
        TickTime.schedule(new TimerTask() {
            @Override
            public void run() {
                if (chkAuto.GetValue()) {
                    CalcPanel.this.CalcTime(LastMatch);
                }
            }
        }, 1000, 10000);

        this.setLayout(new GridLayout(0, 2, 0, 0));

        btnRefresh.addActionListener(new BtnPress());

        mainlbl.setOpaque(true);

        this.add(chkEnabled);
        this.add(blank1);
        this.add(chkAuto);
        this.add(chkDebug);
        this.add(CycleTime);
        this.add(MatchCount);
        this.add(DayStart);
        this.add(blank2);
        this.add(LunchStart);
        this.add(LunchEnd);
        this.add(mainlbl);
        this.add(btnRefresh);

        initDone = true;
    }

    public boolean CalcTime(final int CurrentMatch) {
        LastMatch = CurrentMatch;
        if (!initDone) {
            logger.info("Scheduler Waiting for Window to be ready..");
            return false;
        }
        if (!chkEnabled.GetValue()) {
            if (chkDebug.GetValue()) {
                logger.info("Skipping calculations. We're disabled.");
            }
            return true;
        }
        if (chkDebug.GetValue()) {
            logger.info("Schedule Update Requested..");
        }
        int i = 0;
        final int MatchCnt = MatchCount.getValue();
        final int CycleTm = CycleTime.getValue();
        final MatchTime[] Matches = new MatchTime[MatchCnt];
        try {
            final Date Start = DayStart.getValue();
            final Date LStart = LunchStart.getValue();
            final Date LEnd = LunchEnd.getValue();
            Date LastTime = Start;
            final Date now = new Date();

            if (chkDebug.GetValue()) {
                logger.info("  It is now  : {}.", now);
                logger.info("  Day Start  : {}.", Start);
                logger.info("  Lunch Start: {}.", LStart);
                logger.info("  Lunch End  : {}.", LEnd);
            }

            if (CurrentMatch < 1 || CurrentMatch > MatchCnt) {
                // Don't bother trying - the match number we're getting isn't
                // valid.
                if (chkDebug.GetValue()) {
                    logger.info("Skipping calculations. The Match number is OOB.");
                }
                mainlbl.setText("Unavailable. Match nmbr not valid!");
                return true;
            }

            if (chkDebug.GetValue()) {
                logger.info("Building Schedule..");
            }
            for (i = 0; i < MatchCnt; i++) {
                final Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone(TZ));
                cal.setTime(LastTime);
                // If this is the first match, don't add the cycle time.
                if (i != 0) {
                    cal.add(Calendar.MINUTE, CycleTm);
                }
                LastTime = cal.getTime();
                // If we're in lunch, do it after
                if (LastTime.after(LStart) && LastTime.before(LEnd)) {
                    if (chkDebug.GetValue()) {
                        logger.info("Next Match Would start during Lunch. Moving on..");
                    }
                    LastTime = LEnd;
                }
                // If we're ending inside lunch, do it after
                cal.add(Calendar.MINUTE, CycleTm);
                final Date NextTime = cal.getTime();
                if (NextTime.after(LStart) && NextTime.before(LEnd)) {
                    if (chkDebug.GetValue()) {
                        logger.info("Next Match Would end during Lunch. Moving on..");
                    }
                    LastTime = LEnd;
                }
                final MatchTime m = new MatchTime(i + 1, LastTime);
                Matches[i] = m;
                if (chkDebug.GetValue()) {
                    logger.info("Match " + m.MatchID + " = " + m.MatchStart);
                }
            }
            if (chkDebug.GetValue()) {
                logger.info("Current match: {}", CurrentMatch);
            }
            final Date SchedStart = Matches[CurrentMatch - 1].MatchStart;
            final OffsetAnswer Offset = this.timeDiff(now, SchedStart);

            if (chkDebug.GetValue()) {
                logger.info("Scheduled Time for Match is {}", String.valueOf(SchedStart));
                if (Offset.isNeg) {
                    logger.info("Behind schedule: {}", Offset.intHours + " : " + Offset.intMins);
                }
                if (!Offset.isNeg) {
                    logger.info("Ahead schedule: {}", Offset.intHours + " : " + Offset.intMins);
                }
            }
            if (Offset.isNeg) {
                this.SetMainLbl("Schedule offset: " + Offset.strText, Color.red);
            }
            if (!Offset.isNeg) {
                this.SetMainLbl("Schedule offset: " + Offset.strText, Color.GREEN);
            }
            return true;
        } catch (final IndexOutOfBoundsException e) {
            logger.error("Unhandled OOB Exception in Scheduler?");
            mainlbl.setText("Unavailable. OOB Error!");
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

    private void SetMainLbl(final String text, final Color bg) {
        mainlbl.setBackground(bg);
        mainlbl.setText(text);
    }

    private OffsetAnswer timeDiff(final Date Date1, final Date Date2) {
        String str = "";
        String strMins = "";
        if (chkDebug.GetValue()) {
            logger.info("Starting Calculation..");
        }
        if (Date1 == null || Date2 == null) {
            return new OffsetAnswer(-1, -1, "Null Found?", true);
        }
        int mins = (int) ((Date2.getTime() / 60000) - (Date1.getTime() / 60000));
        if (chkDebug.GetValue()) {
            logger.info("  ALL MINS = {}", mins);
        }
        int hours = mins / 60;
        if (chkDebug.GetValue()) {
            logger.info("  HOURS = {}", hours);
        }
        mins = mins % 60;
        if (chkDebug.GetValue()) {
            logger.info("  MOD MINS = {}", mins);
        }
        boolean isNeg = false;
        if (mins < 0) {
            if (chkDebug.GetValue()) {
                logger.info("  Mins were found to be neg");
            }
            isNeg = true;
            mins = mins * -1;
        }
        if (hours < 0) {
            if (chkDebug.GetValue()) {
                logger.info("  Hours were found to be neg");
            }
            isNeg = true;
            hours = hours * -1;
        }

        strMins = String.valueOf(mins);
        if (mins < 10) {
            if (chkDebug.GetValue()) {
                logger.info("  Mins were less than 10.");
            }
            strMins = "0" + String.valueOf(mins);
        }

        if (chkDebug.GetValue()) {
            logger.info("  Building String...");
        }
        if (isNeg) {
            str = "-" + String.valueOf(hours) + ":" + strMins;
        }
        if (!isNeg) {
            str = String.valueOf(hours) + ":" + strMins;
        }

        if (chkDebug.GetValue()) {
            logger.info("Calcuation Complete");
        }
        return new OffsetAnswer(hours, mins, str, isNeg);
    }
}
