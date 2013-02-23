package core;

import java.awt.Color;
import java.awt.Graphics;

public class Particle {

	float x, y;
	float dx, dy;

	float mass;
	
	float radius;
	
	float life;

	public Particle(float x, float y, float dx, float dy, float mass,
			float radius, float life) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.mass = mass;
		this.radius = radius;
		this.life = life;
	}

	public void update(){
		x += dx;
		y += dy;
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(Math.abs((int)x) % 255, Math.abs((int)y) % 255,  Math.abs((int)(x + y)) % 255));
		g.fillOval((int)(x - radius), (int)(y - radius), (int)(radius * 2 + 1), (int)(radius * 2 + 1));
	}
}
