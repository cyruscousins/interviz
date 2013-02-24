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
		blurColumnOffset = 1;
	}
	int a;
	public void drawToScreen(){
		testBlur(a++ % 2);
		//blurScreen(0.5f, 0, 0.5f, 0);
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
	int blurColumnOffset;
	public void blurScreen(float leftWeight, float rightWeight, float topWeight, float bottomWeight) {
		float totalWeight = 1 + leftWeight + rightWeight + topWeight + bottomWeight;
		assert(totalWeight > 0);
		for(int r=1; r<height-1; r++) {
			for(int c=blurColumnOffset + (r % 2); c<width-1; c+=2) {
				imgInt[r*width + c] = mergePixels((int)pixel(r,c), (int)(leftWeight*pixel(r,c-1)), (int)(rightWeight*pixel(r,c+1)), (int)(topWeight*pixel(r-1,c)), (int)(bottomWeight*pixel(r+1,c)), totalWeight);
			}
		}
		blurColumnOffset = blurColumnOffset == 1 ? 2 : 1;
	}
	private int pixel(int row, int col) {
		return imgInt[row*width + col];
	}
	private int mergePixels(int p1, int p2, int p3, int p4, int p5, float denominator) {
		int r = mergePixels(p1, p2, p3, p4, p5, denominator, 16);
		int g = mergePixels(p1, p2, p3, p4, p5, denominator, 8);
		int b = mergePixels(p1, p2, p3, p4, p5, denominator, 0);
		return (r << 16) | (g << 8) | b;
	}
	private int mergePixels(int p1, int p2, int p3, int p4, int p5, float denominator, int bitOffset) {
		int mask = 0xFF << bitOffset;
		return ((int)((p1 & mask + p2 & mask + p3 & mask + p4 & mask + p5 & mask) / denominator)) >>> bitOffset;
	}
	
	public void testBlur(int s){
		for(int y = 1; y < height - 1; y++){
			for(int x = 1 + (y % 2) + s; x < width - 1; x+=2){
				int i = x + y * width;
				
				int mul = 2;
				int div = 4;
				
				imgInt[i] = 
						((((0x0000ff & imgInt[i - 1]) + (0x0000ff & imgInt[i - width]) + (0x0000ff & imgInt[i]) * mul) >>  0) / div) <<  0 |
						((((0x00ff00 & imgInt[i - 1]) + (0x00ff00 & imgInt[i - width]) + (0x00ff00 & imgInt[i]) * mul) >>  8) / div) <<  8 | 
						((((0xff0000 & imgInt[i - 1]) + (0xff0000 & imgInt[i - width]) + (0xff0000 & imgInt[i]) * mul) >> 16) / div) << 16
						;
			}
		}
	}
}