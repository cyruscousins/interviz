package visualization;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;


public abstract class Visualization {
	VisualizationManager parent;
	public List<Particle> particles;
	
	public Visualization(VisualizationManager parent){
		this.parent = parent;
		particles = new ArrayList<Particle>();
	}
	
	public abstract void update(float t, float amtScalar);
	
	public void updateParticles(float dt){
		
		float drag = (float)(Math.pow(.75f, dt));
		
		int len = particles.size();
		for(int i = 0; i < len; i++){
			Particle p = particles.get(i);

			p.dx += parent.noise.dydx(p.x, p.y) * 64 * dt / p.mass;
			p.dy += parent.noise.dydz(p.x, p.y) * 64 * dt / p.mass;
			
			//Push away from bottom edge
			p.dy -= ((p.y / parent.frame.height) * (p.y / parent.frame.height) * 32) * dt;
			
			p.dx *= drag;
			p.dy *= drag;
			
			if(p.update(dt)){ //if dead
				len--;
				if(i == len){ //last elem
					particles.remove(len);
					break;
				}
				particles.set(i, particles.remove(len));
				i--;
			}
		}
	}
	
	public void render(Graphics g){
		for(Particle particle : particles){
			particle.render(g);
		}
	}
}
