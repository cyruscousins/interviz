package visualization;

import math.Polynomial;

public class BeatPulser extends Visualization{

	public BeatPulser(VisualizationManager vis){
		super(vis);
	}
	
	float life, maxLife;
	
	float x0, y0;
	public void update(float dt, float onFrac) {

		if(parent.newBeat){
			life = maxLife = 60f / parent.tempoNow * .125f;

			x0 = parent.rand.nextFloat() * parent.frame.width;
			y0 = parent.rand.nextFloat() * parent.frame.height;
		}
		
		float rawEmitAmt = parent.relativeLoudness * parent.beatConfidence * onFrac * dt * 128 * life / maxLife;
		
		int emitAmt = (int)(rawEmitAmt) + (parent.rand.nextFloat() < (rawEmitAmt - (int) rawEmitAmt) ? 1 : 0);
		
		
		for(int i = 0; i < emitAmt; i++){

			float vel = 180 * parent.rand.nextFloat() * life / maxLife;
			float theta = (float)(Math.random() * Math.PI * 2);

			float dx = (float)(vel * Math.cos(theta));
			float dy = (float)(vel * Math.sin(theta));
			
			float d2x = 0;
			float d2y = 60;
			
//			float d2x = -dx * .5f;
//			float d2y = -dy * .5f;
			
//			Polynomial dx = new Polynomial(new float[]{(float)(vel * Math.cos(theta)), (float)(grav * Math.cos(gravDir)), -.05f, .025f, -.0125f});
//			Polynomial dy = new Polynomial(new float[]{(float)(vel * Math.sin(theta)), (float)(grav * Math.sin(gravDir)), -.05f, .025f, -.0125f});
			
			Polynomial radius = new Polynomial(new float[]{2f + life * life / (maxLife * maxLife) * 2, 8f + parent.rand.nextFloat() * 4f, -50f});
			
			Particle p = parent.genParticle(x0, y0, dx, dy, d2x, d2y, 1, radius);
			p.update(.05f);
			
			particles.add(p);
//			particles.add(new Particle(rand.nextInt(frame.width), rand.nextInt(frame.height), dx, dy, 1, radius));
		}
		
		//Update particles
		
		updateParticles(dt);
	}
	
}
