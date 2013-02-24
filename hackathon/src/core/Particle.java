package core;

import java.awt.Color;
import java.awt.Graphics;

import math.F1Var;

public class Particle {

	float x, y;
	
	float dx, dy;
	
	F1Var d2x, d2y;

	float mass;
	
	F1Var radius;
	
	float rad;
	
	float time;
	
	int br, bg, bb;

	public Particle(float x, float y, float dx, float dy, F1Var d2x, F1Var d2y, float mass, F1Var radius, int colR, int colG, int colB) {
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

	public void update(float time){
		x += dx * time;
		y += dy * time;
		
		dx += d2x.val(this.time);
		dy += d2y.val(this.time);
		
		this.time += time;
	}
	
	public void render(Graphics g) {
		rad = radius.val(time);
		
		g.setColor(col(x, y, br, bg, bb));
		g.drawOval((int)(x - rad), (int)(y - rad), (int)(rad * 2 + 1), (int)(rad * 2 + 1));
	}
	
	public Color col(float x, float y, int br, int bg, int bb){
		int dIntens = 45;
		float sclr = .1f;
		return new Color(
				lim((int)(Math.sin(x * sclr) * dIntens) + br, 0, 255), 
				lim((int)(Math.sin(y * sclr) * dIntens) + bg, 0, 255), 
				lim((int)(Math.sin((x + y) * sclr) * dIntens) + bb, 0, 255),
				200);
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
