package visualization;

import math.Polynomial;

public class CenterVisualizationPitchClass extends Visualization{
	
	public CenterVisualizationPitchClass(VisualizationManager parent){
		super(parent);
	}
	
	float rot;
	public void update(float dt, float onFrac){
		rot += dt * parent.tempoNow / 60 * (float)Math.PI * 2 / 4;
		
		int sourceX = (int)(parent.frame.width  / 2);
		int sourceY = (int)(parent.frame.height / 2);
		
		float rawEmitAmt = parent.relativeLoudness * parent.relativeLoudness * onFrac * dt * 90;
		
		if(parent.relativeLoudness > 0){
			double[] pitches = parent.segments.get(parent.currentSegmentIndex).getPitches();
			
			for(int i = 0; i < pitches.length; i++){

				float pitchEmitAmt = rawEmitAmt * (float)pitches[i];
				int emitAmt = (int)(pitchEmitAmt) + (parent.rand.nextFloat() < (pitchEmitAmt - (int) pitchEmitAmt) ? 1 : 0);
				
				if(emitAmt > 0){
					float theta = (float)(rot + (float)Math.PI * 2 * i / 12);

					float sinTheta = (float) Math.sin(theta);
					float cosTheta = (float) Math.cos(theta);
					
					int colR = parent.colR + (int)( sinTheta * 30);
					int colG = parent.colG + (int)( cosTheta * 30);
					int colB = parent.colB + (int)(-sinTheta * 30);
					
					for(int j = 0; j < emitAmt; j++){
						float vel = (float)(150 * parent.rand.nextFloat() + 12.5f) * parent.relativeLoudness;
						
						float grav = 35f;
						float gravDir = (float)(Math.PI * 1 / 2);
						
						float dx = (float)(vel * cosTheta);
						float dy = (float)(vel * sinTheta);
						
						float d2x = (float)(grav * Math.cos(gravDir)) - dx * .75f;
						float d2y = (float)(grav * Math.sin(gravDir)) - dy * .75f;
						
						Polynomial radius = new Polynomial(new float[]{2.5f, 1f + parent.rand.nextFloat() * .5f, -.3f - parent.rand.nextFloat() * .3f});
						
						Particle p = new Particle(sourceX, sourceY, dx, dy, d2x, d2y, 1, radius, colR, colG, colB);
						p.update(.2f);
						
						particles.add(p);
					}
				}
			}
		}
		
		//Update particles
		
		updateParticles(dt);
	}
}
