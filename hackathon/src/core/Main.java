package core;

public class Main {
	public static void main(String[] args){
		
		VisualizationFrame frame = new VisualizationFrame(800, 600, null);
		
		Visualization vis = new Visualization(frame);

		frame.init();
		
		for(int i = 0; i < 100000; i++){
			vis.update();
			
			vis.render();
		}
		
	}
}
