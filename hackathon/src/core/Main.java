package core;

import java.awt.GridLayout;
import java.awt.TextArea;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.Track;
import com.echonest.api.v4.TrackAnalysis;


public class Main {
	
	private static TrackAnalysis retrieveAnalysis(File songName){
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
                return track.getAnalysis();
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
		TrackAnalysis analysis = null;
		if(songs.length > 0){
			analysis = retrieveAnalysis(songs[0]);
		}
		for(int i = 0; i < 100000; i++){
			float time = .01f;
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
