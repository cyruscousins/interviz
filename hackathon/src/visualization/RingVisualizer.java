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
		
		radius = (float)(Math.pow(.75f, dt) * radius + 100 * (1 + parent.relativeLoudness));
		
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
			
			float dx = (float)(vel * (sinTheta + cosTheta));
			float dy = (float)(vel * (cosTheta - sinTheta));
			
			float d2x = -x * .5f - grav;
			float d2y = -y * .5f;
			
//			Polynomial dx = new Polynomial(new float[]{(float)(vel * Math.cos(theta)), (float)(grav * Math.cos(gravDir)), -.05f, .025f, -.0125f});
//			Polynomial dy = new Polynomial(new float[]{(float)(vel * Math.sin(theta)), (float)(grav * Math.sin(gravDir)), -.05f, .025f, -.0125f});
			
			Polynomial radius = new Polynomial(new float[]{1.5f, .35f + parent.rand.nextFloat() * .5f, -.225f});
			
			Particle p = new Particle(x, y, dx, dy, d2x, d2y, 1, radius, parent.colR, parent.colG, parent.colB);
			p.update(.2f);
			
			particles.add(p);
//			particles.add(new Particle(rand.nextInt(frame.width), rand.nextInt(frame.height), dx, dy, 1, radius));
		}
		
		//Update particles
		
		updateParticles(dt);
	}
}
