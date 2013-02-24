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
	
	public void render(Graphics g){
		for(Particle particle : particles){
			particle.render(g);
		}
	}
}
