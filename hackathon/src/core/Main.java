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
		if(songs[0].getName().endsWith("wav")) {
			System.out.println("Going to play the song");
			Mp3Player.playSound(songs[0]);
		}
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

		long t0 = System.currentTimeMillis();
		for(int i = 0; i < 100000; i++){
			float time = .02f;
			vis.update(time);
			
			long t1 = System.currentTimeMillis();
			
			if(t1 - t0 < time * 1000){
				try{
					Thread.sleep((int)(time * 1000 - (t1 - t0)));
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			
			t0 = System.currentTimeMillis();
			vis.render();
		}
	}
}
