package visualization;

import math.Polynomial;

public class RingVisualizer extends Visualization{
	
	public RingVisualizer(VisualizationManager parent){
		super(parent);
	}
	
	float radius = 80;
	
	public void update(float dt, float onFrac){
		
		float x0 = (parent.frame.width  / 2);
		float y0 = (parent.frame.height / 2);
		
		float rawEmitAmt = parent.relativeLoudness * parent.relativeLoudness * onFrac * dt * 120;
		
		int emitAmt = (int)(rawEmitAmt) + (parent.rand.nextFloat() < (rawEmitAmt - (int) rawEmitAmt) ? 1 : 0);
		
		if(parent.relativeLoudness < 0) emitAmt = 0;
		
		float scl = (float)Math.pow(.75f, dt);
		radius = (scl * radius + (1 - scl) * 100 * (1 + parent.relativeLoudness));
		
		if(parent.newBeat){
			radius += 50;
		}
		
		for(int i = 0; i < emitAmt; i++){
			float vel = (float)(25 * parent.rand.nextFloat()) * parent.relativeLoudness;
			float theta = (float)(Math.random() * Math.PI * 2);
			
			float cosTheta = (float)Math.cos(theta);
			float sinTheta = (float)Math.sin(theta);
			
			float grav = 50f;
//			float gravDir = (float)(Math.random() * Math.PI * 2);

			float gravDir = (float)(Math.PI * 1 / 2);

			float x = x0 + (float)(cosTheta * radius);
			float y = y0 + (float)(sinTheta * radius);
			
			float dx = (float)(vel * -sinTheta);
			float dy = (float)(vel * cosTheta);
			
			float d2x = -cosTheta * radius * .5f * .25f + grav;
			float d2y = -sinTheta * radius * .5f * .25f;
			
			Polynomial radius = new Polynomial(new float[]{1.5f, .35f + parent.rand.nextFloat() * .5f, -.225f});
			
			Particle p = parent.genParticle(x, y, dx, dy, d2x, d2y, 1, radius);
			p.update(.2f);
			
			particles.add(p);
		}
		
		//Update particles
		
		updateParticles(dt);
	}
}
