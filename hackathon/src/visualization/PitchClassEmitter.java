package visualization;


import java.util.Random;

import com.echonest.api.v4.Segment;

import math.Polynomial;

public class PitchClassEmitter extends Visualization{
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
				float y = 40 * (1 + i) + time * 12;
				
//				y *= rand.nextInt(2) + 1;
				
				y = (int)y % parent.frame.height;
				
				
				float dx = (float)(pitches[i]) * 50;
				float dy = -rand.nextFloat() * 5f;
				
				float d2x = rand.nextFloat();
				float d2y = 20; //GRAV

				Polynomial radius = new Polynomial(new float[]{(float)pitches[i] + 2, .3f, -.225f, .1f, -.075f});
				
				float MASS = 3;
				
				Particle p = new Particle(x, y, dx, dy, d2x, d2y, MASS, radius, parent.colR, parent.colG, parent.colB);
				particles.add(p);
			}
		}
		
		int len = particles.size();
		for(int i = 0; i < len; i++){
			Particle p = particles.get(i);

//			p.dx += noise.dydx(p.x, p.y) * dt;
//			p.dy += (noise.dydz(p.x, p.y)) * dt;
			
			//Push away from bottom edge
			p.dy -= ((p.y / parent.frame.height) * (p.y / parent.frame.height) * 128) * dt;
			
			if(p.update(dt)){ //if dead
				particles.set(i--, particles.remove(--len));
			}
		}
	}
}
