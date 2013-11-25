package Main;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class FieldDataPanel extends JPanel {

    private GenericDisplay a1 = new GenericDisplay("Field ID");
    private GenericDisplay a2 = new GenericDisplay("Match Type");
    private GenericDisplay a3 = new GenericDisplay("Match State");
    private GenericDisplay a4 = new GenericDisplay("Time Remaining");
    private GenericDisplay a5 = new GenericDisplay("Match Number");
    
    public FieldDataPanel(){
        this.setLayout(new GridLayout(0, 1, 0, 0));
        this.add(a1);
        this.add(a2);
        this.add(a3);
        this.add(a4);
        this.add(a5);
    }
    
    public void UpdateField(FCSMsg msg){
        a1.UpdateDisplay(msg.iKeyPart1);
        a2.UpdateDisplay(msg.MatchType());
        
        Color StateColor = Color.yellow;
        
        if(msg.iMatchState!=FCSMsg.MATCH_STATE_AUTONOMOUS_WAITING) StateColor = Color.green; 
        a3.UpdateDisplay("("+msg.MatchConfig()+") "+msg.MatchState(),StateColor);
        
        a4.UpdateDisplay(msg.TimeRemaining());
        a5.UpdateDisplay(msg.iMatchNumber);
    }
}
