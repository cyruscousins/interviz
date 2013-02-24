package visualization;

import sun.awt.geom.AreaOp.AddOp;
import math.Polynomial;

import com.echonest.api.v4.Segment;


public class Accentuator extends Visualization{

	public Accentuator(VisualizationManager parent){
		super(parent);

		Segment seg = parent.segments.get(parent.currentSegmentIndex);
		oVol = (float) seg.getLoudnessMax();
		oTim = seg.getTimbre();
	}
	
	float oVol;
	double[] oTim;
	
	boolean on;
	float x, y;
	float dx, dy;
	float d2x, d2y;
	
	public void update(float dt, float emitFrac){
		Segment seg = parent.segments.get(parent.currentSegmentIndex);
		
		float vol = (float)  seg.getLoudnessMax();
		double[] timbre = seg.getTimbre();
		
		float dist = Math.abs(vol - oVol);
		for(int i = 0; i < timbre.length; i++){
			dist += Math.abs(oTim[i] - timbre[i]);
		}
		
		if(dist > 4){
			if(!on){
				x = parent.rand.nextFloat() * parent.frame.width;
				y = parent.rand.nextFloat() * parent.frame.height;
				
				float theta = parent.rand.nextFloat() * (float)(Math.PI * 2);
				
				dx = (float)Math.cos(theta);
				dy = (float)Math.sin(theta);
				
				d2x = dy - dx;
				d2y = dx - dy;
				
				on = true;
			}
			//emit
			float rawEmit = dist * 100 * dt * emitFrac;
			int emissions = (int)rawEmit + (parent.rand.nextFloat() < (rawEmit - (int)rawEmit) ? 1 : 0);
			for(int i = 0; i < emissions; i++){
				Polynomial radius = new Polynomial(new float[]{1, 2, -1});
				Particle p = new Particle(x, y, dx, dy, d2x, d2y, 4, radius, parent.colR, parent.colG, parent.colB);
				particles.add(p);
				
			}
		}
		else{
			on = false;
		}
		
		updateParticles(dt);
	}
}
