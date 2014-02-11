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
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class DateDisplay extends JPanel {
    final Logger                   logger           = LoggerFactory.getLogger(Main.class);
    /**
     * 
     */
    private final JLabel           lbl              = new JLabel("Uninitialized");

    private SpinnerModel           ModelH           = null; 
    private SpinnerModel           ModelM           = null;
    private JSpinner               ValH             = null;
    private JSpinner               ValM             = null;
    
    private String                 TZ               = "EST";

    private static final long      serialVersionUID = 1L;

    private final SimpleDateFormat df               = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public DateDisplay(final String name, int hour, int min, String TimeZone) {
        if(hour < 1) hour = 1;
        if(min  < 0) min  = 0;
        TZ = TimeZone;
        ModelH = new SpinnerNumberModel(hour, 1, 23, 1);
        ModelM = new SpinnerNumberModel(min, 0, 59, 15);
        ValH = new JSpinner(ModelH);
        ValM = new JSpinner(ModelM);
        lbl.setText(name);
        this.setLayout(new GridLayout(0, 3, 0, 0));
        this.add(lbl);
        this.add(ValH);
        this.add(ValM);
    }

    public Date getValue() throws ParseException {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(TZ));
        final String dateonly = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE);
        final String s = dateonly + " " + String.valueOf(ValH.getValue()) + ":" + String.valueOf(ValM.getValue()) ;
        final Date d = df.parse(s);
        return d;
    }

    public void setValue(final String value) throws ParseException {
        //TODO: Fix this - if I ever need it again.
        //final Date d = df.parse(value);
        //val.setValue(d);
        throw new ParseException("Not Implemented", 0);
    }

}
