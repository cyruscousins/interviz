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
				float y = (40 * (1 + i) + time * 12) % parent.frame.height;
				
				float dx = (float)(pitches[i]) * 50;
				float dy = -rand.nextFloat() * 5f;
				
				float d2x = rand.nextFloat();
				float d2y = 20; //GRAV

				Polynomial radius = new Polynomial(new float[]{(float)pitches[i] + 2, .3f + (float)pitches[i] * .5f, -.35f * rand.nextFloat() * .25f, .1f, -.075f});
				
				float MASS = 3;
				
				Particle p = new Particle(x, y, dx, dy, d2x, d2y, MASS, radius, parent.colR, parent.colG, parent.colB);
				particles.add(p);
				
				if(bothSides){
					p = new Particle(parent.frame.width - x, y, -dx, dy, -d2x, d2y, MASS, radius, parent.colR, parent.colG, parent.colB);
					particles.add(p);
				}
			}
		}

		updateParticles(dt);
	}
}
