package core;

import java.awt.Color;
import java.awt.Graphics;

import math.F1Var;

public class Particle {

	float x, y;
	float dx, dy;

	float mass;
	
	F1Var radius;
	
	float time;
	
	int br, bg, bb;

	public Particle(float x, float y, float dx, float dy, float mass,
			F1Var radius) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.mass = mass;
		this.radius = radius;
		
		br = bg = bb = 100;
	}

	public void update(float time){
		x += dx;
		y += dy;
		
		this.time += time;
	}
	
	public void render(Graphics g) {
		float rad = radius.val(time);
		
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
