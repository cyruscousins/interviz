package core;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import java.io.File;

public class Mp3Player {
	private Clip clip;
	boolean hasStartedRunning;
	
	public Mp3Player(File soundFile) {
		try {
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile);
			AudioFormat format = inputStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			clip = (Clip)AudioSystem.getLine(info);
	        clip.open(inputStream);
	        hasStartedRunning = false;
		} catch (Exception e) {
			System.err.println(e.toString());
			e.printStackTrace();
		}
	}
	
	public void playSound() {
		clip.start();
	}
	
	public long getMicrosecondsTime() {
		return clip.getMicrosecondPosition();
	}
	
	public boolean isDonePlaying() {
		boolean isRunning = clip.isRunning() || clip.isActive();
		if(!hasStartedRunning && isRunning) {
			hasStartedRunning = true;
		}
		return hasStartedRunning && !isRunning;
	}
}