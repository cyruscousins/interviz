package core;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import math.Polynomial;

public class Visualization {
	VisualizationFrame frame;
	
	ArrayList<Particle> particles = new ArrayList<Particle>();
	ArrayList<Particle> trash = new ArrayList<Particle>();
	
	public Visualization(VisualizationFrame frame){
		this.frame = frame;
	}
	
	float time;
	float emitterTheta;
	
	Random rand = new Random();
	public void update(float t){
		this.time += t;
		
		float tempoNow = 120;
		
		emitterTheta += (float)(tempoNow / 60 * (2 * Math.PI));
		
		float rad = 8;
		
		int sourceX = (int)(frame.width / 2 + (Math.cos(emitterTheta) * rad));
		int sourceY = (int)(frame.height / 2 + (Math.sin(emitterTheta) * rad));
		
		for(int i = 0; i < 1; i++){
			float vel = (float)(3 * Math.random());
			float theta = (float)(Math.random() * Math.PI * 2);
			
			float grav = 2;
//			float gravDir = (float)(Math.random() * Math.PI * 2);

			float gravDir = (float)(Math.PI * 1 / 2);
			
			Polynomial dx = new Polynomial(new float[]{(float)(vel * Math.cos(theta)), (float)(grav * Math.cos(gravDir))});
			Polynomial dy = new Polynomial(new float[]{(float)(vel * Math.sin(theta)), (float)(grav * Math.sin(gravDir))});
			
//			Polynomial dx = new Polynomial(new float[]{(float)(vel * Math.cos(theta)), (float)(grav * Math.cos(gravDir)), -.05f, .025f, -.0125f});
//			Polynomial dy = new Polynomial(new float[]{(float)(vel * Math.sin(theta)), (float)(grav * Math.sin(gravDir)), -.05f, .025f, -.0125f});
			
			Polynomial radius = new Polynomial(new float[]{2, .3f, -.2f});

			Particle p = new Particle(sourceX, sourceY, dx, dy, 1, radius);
			p.update(.2f);
			
			particles.add(p);
//			particles.add(new Particle(rand.nextInt(frame.width), rand.nextInt(frame.height), dx, dy, 1, radius));
		}
		
		for(Particle p : particles){
			p.update(.02f);
			if(p.rad < 0) trash.add(p);
		}
		for(Particle p : trash){
			particles.remove(p);
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
