package core;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

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

		
		for(int i = 0; i < 100000; i++){
			vis.update();
			
			vis.render();
		}
		
	}
}
