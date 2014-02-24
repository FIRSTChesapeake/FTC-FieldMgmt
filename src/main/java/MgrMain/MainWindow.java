/**
 * 
 */
package MgrMain;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FTCMgrShared.*;
import SoundGenerator.SoundTestWindow;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    final String              AppTitle         = "FTC Field Mgmt Overview System";
    final Logger              logger           = LoggerFactory.getLogger(Main.class);

    private final Field       Field1           = new Field(1);
    private final Field       Field2           = new Field(2);

    private final TimingPanel clock            = new TimingPanel();

    private int               LastMatch        = 0;

    private ClientPack		  LastPack		   = null;
    
    private Timer			  packTime		   = new Timer();
    private long			  packDelay		   = 3;
    
    
    public MainWindow() {
        
    	final TimerTask packTask = new TimerTask() {
            @Override
            public void run() {
            	if(LastPack != null){
            	    logger.info("tick");
            	}
            }
        };
    	packTime.schedule(packTask, packDelay * 1000, packDelay * 1000);
    	
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                logger.info("Main Window is closing.");
                Main.Quit();
            }
        });
        this.setSize(1000, 500);
        this.setLayout(new GridLayout(0, 2, 0, 0));

        this.getContentPane().add(Field1);
        this.getContentPane().add(Field2);

        final SoundTestWindow Testframe = new SoundTestWindow();
        this.getContentPane().add(Testframe);

        this.getContentPane().add(clock);

        this.invalidate();
    }
    
    public void UpdateField(final FCSMsg msg) {
        ClientPack pack = new ClientPack();
        if(msg.iMatchState == FCSMsg.MATCH_STATE_TELEOP_WAITING){
        	pack.FieldState = false;
        } else {
        	pack.FieldState = true;
        }
        pack.R1 = msg.R1;
        pack.R2 = msg.R2;
        pack.B1 = msg.B1;
        pack.B2 = msg.B2;
        pack.FieldID = msg.iKeyPart1;
        pack.PackType = ClientPack.ePackType.REFRESH;
        LastPack = pack;
        
    	if (msg.iKeyPart1 == 1) {
            Field1.UpdateField(msg);
        }
        if (msg.iKeyPart1 == 2) {
            Field2.UpdateField(msg);
        }
        if (msg.iMatchNumber != LastMatch) {
            if (clock.UpdateSchedule(msg.iMatchNumber)) {
                LastMatch = msg.iMatchNumber;
            }
        }

        Field1.repaint();
        Field2.repaint();
    }
}
