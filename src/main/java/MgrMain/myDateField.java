/**
 * 
 */
package MgrMain;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class myDateField extends JFormattedTextField {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public myDateField() {
        this.setValue(new Date());
        this.setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(new SimpleDateFormat("yyyy-MM-dd HH:mm"))));
        this.setValue(Calendar.getInstance().getTime());

        this.addPropertyChangeListener("value", new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {

            }
        });
    }
}
