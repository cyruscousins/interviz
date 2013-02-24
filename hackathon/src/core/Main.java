package core;

import java.io.File;

import javax.swing.JFileChooser;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import com.echonest.api.v3.EchoNestException;
import com.echonest.api.v3.track.FloatWithConfidence;
import com.echonest.api.v3.track.Metadata;
import com.echonest.api.v3.track.TrackAPI;
import com.echonest.api.v3.track.TrackAPI.AnalysisStatus;

public class Main {

	public static void main(String[] args){
		
		VisualizationFrame frame = new VisualizationFrame(800, 600, null);
		
		Visualization vis = new Visualization(frame);

		frame.init();
		
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(frame);
		File song = fc.getSelectedFile();

		if(song == null){
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
		} else {
			
			AudioFile songFile = null;
			
			try {
				songFile = AudioFileIO.read(song);
			} catch (Exception e){
				e.printStackTrace();
			}
			
			Tag tag = songFile.getTag();
			System.out.println(tag.getFirst(FieldKey.ARTIST) + 
			tag.getFirst(FieldKey.ALBUM) + 
			tag.getFirst(FieldKey.TITLE));
			
			TrackAPI trackAPI = null;
			
			try {
				trackAPI = new TrackAPI("QGBFJRQABCWTIDEG0");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				String id = trackAPI.uploadTrack(song, false);
				AnalysisStatus status = trackAPI.waitForAnalysis(id, 60000);
	            if (status == AnalysisStatus.COMPLETE) {
	                Metadata metadata = trackAPI.getMetadata(id);
	                FloatWithConfidence bpm = trackAPI.getTempo(id);
	                System.out.println("Metadata:");
	                System.out.println(metadata);
	                System.out.println("BPM is " + bpm);
	            } else {
	                System.out.println("Status is " + status);
	            }
			} catch (EchoNestException e1) {
				e1.printStackTrace();
			}
			
			
		}
		
	}
}
