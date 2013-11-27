/**
 * 
 */
package Main;

import java.applet.AudioClip;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.audio.AudioPlayer;

/**
 * @author Matthew Glennon (mglennon@virginiafirst.org)
 * https://github.com/VirginiaFIRST/FTC-FieldMgmt
 */
public class SoundGen {

    final public static Logger              logger      = LoggerFactory.getLogger(Main.class);
    
    private static final String sPath = "/Sound/";
    private static final String sFormat = "wav";
    
    public static synchronized void playSound(final String inFile) {
        Thread th = new Thread(new Runnable() {
            public void run() {
                try {
                    final String fPath = System.getProperty("user.dir") + sPath + inFile + "." + sFormat;
                    Clip clip = AudioSystem.getClip();
                    logger.info("Playing Sound {}",fPath);
                    File file = new File(fPath);
                    final AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
                    clip.open(inputStream);
                    clip.start();
                } catch(LineUnavailableException e) {
                    logger.error("Audio Line Unavailable: {}", e.getMessage());
                } catch (FileNotFoundException e) {
                    logger.error("File not found: {}", e.getMessage());
                } catch (UnsupportedAudioFileException e){
                    logger.error("Audio File Unsupported: {}", e.getMessage());
                } catch (IOException e) {
                    logger.error("IO Error: {}", e.getMessage());
                } catch (IllegalArgumentException e) {
                    logger.error("Illegal Arguement: {}", e.getMessage());
                }
            }
        });
        th.setName("SoundGen");
        th.start();
    }
}
