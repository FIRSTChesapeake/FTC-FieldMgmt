/**
 * 
 */
package MgrMain;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class NumericDisplay extends JPanel {

    /**
     * 
     */
    private final JLabel      lbl              = new JLabel("Uninitialized");

    private SpinnerModel      Model            = null;
    private JSpinner          val              = null;

    private static final long serialVersionUID = 1L;

    public NumericDisplay(final String name, final double Value, final double Min, final double Max, final double Step) {
        lbl.setText(name);
        Model = new SpinnerNumberModel(Value, Min, Max, Step);
        val = new JSpinner(Model);
        val.setOpaque(true);
        this.setLayout(new GridLayout(0, 2, 0, 0));
        this.add(lbl);
        this.add(val);
    }

    public int getValue() {
        return (int) val.getValue();
    }

    public void setValue(final double value) {
        val.setValue(value);
    }

}
