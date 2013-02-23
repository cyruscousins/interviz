package core;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;

public class Visualization {
	VisualizationFrame frame;
	
	LinkedList<Particle> particles = new LinkedList<Particle>();
	
	public Visualization(VisualizationFrame frame){
		this.frame = frame;
	}
	
	Random rand = new Random();
	public void update(){
		for(int i = 0; i < 10; i++){
			float vel = 2;
			particles.add(new Particle(rand.nextInt(frame.width), rand.nextInt(frame.height), (rand.nextFloat() - .5f) * vel, (rand.nextFloat() - .5f) * vel, 1, 1 + rand.nextInt(16), 8));
		}
		
		for(Particle p : particles){
			p.update();
		}
	}
	
	public void render(){
		Graphics g = frame.bufferG;
		for(Particle particle : particles){
			particle.render(g);
		}
		frame.drawToScreen();
	}
}
