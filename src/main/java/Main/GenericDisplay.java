package Main;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GenericDisplay extends JPanel {
    
    JLabel lbl = new JLabel("Uninitialized");
    JLabel val = new JLabel("Uninitialized");
    
    public GenericDisplay(String name){
        lbl.setText(name);
        val.setOpaque(true);
        this.setLayout(new GridLayout(0, 2, 0, 0));
        this.add(lbl);
        this.add(val);
    }
    
    public void UpdateDisplay(int value){
        UpdateDisplay(value, this.getBackground());
    }
    
    public void UpdateDisplay(String value){
        UpdateDisplay(value, this.getBackground());
    }
    
    public void UpdateDisplay(String value, Color clr){
        val.setText(value);
        val.setBackground(clr);
    }
    public void UpdateDisplay(int value, Color clr){
        val.setText(String.valueOf(value));
        val.setBackground(clr);
    }
}
