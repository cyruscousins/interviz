package core;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseMotionListener;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;

public class VisualizationFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	//manage a buffered frame
	public Frame frame;
	public BufferedImage buffer;
	public int[] imgInt;
	public Graphics frameG, bufferG;
	public int width, height;
	int xo = 5, yo = 25; //offsets
	public VisualizationFrame(int width, int height, MouseMotionListener listener){
		frame = new Frame();
		this.frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		         System.exit(0);
		    }
		});
		
		addMouseMotionListener(listener);
		
		this.width = width;
		this.height = height;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void init(){
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		imgInt = ((DataBufferInt)buffer.getRaster().getDataBuffer()).getData();
		bufferG = buffer.getGraphics();
		frame.setSize(width + xo * 2, height + xo + yo);
		frame.setVisible(true);
		frameG = frame.getGraphics();
	}
	public void drawToScreen(){
		frameG.drawImage(buffer, xo, yo, null);
	}
	public void takeScreenShot(String name){
		try{
			ImageIO.write(buffer, ".png", new File("PNGOUT" + name));
			System.out.println("Screen shot successful.");
		} catch(Exception e){
			System.out.println("Error taking screenshot.");
		}
	}
}