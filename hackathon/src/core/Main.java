package core;

import javax.swing.JFileChooser;

public class Main {
	public static void main(String[] args){
		
		VisualizationFrame frame = new VisualizationFrame(800, 600, null);
		
		Visualization vis = new Visualization(frame);

		frame.init();
		
		//Create a file chooser
		
		final JFileChooser fc = new JFileChooser();
		
		//In response to a button click:
		
		int returnVal = fc.showOpenDialog(frame);
		
		for(int i = 0; i < 100000; i++){
			vis.update();
			
			vis.render();
		}
		
	}
}
