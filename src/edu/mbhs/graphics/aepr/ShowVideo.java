package edu.mbhs.graphics.aepr;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Size;

import edu.mbhs.graphics.aepr.VideoCapturer;
import edu.mbhs.graphics.aepr.util.ApplyBlur;
import edu.mbhs.graphics.aepr.util.FindContours;

/*
 * NOTE ON PIPELINES:
 * I've set this up around the pipeline design pattern. Each rendering step
 * has its own class. Unless it is an end step (e.g. ShowVideo), it should
 * implement FrameStream. This allows it to be plugged into the constructor
 * of the next step. The constructor of every step except the initial step
 * should take a FrameStream in its constructor, and process that.
 */

public class ShowVideo extends JFrame {
	
	DrawFrame d=new DrawFrame();
	public static void main(String[] args){
		new ShowVideo();
	}
	
	public ShowVideo(){
		//System.out.println(":)");
		setTitle("Video capture 001");
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(d);
		setResizable(true);
		setVisible(true);
		repaint();
	}
	
	private class DrawFrame extends JPanel{
		VideoCapturer cap;
		ApplyBlur blur;
		FindContours fc;
		FindFaces ff;
		DrawFromRects dr;
		FrameStream end;	//the final product of the pipeline
		
		public DrawFrame(){
			super();
			
			//SET UP PIPELINE HERE
			cap=new VideoCapturer();
			//blur=new ApplyBlur(cap);
			//blur.setBlurSize(new Size(10,10));
			ff=new FindFaces(cap);
			dr=new DrawFromRects(ff);
			end=dr;
		}
		
		public void paintComponent(Graphics g){
			super.paintComponents(g);
			g.drawImage(end.frame(), 0, 0, getWidth() , getHeight() , null);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// AUTO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
		}
	}
	
}
