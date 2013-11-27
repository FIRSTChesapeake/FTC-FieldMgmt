package MgrMain;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Field extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public FieldDataPanel     DataPanel        = new FieldDataPanel();

    public FieldRobotsPanel   RobotPanel       = new FieldRobotsPanel();

    public int                FieldID          = 0;

    public Field(final int id) {
        FieldID = id;
        this.setLayout(new GridLayout(0, 1, 0, 0));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.add(DataPanel);

        this.add(RobotPanel);
    }

    public void UpdateField(final FCSMsg msg) {

        DataPanel.UpdateField(msg);

        RobotPanel.UpdateField(msg);
    }
}