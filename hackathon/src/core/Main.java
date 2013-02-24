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
		fc.setMultiSelectionEnabled(true);
		fc.showOpenDialog(frame);
		File[] songs = fc.getSelectedFiles();
		Track track = null;
		if(songs.length > 0){
			track = retrieveTrack(songs[0]);
			try{
				vis.setTrack(track);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		Mp3Player songPlayer = null;
		if(songs[0].getName().endsWith("wav")) {
			System.out.println("Going to play the song");
			songPlayer = new Mp3Player(songs[0]);
		} else {
			System.err.println("I can't play this file!");
			System.exit(1);
		}

		songPlayer.playSound();
		long t0 = 0;
		long t1 = 0;
		while(songPlayer.isDonePlaying()) {
			t0 = songPlayer.getMillisecondsTime();
			System.out.println(t0-t1);
			vis.update((t0 - t1)/1000.0f);
			
			t1 = songPlayer.getMillisecondsTime();
			
			vis.render();
		}
		System.out.println("I'm done");
	}
}
