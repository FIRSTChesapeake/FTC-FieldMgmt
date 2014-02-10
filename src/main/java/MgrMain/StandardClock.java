/**
 * 
 */
package MgrMain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 * https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class StandardClock extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * @param args
     */
    private JLabel            lbl              = new JLabel("");
    private Timer             TickTime         = new Timer();
    private final DateFormat  df               = new SimpleDateFormat("HH:mm:ss");
    
    public StandardClock(){
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lbl.setFont(lbl.getFont().deriveFont(64.0f));
        this.add(lbl, BorderLayout.CENTER);
        this.add(lbl);
        TickTime.schedule(new TimerTask() {
            @Override
            public void run() {
                Date now = new Date();
                String out = df.format(now);
                lbl.setText(out);
            }
        }, 1000, 1000);
    }
}
