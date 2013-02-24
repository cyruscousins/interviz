package core;

import java.io.File;

import javax.swing.JFileChooser;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import com.echonest.api.v3.track.TrackAPI;

public class Main {

	public static void main(String[] args){
		
		VisualizationFrame frame = new VisualizationFrame(800, 600, null);
		
		Visualization vis = new Visualization(frame);

		frame.init();
		
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(frame);
		File song = fc.getSelectedFile();

		if(song == null){
			System.exit(1);
		}
		
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
