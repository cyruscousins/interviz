package visualization;

import sun.awt.geom.AreaOp.AddOp;
import math.Polynomial;

import com.echonest.api.v4.Segment;


public class Accentuator extends Visualization{

	public Accentuator(VisualizationManager parent){
		super(parent);
	}
	
	float oVol;
	double[] oTim;
	
	int oSeg;
	
	float dist;
	
	boolean on;
	float x, y;
	float dx, dy;
	float d2x, d2y;
	
	
	
	public void update(float dt, float emitFrac){
		Segment seg = parent.segments.get(parent.currentSegmentIndex);
		
		int curSeg = parent.currentSegmentIndex;
		if(curSeg != oSeg){
			if(oTim == null){
				oVol = (float) seg.getLoudnessMax();
				oTim = seg.getTimbre();
			}
			
			float vol = (float)  seg.getLoudnessMax();
			double[] timbre = seg.getTimbre();
			
			dist = Math.abs(vol - oVol);
			for(int i = 0; i < timbre.length; i++){
				dist += Math.abs(oTim[i] - timbre[i]);
			}
			
			dist /= 620; //normalize it.
		}
		
		if(dist > 1){
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
			float rawEmit = dist * 20 * dt * emitFrac;
			int emissions = (int)rawEmit + (parent.rand.nextFloat() < (rawEmit - (int)rawEmit) ? 1 : 0);
			for(int i = 0; i < emissions; i++){
				float tTheta = (float)(parent.rand.nextFloat() * Math.PI * 2);
				float tRad = parent.rand.nextFloat() * 20;
				
				Polynomial radius = new Polynomial(new float[]{1, 2, -1.5f + .5f * parent.rand.nextFloat()});
				Particle p = parent.genParticle(x, y, dx + (float)(Math.cos(tTheta)) * tRad, dy + (float)(Math.sin(tTheta)) * tRad, d2x, d2y, 4, radius);
				particles.add(p);
				
			}
		}
		else{
			on = false;
		}
		
		updateParticles(dt);
	}
}
