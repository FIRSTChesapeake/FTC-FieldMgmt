/**
 * 
 */
package MgrMain;

import java.awt.GridLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 * https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class DateDisplay extends JPanel {
    final Logger              logger           = LoggerFactory.getLogger(Main.class);
    /**
     * 
     */
    private final JLabel                        lbl              = new JLabel("Uninitialized");

    private final myDateField                    val              = new myDateField();
    
    private static final long serialVersionUID = 1L;
    
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public DateDisplay(String name, int hour, int min){
        lbl.setText(name);
        val.setOpaque(true);
        try {
            int AM = 0;
            if(hour >12){
                AM = 1;
                hour = hour - 12;
            }
            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("EST"));
            cal.set(Calendar.HOUR, hour);
            cal.set(Calendar.MINUTE, min);
            cal.set(Calendar.AM_PM, AM);
            val.setValue(cal.getTime());
        } catch (Exception e) {
            logger.error("Date Picker couldn't set default date {}",hour+":"+min);
        }
        this.setLayout(new GridLayout(0, 2, 0, 0));
        this.add(lbl);
        this.add(val);
    }
    public Date getValue() throws ParseException{
        String s = val.getText();
        Date d = df.parse(s);
        return d;
    }
    public void setValue(String value) throws ParseException{
        Date d = df.parse(value);
        val.setValue(d);
    }
    
}
