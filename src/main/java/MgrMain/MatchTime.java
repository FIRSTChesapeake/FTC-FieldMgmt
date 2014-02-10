/**
 * 
 */
package MgrMain;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 * https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
import java.util.Date;

public class MatchTime {
    public int MatchID = 0;
    public Date MatchStart = new Date();
    
    public MatchTime(int id, Date start){
        MatchID = id;
        MatchStart = start;
    }
}
