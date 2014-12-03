/**
 * 
 */
package MgrMain;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FTCMgrShared.ClientPack;
import SoundGenerator.SoundTestWindow;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    final Logger logger = LoggerFactory.getLogger(Main.class);

    private final Field Field1 = new Field(1);
    private final Field Field2 = new Field(2);

    private final JPanel UtilPanel = new JPanel();
    private final IPFetch.InfoPanel info = new IPFetch.InfoPanel();

    private final TimingPanel clock = new TimingPanel();

    private int LastMatch = 0;

    public MainWindow(String WindowTitle) {

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                MainWindow.this.logger.info("Main Window is closing.");
                Main.Quit();
            }
        });
        this.setTitle(WindowTitle);
        this.setSize(1000, 500);
        this.setLayout(new GridLayout(0, 2, 0, 0));

        this.getContentPane().add(this.Field1);
        this.getContentPane().add(this.Field2);

        // Build Utilities Frame
        final SoundTestWindow Testframe = new SoundTestWindow();
        this.UtilPanel.setLayout(new GridLayout(0, 1, 0, 0));
        this.UtilPanel.add(Testframe);
        this.UtilPanel.add(this.info);

        this.getContentPane().add(this.UtilPanel);

        this.getContentPane().add(this.clock);

        this.invalidate();
    }

    public void UpdateField(final FCSMsg msg) {
        final ClientPack pack = new ClientPack();
        if (msg.iMatchState == FCSMsg.MATCH_STATE_TELEOP_WAITING) {
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

        if (msg.iKeyPart1 == 1) {
            this.Field1.UpdateField(msg);
        }
        if (msg.iKeyPart1 == 2) {
            this.Field2.UpdateField(msg);
        }
        if (msg.iMatchNumber != this.LastMatch) {
            if (this.clock.UpdateSchedule(msg.iMatchNumber)) {
                this.LastMatch = msg.iMatchNumber;
            }
        }

        this.Field1.repaint();
        this.Field2.repaint();
    }

    public void UpdateNetInfo(final List<IPFetch.NetworkAdapter> Adptrs) {
        this.info.UpdateList(Adptrs);
    }
}
