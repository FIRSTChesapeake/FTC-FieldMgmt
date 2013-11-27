package MgrMain;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

    private void setColor(final Color clr) {
        this.setBackground(clr);
    }

    public void UpdateInfo(final Robot d) {
        data.setText(String.valueOf(d.TeamID));
        switch (d.Status) {
            case FCSMsg.TEAM_STATUS_OK:
                this.setColor(Color.green);
                break;
            case FCSMsg.TEAM_STATUS_WARNING:
                this.setColor(Color.yellow);
                break;
            case FCSMsg.TEAM_STATUS_ERROR:
            case FCSMsg.TEAM_STATUS_DISABLED:
                this.setColor(Color.red);
                break;
            default:
                this.setColor(Color.orange);
                break;
        }
        data.repaint();
    }
}
