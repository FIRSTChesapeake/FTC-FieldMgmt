package MgrMain;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Field extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    final public static Logger         logger = LoggerFactory.getLogger(Main.class);

    public FieldDataPanel     DataPanel        = new FieldDataPanel();

    public FieldRobotsPanel   RobotPanel       = new FieldRobotsPanel();

    public int                FieldID          = 0;

    private long              LastHeard        = 0;
    private long              DeadDelay        = 5;
    private Timer             TickTime         = new Timer();
    private int               StartDelay       = 5;
    private int               LoopDelay        = 2;
    
    public Field(final int id) {
        FieldID = id;
        this.setLayout(new GridLayout(0, 1, 0, 0));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.add(DataPanel);

        this.add(RobotPanel);
        
        TickTime.schedule(new TimerTask() {
            @Override
            public void run() {
                if((System.currentTimeMillis() - LastHeard) > DeadDelay*1000){
                    // We're Dead
                    DataPanel.SetStatus("Offline!", Color.red);
                } else {
                    // We're alive
                    DataPanel.SetStatus("Online", Color.green);
                }
            }
        }, StartDelay*1000, LoopDelay*1000);
    }

    public void UpdateField(final FCSMsg msg) {

        DataPanel.UpdateField(msg);

        RobotPanel.UpdateField(msg);
        
        LastHeard = System.currentTimeMillis();
        
    }
}