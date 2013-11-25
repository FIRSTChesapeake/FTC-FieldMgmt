package Main;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RobotPanel extends JPanel {

    private JLabel data = new JLabel();

    public RobotPanel(){
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        data.setFont (data.getFont ().deriveFont (64.0f));
        this.add(data, BorderLayout.CENTER);
    }

    public void UpdateInfo(Robot d){
        data.setText(String.valueOf(d.TeamID));
        switch(d.Status){
            case FCSMsg.TEAM_STATUS_OK:
                setColor(Color.green);
                break;
            case FCSMsg.TEAM_STATUS_WARNING:
                setColor(Color.yellow);
                break;
            case FCSMsg.TEAM_STATUS_ERROR:
            case FCSMsg.TEAM_STATUS_DISABLED:
                setColor(Color.red);
                break;
            default:
                setColor(Color.orange);
                break;
        }
        data.repaint();
    }
    private void setColor(Color clr){
        this.setBackground(clr);
    }
}
