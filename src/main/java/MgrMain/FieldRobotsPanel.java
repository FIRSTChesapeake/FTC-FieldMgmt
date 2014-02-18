package MgrMain;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
public class FieldRobotsPanel extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    RobotPanel                R1               = new RobotPanel();
    RobotPanel                R2               = new RobotPanel();
    RobotPanel                B1               = new RobotPanel();
    RobotPanel                B2               = new RobotPanel();

    public FieldRobotsPanel() {
        this.setLayout(new GridLayout(0, 2, 0, 0));
        this.add(R1);
        this.add(B1);
        this.add(R2);
        this.add(B2);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    }

    public void UpdateField(final FCSMsg msg) {
        R1.UpdateInfo(msg.R1);
        R2.UpdateInfo(msg.R2);
        B1.UpdateInfo(msg.B1);
        B2.UpdateInfo(msg.B2);
    }
}
