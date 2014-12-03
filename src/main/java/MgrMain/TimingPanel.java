/**
 * 
 */
package MgrMain;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class TimingPanel extends JPanel {
    final Logger logger = LoggerFactory.getLogger(Main.class);
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param args
     */

    private final JPanel TopPanel = new JPanel();
    private final JPanel BotPanel = new JPanel();

    private final StandardClock clock = new StandardClock();
    private final CalcPanel timer = new CalcPanel();

    public TimingPanel() {
        this.setLayout(new GridLayout(0, 1, 0, 0));
        this.TopPanel.setLayout(new GridLayout(0, 1, 0, 0));
        this.BotPanel.setLayout(new GridLayout(0, 1, 0, 0));
        this.TopPanel.add(this.clock);

        this.BotPanel.add(this.timer);

        this.add(this.TopPanel);
        this.add(this.BotPanel);
    }

    public boolean UpdateSchedule(final int CurrentMatch) {
        return this.timer.CalcTime(CurrentMatch);
    }
}
