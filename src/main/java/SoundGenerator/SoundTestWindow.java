/**
 * 
 */
package SoundGenerator;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MgrMain.Main;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class SoundTestWindow extends JPanel {

    /**
     * 
     */
    private static final long  serialVersionUID = 1L;

    final public static Logger logger           = LoggerFactory.getLogger(Main.class);

    final private SpinnerModel Model            = new SpinnerNumberModel(1, 1, 10, 1);

    final private JSpinner     loopCount        = new JSpinner(Model);

    public SoundTestWindow() {
        // this.setSize(1000, 500);
        this.setLayout(new GridLayout(0, 2, 0, 0));

        String files;
        final File folder = new File(System.getProperty("user.dir") + SoundGen.sPath);
        final File[] listOfFiles = folder.listFiles();

        for (final File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                files = listOfFile.getName();
                if (files.endsWith("." + SoundGen.sFormat)) {
                    final String filename = files.replace("." + SoundGen.sFormat, "");
                    logger.info("Buiding Test Btn: {}", filename);
                    final JButton j = new JButton(filename);
                    j.addActionListener(new TestBtnPress());
                    this.add(j);
                }
            }
        }
        this.add(loopCount);

        final JButton stop = new JButton("STOP");
        stop.addActionListener(new StopBtnPress());
        stop.setEnabled(false);
        this.add(stop);
        this.invalidate();
    }

    private class StopBtnPress implements ActionListener {
        @Override
        // ONEDAY Actually Stop Sound Gen work.
        public void actionPerformed(final ActionEvent e) {
            logger.info("Stopping All Sound");
            SoundGen.StopAll();
        }

    }

    private class TestBtnPress implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            final JButton j = (JButton) e.getSource();
            logger.info("Test Playing: {}", j.getText());
            SoundGen.playSound(j.getText(), (int) loopCount.getValue());
        }

    }
}
