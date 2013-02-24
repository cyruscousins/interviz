package visualization;


import java.util.Random;

import com.echonest.api.v4.Segment;

import math.Polynomial;

public class PitchClassEmitter extends Visualization{
	boolean bothSides = true;
	
	
	Random rand;
	public PitchClassEmitter(VisualizationManager parent){
		super(parent);
		rand = parent.rand;
	}
	
	float time;
	public void update(float dt, float amt){
		time += dt;
		
		Segment seg = parent.segments.get(parent.currentSegmentIndex);
		double[] pitches = seg.getPitches();
		
		for(int i = 0; i < pitches.length; i++){
			if(pitches[i] * pitches[i] > rand.nextDouble() + .25f){
				
				float x = 32;
				float y = (40 * (1 + i) + time * 50) % parent.frame.height;
				
				float theta = (float)(rand.nextFloat() * Math.PI * 2);
				float r = rand.nextFloat() * 32 + (parent.newBeat ?  16 : 0);
				
				
				float dx = (float)(pitches[i]) * 100;
				float dy = -rand.nextFloat() * 5f;
				
				float d2x = rand.nextFloat() * 25;
				float d2y = 20; //GRAV

				Polynomial radius = new Polynomial(new float[]{(float)pitches[i] + 2, .125f + (float)pitches[i] * .125f, -.25f - rand.nextFloat() * .125f});
				
				float MASS = 3;
				
				Particle p = new Particle(x, y, dx + (float)(Math.cos(theta) * r), dy + (float)(Math.sin(theta) * r), d2x, d2y, MASS, radius, parent.colR, parent.colG, parent.colB);
				particles.add(p);
				
				if(bothSides){
					theta = (float)(rand.nextFloat() * Math.PI * 2);
					r = rand.nextFloat() * 32 + (parent.newBeat ?  16 : 0);
					
					p = new Particle(x, y, dx + (float)(Math.cos(theta) * r), dy + (float)(Math.sin(theta) * r), d2x, d2y, MASS, radius, parent.colR, parent.colG, parent.colB);
					particles.add(p);
				}
			}
		}

		updateParticles(dt);
	}
}
