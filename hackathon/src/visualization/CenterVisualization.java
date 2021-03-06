package visualization;

import math.Polynomial;

public class CenterVisualization extends Visualization{
	
	public CenterVisualization(VisualizationManager parent){
		super(parent);
	}
	
	public float emitterTheta, emitterR;
	
	public void update(float dt, float onFrac){
		
		int sourceX = (int)(parent.frame.width  / 2 + (Math.cos(emitterTheta) * emitterR));
		int sourceY = (int)(parent.frame.height / 2 + (Math.sin(emitterTheta) * emitterR));
		
		float rawEmitAmt = parent.relativeLoudness * parent.relativeLoudness * onFrac * dt * 60;
		
		int emitAmt = (int)(rawEmitAmt) + (parent.rand.nextFloat() < (rawEmitAmt - (int) rawEmitAmt) ? 1 : 0);
		
		if(parent.relativeLoudness < 0) emitAmt = 0;
		
		for(int i = 0; i < emitAmt; i++){
			float vel = (float)(25 * parent.rand.nextFloat()) * parent.relativeLoudness;
			float theta = (float)(Math.random() * Math.PI * 2);
			
			float grav = 50f;
//			float gravDir = (float)(Math.random() * Math.PI * 2);

			float gravDir = (float)(Math.PI * 1 / 2);
			
			float dx = (float)(vel * Math.cos(theta));
			float dy = (float)(vel * Math.sin(theta));
			
			float d2x = (float)(grav * Math.cos(gravDir));
			float d2y = (float)(grav * Math.sin(gravDir));
			
//			Polynomial dx = new Polynomial(new float[]{(float)(vel * Math.cos(theta)), (float)(grav * Math.cos(gravDir)), -.05f, .025f, -.0125f});
//			Polynomial dy = new Polynomial(new float[]{(float)(vel * Math.sin(theta)), (float)(grav * Math.sin(gravDir)), -.05f, .025f, -.0125f});
			
			Polynomial radius = new Polynomial(new float[]{1.5f, .3f, -.225f});
			
			Particle p = parent.genParticle(sourceX, sourceY, dx, dy, d2x, d2y, 1, radius);
			p.update(.2f);
			
			particles.add(p);
//			particles.add(new Particle(rand.nextInt(frame.width), rand.nextInt(frame.height), dx, dy, 1, radius));
		}
		
		//Update particles
		
		updateParticles(dt);
	}
}
