package edu.mbhs.graphics.aepr.misc;

/**
 * @author Andrew Phillips
 * @since 2/8/15
 * @class VideoCaptur, DrawFrame, and Draw
 * This program is a simple OpenCV program that takes input from a webcam, and 
 * outputs it in a JFrame.
 */
//Imports

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import edu.mbhs.graphics.aepr.Mat2Imag;

/*This is my code. It sets up the device for
 capturing video data*/
public class VideoCaptur {
	//Loads the native libraries used in this OpenCV program.
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	//Sets up the matrix, and the video capture device.
	CascadeClassifier face_cascade =new CascadeClassifier("/usr/local/opt/opencv3/share/OpenCV/haarcascades/haarcascade_frontalface_alt.xml");
	VideoCapture cap;
	Mat2Imag m2i = new Mat2Imag();
	 Mat mGrey=new Mat();  
	/*
	 * This constructor opens the video stream.
	 * @param none
	 * @return none
	 */
	VideoCaptur() {
		//Set up video stream
		cap = new VideoCapture();
		//Open the stream
		cap.open(0);

	}
	double[] pnt = new double[4];
	/*
	 * This program does a variety of things. The first thing  is send the video stream
	 * data to the matrix.
	 * @param none
	 * @return m2i.getImage();
	 */
	BufferedImage frame() {
		cap.read(m2i.mat);

		
		/*Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*12 + 1, 2*12 + 1));
        Imgproc.dilate(m2i.mat, m2i.mat, element);
        Imgproc.erode(m2i.mat, m2i.mat, element);
        Imgproc.cvtColor(m2i.mat, m2i.mat, Imgproc.COLOR_BGR2RGB);*/
		
		MatOfRect faces = new MatOfRect(); 
        m2i.mat.copyTo(mGrey);  
        Imgproc.cvtColor(m2i.mat, mGrey, Imgproc.COLOR_BGR2GRAY);  
        Imgproc.equalizeHist( mGrey, mGrey );  
        face_cascade.detectMultiScale(mGrey, faces); 
        
        for(Rect rect:faces.toArray())  
        {  
             Point center= new Point(rect.x + rect.width*0.5, rect.y + rect.height*0.5 );  
             pnt[0] = (rect.x + rect.width*0.5) - 200;
             pnt[1] = (rect.y + rect.height*0.5) - 50;
             //Not really needed
             pnt[2] = (rect.width*0.5) * 5;
             pnt[3] = (rect.height*0.5) * 5;
             
             MatOfPoint p1=new MatOfPoint(new Point(pnt[0],pnt[1]),new Point(pnt[2],pnt[3]));
             List<MatOfPoint> list=new ArrayList<>();
             list.add(p1);
             
             Imgproc.polylines(m2i.mat, list, true, new Scalar( 255, 0, 255 ));
             Imgproc.ellipse( m2i.mat, center, new Size( rect.width*0.5, rect.height*0.5), 0, 0, 360, new Scalar( 255, 0, 255 ), 4, 8, 0 );  
        }
		return m2i.getImage(m2i.mat);
	}
	double[] point() {
		
        return pnt;
	}
}

class DrawFrame2 extends JFrame {
	Draw d = new Draw();

	public static void main(String args[]) {
		new DrawFrame2();
	}

	public DrawFrame2() {
		System.out.println(":)");
		setTitle("Video capture 002");
		setSize(600, 600);
		add(d);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setVisible(true);
	}
}

class Draw extends JPanel {
	VideoCaptur cap = new VideoCaptur();
	VideoCaptur[] cap2 = new VideoCaptur[3];
	float alpha = .3f;
	public Draw() {
		setBackground(Color.white);
	}
	int i = 0;
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		double[] img = cap.point();
		
		g.drawImage(cap.frame(), 0, 0, getWidth() , getHeight() , null);
		//g.drawRect((int) img[0]-120, (int) img[1]-10, 250, 250);
		repaint();
		
	}
}