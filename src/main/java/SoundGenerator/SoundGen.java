/**
 * 
 */
package SoundGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MgrMain.Main;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 *         https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class SoundGen {

    final public static Logger  logger  = LoggerFactory.getLogger(Main.class);

    protected static final String sPath   = "/Sound/";
    protected static final String sFormat = "wav";
    
    public static synchronized void playSound(final String inFile) {
        final Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String fPath = System.getProperty("user.dir") + sPath + inFile + "." + sFormat;
                    final Clip clip = AudioSystem.getClip();
                    logger.info("Playing Sound {}", fPath);
                    final File file = new File(fPath);
                    final AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
                    clip.open(inputStream);
                    clip.start();
                } catch (final LineUnavailableException e) {
                    logger.error("Audio Line Unavailable: {}", e.getMessage());
                } catch (final FileNotFoundException e) {
                    logger.error("File not found: {}", e.getMessage());
                } catch (final UnsupportedAudioFileException e) {
                    logger.error("Audio File Unsupported: {}", e.getMessage());
                } catch (final IOException e) {
                    logger.error("IO Error: {}", e.getMessage());
                } catch (final IllegalArgumentException e) {
                    logger.error("Illegal Arguement: {}", e.getMessage());
                }
            }
        });
        th.setName("SoundGen");
        th.start();
    }
}
