package core;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import java.io.File;

public class Mp3Player {
	public static synchronized void playSound(final File soundFile) {
		new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
			public void run() {
				try {
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile);
					AudioFormat format = inputStream.getFormat();
					DataLine.Info info = new DataLine.Info(Clip.class, format);
					Clip clip = (Clip)AudioSystem.getLine(info);
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