package core;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Segment;
import com.echonest.api.v4.TimedEvent;
import com.echonest.api.v4.Track;
import com.echonest.api.v4.TrackAnalysis;

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
	float emitterTheta, emitterR;
	
	int emitCount;
	
	Random rand = new Random();
	public void update(float dt){
		this.time += dt;
		

		float tempoNow;
		int sourceX, sourceY;
		
		int colR, colG, colB;
		
		int newSegCount;
		
		if(segments == null){
			
			tempoNow = 120;
		
			emitterTheta += (float)(dt * tempoNow / 60 * (2 * Math.PI) * .125f);
			
			emitterR = 64;
			
			float colSpeed = .5f;
			colR = (int)(100 + 50 * Math.sin(time * colSpeed));
			colG = (int)(100 + 50 * Math.cos(time * colSpeed));
			colB = (int)(100 - 50 * Math.sin(time * colSpeed));

			newSegCount = 4;
		}
		else{
			Segment seg = segments.get(currentSegmentIndex);
			
			while(time - segT0 > seg.getDuration()){
				segT0 += seg.getDuration();
				currentSegmentIndex++;
				if(currentSegmentIndex >= segments.size()){
					System.out.println("SONG FINISHED!");
				}
				else seg = segments.get(currentSegmentIndex);
			}
			
			float relativeLoudness = (float)(seg.getLoudnessMax() / tLoudness);
			
//			if(rand.nextFloat() > .75f) System.out.println("RLOUDNESS: " + relativeLoudness);
			
			tempoNow = 120; //TODO
			
			emitterTheta += dt * tempoNow / 60;
			emitterR = (float)(32 * relativeLoudness);

			double[] timbres = seg.getTimbre(); //12 values, centered around 0
			
			colR = (int)((timbres[0] + 1) * 127);
			colG = (int)((timbres[1] + 1) * 127);
			colB = (int)((timbres[2] + 1) * 127);
			
			//How many particles does the emitter emit?
			float countTemp = relativeLoudness * relativeLoudness * dt * 100;
			newSegCount = (int) countTemp + (rand.nextFloat() < (countTemp - (int) countTemp) ? 1 : 0);

			if(rand.nextFloat() > .75f) System.out.println("NEWSEGS: " + newSegCount);
			
			double[] pitches = seg.getPitches();
			
			for(int i = 0; i < pitches.length; i++){
				if(pitches[i] * pitches[i] > rand.nextDouble() + .25f){
					
					float x = 32;
					float y = 50 * (2 + i);
					
					y *= rand.nextInt(2) + 1;
					
					float dx = (float)(pitches[i]) * 400;
					float dy = -rand.nextFloat() * 10f;
					
					float d2x = rand.nextFloat();
					float d2y = 20; //GRAV

					Polynomial radius = new Polynomial(new float[]{2, .3f, -.225f, .1f, -.075f});
					
					float MASS = 3;
					
					Particle p = new Particle(x, y, dx, dy, d2x, d2y, MASS, radius, colR, colG, colB);
					particles.add(p);
				}
			}
		}
		
		sourceX = (int)(frame.width / 2 + (Math.cos(emitterTheta) * emitterR));
		sourceY = (int)(frame.height / 2 + (Math.sin(emitterTheta) * emitterR));
		
		for(int i = 0; i < 2; i++){
			float vel = (float)(50 * Math.random());
			float theta = (float)(Math.random() * Math.PI * 2);
			
			float grav = 100f;
//			float gravDir = (float)(Math.random() * Math.PI * 2);

			float gravDir = (float)(Math.PI * 1 / 2);
			
			float dx = (float)(vel * Math.cos(theta));
			float dy = (float)(vel * Math.sin(theta));
			
			float d2x = (float)(grav * Math.cos(gravDir));
			float d2y = (float)(grav * Math.sin(gravDir));
			
//			Polynomial dx = new Polynomial(new float[]{(float)(vel * Math.cos(theta)), (float)(grav * Math.cos(gravDir)), -.05f, .025f, -.0125f});
//			Polynomial dy = new Polynomial(new float[]{(float)(vel * Math.sin(theta)), (float)(grav * Math.sin(gravDir)), -.05f, .025f, -.0125f});
			
			Polynomial radius = new Polynomial(new float[]{1.5f, .3f, -.225f});

			Particle p = new Particle(sourceX, sourceY, dx, dy, d2x, d2y, 1, radius, colR, colG, colB);
			p.update(.2f);
			
			particles.add(p);
//			particles.add(new Particle(rand.nextInt(frame.width), rand.nextInt(frame.height), dx, dy, 1, radius));
		}
		
		for(Particle p : particles){
			
//			p.dx += noise.dydx(p.x, p.y) * dt;
//			p.dy += (noise.dydz(p.x, p.y)) * dt;
			
			p.dy -= ((p.y / frame.height) * (p.y / frame.height) * 128) * dt;
			
			p.update(dt);
			if(p.rad < 0) trash.add(p);
		}
		
		for(Particle p : trash){
			particles.remove(p);
		}
	}
	
	Track track;
	TrackAnalysis analysis;
	
	List<TimedEvent> tatums;
	List<Segment> segments;
	int currentSegmentIndex;
	float segT0;
	
	float tLoudness;
	
	public void setTrack(Track track) throws EchoNestException{
		this.track = track;
		
		tLoudness = (float)track.getLoudness();
		
		analysis = track.getAnalysis();
		
		tatums = analysis.getTatums();
		segments = analysis.getSegments();
		currentSegmentIndex = 0;
		segT0 = time;
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
