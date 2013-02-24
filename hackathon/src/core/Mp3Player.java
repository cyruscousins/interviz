package core;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Mp3Player {
	public static synchronized void playSound(final File soundFile) {
		new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile);
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.toString());
					e.printStackTrace();
				}
			}
		}).start();
	}
}