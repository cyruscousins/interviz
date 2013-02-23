package core;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;

import math.Polynomial;

public class Visualization {
	VisualizationFrame frame;
	
	LinkedList<Particle> particles = new LinkedList<Particle>();
	
	public Visualization(VisualizationFrame frame){
		this.frame = frame;
	}
	
	Random rand = new Random();
	public void update(){
		for(int i = 0; i < 1; i++){
			float vel = 2;
			particles.add(new Particle(rand.nextInt(frame.width), rand.nextInt(frame.height), (rand.nextFloat() - .5f) * vel, (rand.nextFloat() - .5f) * vel, 1, new Polynomial(new float[]{2, .25f, -.1f})));
		}
		
		for(Particle p : particles){
			p.update(.2f);
		}
	}
	
	public void render(){
		Graphics g = frame.bufferG;
		g.setColor(new Color(0, 0, 0, 10));
		g.fillRect(0, 0, frame.width, frame.height);
		
		for(Particle particle : particles){
			particle.render(g);
		}
		frame.drawToScreen();
	}
}
