package core;

import java.io.File;

import javax.swing.JFileChooser;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.TimedEvent;
import com.echonest.api.v4.Track;
import com.echonest.api.v4.Track.AnalysisStatus;
import com.echonest.api.v4.TrackAnalysis;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference.Metadata;


public class Main {

	public static void main(String[] args){
		
		VisualizationFrame frame = new VisualizationFrame(800, 600, null);
		
		Visualization vis = new Visualization(frame);

		frame.init();
		
		final JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(frame);
		File song = fc.getSelectedFile();

		if(song != null){
			EchoNestAPI enApi = null;
			try {
				enApi = new EchoNestAPI("QGBFJRQABCWTIDEG0");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
                Track track = enApi.uploadTrack(song);
                track.waitForAnalysis(30000);
	            if (track.getStatus() == Track.AnalysisStatus.COMPLETE) {
	            	System.out.println("Tempo: " + track.getTempo());
                    System.out.println("Danceability: " + track.getDanceability());
                    System.out.println("Speechiness: " + track.getSpeechiness());
                    System.out.println("Liveness: " + track.getLiveness());
                    System.out.println("Energy: " + track.getEnergy());
                    System.out.println("Loudness: " + track.getLoudness());
                    System.out.println();
                    System.out.println("Beat start times:");
                    TrackAnalysis analysis = track.getAnalysis();
                    for (TimedEvent beat : analysis.getBeats()) {
                        System.out.println("beat " + beat.getStart());
                    }
	            } else {
	            	System.err.println("Trouble analysing track " + track.getStatus());
	            }
			} catch (Exception e1) {
				e1.printStackTrace();
			}
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
