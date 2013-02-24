package core;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import math.F2Var;
import math.Polynomial;

public class Visualization {
	VisualizationFrame frame;
	
	ArrayList<Particle> particles = new ArrayList<Particle>();
	ArrayList<Particle> trash = new ArrayList<Particle>();
	
	
	F2Var noise;
	
	public Visualization(VisualizationFrame frame){
		this.frame = frame;
		noise = math.Noise3.noiseSVF(128, 512, 0, 4);
	}
	
	float time;
	float emitterTheta;
	
	Random rand = new Random();
	public void update(float dt){
		this.time += dt;
		
		float tempoNow = 120;
		
		emitterTheta += (float)(dt * tempoNow / 60 * (2 * Math.PI) * .125f);
		
		float rad = 64;
		
		int sourceX = (int)(frame.width / 2 + (Math.cos(emitterTheta) * rad));
		int sourceY = (int)(frame.height / 2 + (Math.sin(emitterTheta) * rad));
		
		float colSpeed = .5f;
		int colR = (int)(100 + 50 * Math.sin(time * colSpeed));
		int colG = (int)(100 + 50 * Math.cos(time * colSpeed));
		int colB = (int)(100 - 50 * Math.sin(time * colSpeed));
		
		for(int i = 0; i < 4; i++){
			float vel = (float)(50 * Math.random());
			float theta = (float)(Math.random() * Math.PI * 2);
			
			float grav = 80f;
//			float gravDir = (float)(Math.random() * Math.PI * 2);

			float gravDir = (float)(Math.PI * 1 / 2);
			
			float dx = (float)(vel * Math.cos(theta));
			float dy = (float)(vel * Math.sin(theta));
			
			float d2x = (float)(grav * Math.cos(gravDir));
			float d2y = (float)(grav * Math.sin(gravDir));
			
//			Polynomial dx = new Polynomial(new float[]{(float)(vel * Math.cos(theta)), (float)(grav * Math.cos(gravDir)), -.05f, .025f, -.0125f});
//			Polynomial dy = new Polynomial(new float[]{(float)(vel * Math.sin(theta)), (float)(grav * Math.sin(gravDir)), -.05f, .025f, -.0125f});
			
			Polynomial radius = new Polynomial(new float[]{2, .3f, -.2f});

			Particle p = new Particle(sourceX, sourceY, dx, dy, d2x, d2y, 1, radius, colR, colG, colB);
			p.update(.2f);
			
			particles.add(p);
//			particles.add(new Particle(rand.nextInt(frame.width), rand.nextInt(frame.height), dx, dy, 1, radius));
		}
		
		for(Particle p : particles){
			
			p.dx += noise.dydx(p.x, p.y) * dt;
			p.dy += (noise.dydz(p.x, p.y) - (p.y / frame.height) * (p.y / frame.height) * 128) * dt;
			
			p.update(dt);
			if(p.rad < 0) trash.add(p);
		}
		
		for(Particle p : trash){
			particles.remove(p);
		}
	}
	
	public void render(){
		Graphics g = frame.bufferG;
		g.setColor(new Color(0, 0, 0, 12));
		g.fillRect(0, 0, frame.width, frame.height);
		
		for(Particle particle : particles){
			particle.render(g);
		}
		frame.drawToScreen();
	}
}
