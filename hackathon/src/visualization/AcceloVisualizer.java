package visualization;

import math.Polynomial;

public class AcceloVisualizer extends Visualization{
	public AcceloVisualizer(VisualizationManager vis){
		super(vis);
	}

	public void update(float dt, float onFrac) {
		
		float rawEmitAmt = parent.relativeLoudness * parent.relativeLoudness * onFrac * dt * 40;
		
		int emitAmt = (int)(rawEmitAmt) + (parent.rand.nextFloat() < (rawEmitAmt - (int) rawEmitAmt) ? 1 : 0);
		
		if(parent.relativeLoudness < 0) emitAmt = 0;
		
		for(int i = 0; i < emitAmt; i++){
			
			int sourceX = (int)(parent.rand.nextFloat() * parent.frame.width);
			int sourceY = (int)(parent.rand.nextFloat() * parent.frame.height);
			
			float vel = (float)(25 * parent.rand.nextFloat());
			float theta = (float)(Math.random() * Math.PI * 2);

			float dx = (float)(vel * Math.cos(theta));
			float dy = (float)(vel * Math.sin(theta));
			
			float d2x = -dx * .5f;
			float d2y = -dy * .5f;
			
//			Polynomial dx = new Polynomial(new float[]{(float)(vel * Math.cos(theta)), (float)(grav * Math.cos(gravDir)), -.05f, .025f, -.0125f});
//			Polynomial dy = new Polynomial(new float[]{(float)(vel * Math.sin(theta)), (float)(grav * Math.sin(gravDir)), -.05f, .025f, -.0125f});
			
			Polynomial radius = new Polynomial(new float[]{1.5f, .3f + parent.rand.nextFloat() * .25f, -.225f});
			
			Particle p = parent.genParticle(sourceX, sourceY, dx, dy, d2x, d2y, 1, radius);
			p.update(.2f);
			
			particles.add(p);
//			particles.add(new Particle(rand.nextInt(frame.width), rand.nextInt(frame.height), dx, dy, 1, radius));
		}
		
		//Update particles
		
		updateParticles(dt);
	}
}
