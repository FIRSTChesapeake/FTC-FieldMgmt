package MgrMain;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import FTCMgrShared.*;
public class RobotPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final JLabel      data             = new JLabel();

    public RobotPanel() {
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        data.setFont(data.getFont().deriveFont(64.0f));
        this.add(data, BorderLayout.CENTER);
    }

    public void UpdateInfo(final Robot d) {
        data.setText(String.valueOf(d.TeamID));
        switch (d.Status) {
            case GREEN:
                this.setColor(Color.green);
                break;
            case YELLOW:
                this.setColor(Color.yellow);
                break;
            case RED:
                this.setColor(Color.red);
                break;
            default:
                this.setColor(Color.blue);
                break;
        }
        data.repaint();
    }

    private void setColor(final Color clr) {
        this.setBackground(clr);
    }
}
