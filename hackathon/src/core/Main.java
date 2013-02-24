package core;

import java.io.File;
import javax.swing.JFileChooser;
import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.Track;

public class Main {
	
	private static Track retrieveTrack(File songName){
		EchoNestAPI enApi = null;
		try {
			enApi = new EchoNestAPI("QGBFJRQABCWTIDEG0");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
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
		
		Visualization vis = new Visualization(frame);

		frame.init();
		final JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		fc.showOpenDialog(frame);
		File[] songs = fc.getSelectedFiles();
		Mp3Player.playSound(songs[0]);
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
		for(int i = 0; i < 100000; i++){
			float time = .015f;
			vis.update(time);
			try{
				Thread.sleep((int)(1000 * time));
			}
			catch(Exception e){
				e.printStackTrace();
			}
			vis.render();
		}
	}
}
