package visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import math.F1Var;

public class Particle {
	public static Random rand = new Random();
	
	float x, y;
	
	float dx, dy;
	
	float d2x, d2y;

	float mass;
	
	F1Var radius;
	
	float rad;
	
	float time;
	
	int br, bg, bb, ba;
	
	int strangeness;
	
	public static final int TYPE_FR = 0, TYPE_DR = 1, TYPE_FO = 2, TYPE_DO = 3;
	
	static final int types = 4;
	static int lastType;
	int type = lastType++ % types;

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
		
		ba = Math.min(255, lastType % 250 + 50);
	}
	
	public Particle(float x, float y, float dx, float dy, float d2x, float d2y, float mass, F1Var radius, int colR, int colG, int colB, int clearness, int squareness, int strangeness) {
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
		
		ba = Math.max(0, Math.min(255, lastType % 25 * 9 + 50 - clearness));
		
		this.strangeness = strangeness;
		
		type /= 2;
		if(rand.nextInt(255) < squareness) type += 2; //square 
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
		if(type == 0){
			g.fillRect((int)(x - rad), (int)(y - rad), (int)(rad * 2 + 1), (int)(rad * 2 + 1));
		}
		else if(type == 1){
			g.drawRect((int)(x - rad), (int)(y - rad), (int)(rad * 2 + 1), (int)(rad * 2 + 1));
		}
		else if(type == 2){
			g.fillOval((int)(x - rad), (int)(y - rad), (int)(rad * 2 + 1), (int)(rad * 2 + 1));
		}
		else if(type == 3){
			g.drawOval((int)(x - rad), (int)(y - rad), (int)(rad * 2 + 1), (int)(rad * 2 + 1));
		}
	}
	
	public Color col(float time, float x, float y, int br, int bg, int bb){
		int dIntens = 20 + strangeness / 10;
		time *= strangeness * .1f;
		return new Color(
				lim((int)( Math.sin(time) * dIntens) + br, 0, 255), 
				lim((int)( Math.cos(time) * dIntens) + bg, 0, 255), 
				lim((int)(-Math.sin(time) * dIntens) + bb, 0, 255),
				(int)(ba * Math.min(rad * .25f, 1)));
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
