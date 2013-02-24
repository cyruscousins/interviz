package visualization;

import java.awt.Color;
import java.awt.Graphics;

import math.F1Var;

public class Particle {

	float x, y;
	
	float dx, dy;
	
	float d2x, d2y;

	float mass;
	
	F1Var radius;
	
	float rad;
	
	float time;
	
	int br, bg, bb;

	public Particle(float x, float y, float dx, float dy, float d2x, float d2y, float mass, F1Var radius, int colR, int colG, int colB) {
		this.x = x;
		this.y = y;
		
		this.dx = dx;
		this.dy = dy;
		
		this.d2x = d2x;
		this.d2y = d2y;
		
		this.mass = mass;
		this.radius = radius;
		
		br = colR;
		bg = colG;
		bb = colB;
	}

	//returns true on death.
	public boolean update(float dt){
		x += dx * dt;
		y += dy * dt;
		
		dx += d2x * dt;
		dy += d2y * dt;
		
		this.time += dt;
		
		rad = radius.val(this.time);
		
		return rad < 0;
	}
	
	public void render(Graphics g) {
		g.setColor(col(time, x, y, br, bg, bb));
		g.drawOval((int)(x - rad), (int)(y - rad), (int)(rad * 2 + 1), (int)(rad * 2 + 1));
	}
	
	public Color col(float time, float x, float y, int br, int bg, int bb){
		int dIntens = 40;
		float psclr = .01f;
		time *= 1f;
		return new Color(
				lim((int)(Math.sin(time + x * psclr) * dIntens) + br, 0, 255), 
				lim((int)(Math.sin(time + y * psclr) * dIntens) + bg, 0, 255), 
				lim((int)(Math.sin(time + (x + y) * psclr) * dIntens) + bb, 0, 255),
				100);
	}
	
	int lim(int i, int a, int b){
		if(i < a){
			return a;
		}
		else if(i > b){
			return b;
		}
		return i;
	}
}
