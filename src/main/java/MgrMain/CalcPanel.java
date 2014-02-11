/**
 * 
 */
package MgrMain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 * https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class CalcPanel extends JPanel {
    final Logger              logger           = LoggerFactory.getLogger(Main.class);
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JLabel mainlbl = new JLabel("");
    
    private CheckOption chkEnabled     = new CheckOption("Enable Timer", false);
    private JPanel      blank         = new JPanel();
    
    private NumericDisplay CycleTime  = new NumericDisplay("Cycle Time",6,4,10,1);
    private NumericDisplay MatchCount = new NumericDisplay("Match Count",33,1,100,1);
    
    private DateDisplay DayStart      = new DateDisplay("Day Start",11,0);
    private DateDisplay LunchStart    = new DateDisplay("Lunch Start", 12,0);
    private DateDisplay LunchEnd      = new DateDisplay("Lunch End", 12,5);
    private DateDisplay DayEnd        = new DateDisplay("Day End", 15,0);
    
    private Timer             TickTime         = new Timer(); 
    
    private boolean initDone = false;
    
    private int LastMatch = 0;
    
    public CalcPanel(){
        TickTime.schedule(new TimerTask() {
            @Override
            public void run() {
                CalcTime(LastMatch);
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
    public boolean CalcTime(int CurrentMatch){
        if (!initDone) {
            logger.info("Scheduler Waiting for Window to be ready..");
            return false;
        }
        if(!chkEnabled.GetValue()){
            // Skip the calc.. we're disabled.
            return true;
        }
        logger.info("Schedule Update Requested..");
        LastMatch = CurrentMatch;
        int i = 0;
        final int MatchCnt = MatchCount.getValue();
        final int CycleTm = CycleTime.getValue();
        MatchTime[] Matches = new MatchTime[MatchCnt];
        try {
            Date Start = DayStart.getValue();
            Date End = DayEnd.getValue();
            Date LStart = LunchStart.getValue();
            Date LEnd = LunchEnd.getValue();
            Date LastTime = Start;
            Date now = new Date();
            logger.info("Building Schedule..");
            for(i=0;i<MatchCnt;i++){
                Calendar cal = Calendar.getInstance();
                //TODO: Make this selectable.
                cal.setTimeZone(TimeZone.getTimeZone("EST"));
                cal.setTime(LastTime);
                if(i == 0) cal.add(Calendar.MINUTE, (CycleTm*-1));
                cal.add(Calendar.MINUTE, CycleTm);
                LastTime = cal.getTime();
                // If we're in lunch, do it after
                if(LastTime.after(LStart) && LastTime.before(LEnd)){
                    logger.info("Next Match Would start during Lunch. Moving on..");
                    LastTime = LEnd;
                }
                // If we're ending inside lunch, do it after
                cal.add(Calendar.MINUTE, CycleTm);
                LastTime = cal.getTime();
                if(LastTime.after(LStart) && LastTime.before(LEnd)){
                    logger.info("Next Match Would end during Lunch. Moving on..");
                    LastTime = LEnd;
                }
                MatchTime m = new MatchTime(i+1,LastTime);
                Matches[i] = m;
                logger.info("Match "+m.MatchID+" = "+m.MatchStart);
            }
            logger.info("Current match: {}",CurrentMatch);
            String Offset = timeDiff(now, Matches[CurrentMatch-1].MatchStart);
            logger.info("Off schedule: {}", Offset);
            mainlbl.setText("Schedule offset: " + String.valueOf(Offset));
            return true;
        } catch (IndexOutOfBoundsException e){
            logger.error("FCS Sent Match Number that we aren't aware of.. We'll just wait to see what happens next time.");
            mainlbl.setText("Unavailable. Match ID too high!");
            return true;
        } catch (ParseException e) {
            logger.error("Scheduler Parser Exception");
            mainlbl.setText("Unavailable. Parse Failed!");
            return true;
        } catch (NullPointerException e) {
            logger.error("Scheduler Null Exception");
            mainlbl.setText("Unavailable. Null Pointer?");
            return true;
        }
    }
    private String timeDiff(Date Date1, Date Date2){
        String str = "";
        if(Date1 == null || Date2 == null) return "Can Not Calculate";
        int mins = (int) ((Date2.getTime()/60000) - (Date1.getTime()/60000));
        int hours = (int) mins / 60;
        mins = (int) mins % 60;
        if(mins < 10){
            str = hours+":0"+mins;
        }
        str = hours+":"+mins;
        return str;
    }
}
