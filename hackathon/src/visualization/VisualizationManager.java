package visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Segment;
import com.echonest.api.v4.TimedEvent;
import com.echonest.api.v4.Track;
import com.echonest.api.v4.TrackAnalysis;

import core.VisualizationFrame;

import math.F1Var;
import math.F2Var;
import math.Polynomial;

public class VisualizationManager implements MouseMotionListener, MouseListener{
	float mouseX, mouseY;
	public float mouseClickTime;
	
	VisualizationFrame frame;
	
	
	ArrayList<Visualization> visualizations = new ArrayList<Visualization>();
	
	F2Var noise;
	
	public VisualizationManager(VisualizationFrame frame){
		this.frame = frame;
		noise = math.Noise3.noiseSVF(128, 512, 0, 4);
		
		visualizations.add(new CenterVisualization(this));
		visualizations.add(new PitchClassEmitter(this));
		visualizations.add(new Accentuator(this));
		visualizations.add(new CenterVisualizationPitchClass(this));
//		visualizations.add(new AcceloVisualizer(this));
		visualizations.add(new BeatPulser(this));
		visualizations.add(new BeatPulser(this));
		visualizations.add(new BeatPulser(this));
		
		frame.frame.addMouseMotionListener(this);
		frame.frame.addMouseListener(this);
	}
	
	public float time;
	
	public int emitCount;
	
	public float relativeLoudness;
	
	public float tempoNow, beatConfidence;
	
	public int colR, colG, colB;
	public int clearness, squareness, strangeness;
	
	Random rand = new Random();

	boolean newBeat = false;
	
	public void update(float dt){
		if(currentSegmentIndex >= segments.size() || currentBeatIndex >= beats.size()){
			updateFlush(dt);
			return;
		}
		this.time += dt;
		
		mouseClickTime -= dt;
		if(mouseClickTime < 0){
			mouseClickTime = 0;
		}
		
		float interpAmt = (float)Math.pow(.1f, dt);
		

		Segment seg = segments.get(currentSegmentIndex);
		
		while(time - segT0 > seg.getDuration()){
			segT0 += seg.getDuration();
			currentSegmentIndex++;
			if(currentSegmentIndex >= segments.size()){
				System.out.println("SONG FINISHED (segments)!");
				return;
			}
			else seg = segments.get(currentSegmentIndex);
			
		}
		
		TimedEvent beat = beats.get(currentBeatIndex);

		newBeat = false;
		
		while(time - beatT0 > beat.getDuration()){
			beatT0 += beat.getDuration();
			currentBeatIndex++;
			if(currentBeatIndex >= beats.size()){
				System.out.println("SONG FINISHED (beats)!");
				return;
			}
			else beat = beats.get(currentBeatIndex);
			
			newBeat = true;
			beatConfidence = (float) beat.getConfidence();
			
			tempoNow = (float)(60 / beat.getDuration());
//			System.out.println(tempoNow);
		}
		
		relativeLoudness = interpolate(interpAmt, relativeLoudness, (float)(seg.getLoudnessMax() / tLoudness));
		

		calcParticles(interpAmt, seg);
		
		for(int i = 0; i < visualizations.size(); i++){
			visualizations.get(i).update(dt, (float)(Math.sin(i + time) * .5f + .5f) * (newBeat ? 2 : 1));
		}
	}

	public void updateFlush(float dt){
		this.time += dt;
		
		int currentSegmentIndex = segments.size() - 1;
		
		float interpAmt = (float)Math.pow(.1f, dt);
		

		Segment seg = segments.get(currentSegmentIndex);
		
		TimedEvent beat = beats.get(currentBeatIndex - 1);
		newBeat = false;
		beatConfidence = 0;
		
		relativeLoudness = interpolate(interpAmt, relativeLoudness, 0);
		
		tempoNow = (float)(Math.pow(.5f, dt) * tempoNow);

		calcParticles(interpAmt, seg);
		
		for(int i = 0; i < visualizations.size(); i++){
			visualizations.get(i).update(dt, 0);
		}
	}
	
	public void calcParticles(float interpAmt, Segment seg){
		double[] timbres = seg.getTimbre(); //12 values, centered around 0
		
		colR = (int)interpolate(interpAmt, colR, (float)(2 + timbres[0] * 1.5 + (timbres[3] + timbres[4]) * .25) * 64);
		colG = (int)interpolate(interpAmt, colG, (float)(2 + timbres[1] * 1.5 + (timbres[3] + timbres[5]) * .25) * 64);
		colB = (int)interpolate(interpAmt, colB, (float)(2 + timbres[2] * 1.5 + (timbres[4] + timbres[5]) * .25) * 64);
		
		squareness  = (int)interpolate(interpAmt, squareness,  (float)((1 + timbres[6]) * 127.9));
		strangeness = (int)interpolate(interpAmt, strangeness, (float)((1 + timbres[7]) * 127.9));
		clearness   = (int)interpolate(interpAmt, clearness,   (float)((1 + timbres[8]) * 127.9));
	}
	
	Track track;
	TrackAnalysis analysis;
	
	List<TimedEvent> beats;
	List<Segment> segments;
	int currentSegmentIndex, currentBeatIndex;
	float segT0, beatT0;
	
	float tLoudness, liveness;
	
	public void setTrack(Track track) throws EchoNestException{
		this.track = track;
		
		tLoudness = (float)track.getLoudness();
		
		liveness = (float)(track.getLoudness() + 1) * .5f;
		
		analysis = track.getAnalysis();

		beats = analysis.getBeats();
		segments = analysis.getSegments();
		
		currentSegmentIndex = 0;
		segT0 = time;
		
		tempoNow = (float)track.getTempo();
	}
	
	public void render(){
		Graphics g = frame.bufferG;
		g.setColor(new Color(0, 0, 0, 8));
		g.fillRect(0, 0, frame.width, frame.height);
		
		for(Visualization vis : visualizations){
			vis.render(g);
		}
		
		frame.drawToScreen();
	}
	
	public float interpolate(float interp, float a, float b){
		return a * interp + (1 - interp) * b;
	}
	
	public Particle genParticle(float x, float y, float dx, float dy, float d2x, float d2y, float mass, F1Var radius){
		return new Particle(x, y, dx, dy, d2x, d2y, 1, radius, colR, colG, colB, clearness, squareness, strangeness);
	}
	

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		mouseX = arg0.getX();
		mouseY = arg0.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mouseClickTime = 1f;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
