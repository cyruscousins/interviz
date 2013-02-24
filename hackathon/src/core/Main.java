package core;

import java.io.File;
import javax.swing.JFileChooser;

import visualization.VisualizationManager;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.Track;

public class Main {
	
	private static Track retrieveTrack(File songName){
		try {
			EchoNestAPI enApi = new EchoNestAPI("QGBFJRQABCWTIDEG0");
            Track track = enApi.uploadTrack(songName);
            track.waitForAnalysis(30000);
            if (track.getStatus() == Track.AnalysisStatus.COMPLETE) {
                return track;
            } else {
            	System.err.println("Trouble analysing track " + track.getStatus());
            	return null;
            }
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){
		VisualizationFrame frame = new VisualizationFrame(800, 600, null);
		
		VisualizationManager vis = new VisualizationManager(frame);

		frame.init();
		final JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(frame);
		File songFile = fc.getSelectedFile();
		String otherFileName = songFile.getPath().substring(0, songFile.getPath().lastIndexOf('.'));
		File mp3file = null, wavfile = null;
		if(songFile.getName().endsWith("wav")) {
			mp3file = new File(otherFileName + ".mp3");
			wavfile = songFile;
		} else if(songFile.getName().endsWith("mp3")){
			mp3file = songFile;
			wavfile = new File(otherFileName + ".wav");
		} else {
			System.err.println("Invalid file extension. Only wav and mp3 allowed");
			System.exit(1);
		}
		Track track = null;
		track = retrieveTrack(mp3file);
		try{
			vis.setTrack(track);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		Mp3Player songPlayer = null;

		songPlayer = new Mp3Player(wavfile);

		songPlayer.playSound();
		long t0 = 0;
		long t1 = 0;
		while(songPlayer.isDonePlaying()) {
			t0 = songPlayer.getMicrosecondsTime();
			System.out.println(t0-t1);
			vis.update((t0 - t1)/1000000.0f);
			
			t1 = songPlayer.getMicrosecondsTime();
			
			vis.render();
		}
		System.out.println("I'm done");
	}
}
