package core;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

public class Main {
	public static void main(String[] args){
		
		VisualizationFrame frame = new VisualizationFrame(800, 600, null);
		
		Visualization vis = new Visualization(frame);

		frame.init();
		
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(frame);
		File song = fc.getSelectedFile();
		
		try {
			MP3File mp3song = new MP3File(song);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		for(int i = 0; i < 100000; i++){
			vis.update();
			
			vis.render();
		}
		
	}
}
