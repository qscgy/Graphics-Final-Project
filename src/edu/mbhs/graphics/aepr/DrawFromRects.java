package edu.mbhs.graphics.aepr;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.WritableImage;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class DrawFromRects implements FrameStream {
	Mat2Imag m2i = new Mat2Imag();
	FindFaces ff;
	Mat mGrey;
	FrameStream stream;
	CascadeClassifier face_cascade =new CascadeClassifier("/usr/local/opt/opencv3/share/OpenCV/haarcascades/haarcascade_frontalface_alt.xml");
	
	public DrawFromRects(FindFaces ff){
		this.ff=ff;
		this.stream=ff.stream;
		mGrey=new Mat();
	}
	
	@Override
	public Mat readMat() {
		// AUTO Auto-generated method stub
		return null;
	}

	@Override
	public WritableImage frame() {
		double[] pnt=new double[4];
		
		/*Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*12 + 1, 2*12 + 1));
        Imgproc.dilate(m2i.mat, m2i.mat, element);
        Imgproc.erode(m2i.mat, m2i.mat, element);
        Imgproc.cvtColor(m2i.mat, m2i.mat, Imgproc.COLOR_BGR2RGB);*/
		
		MatOfRect faces=ff.getRects();
		m2i.mat=stream.readMat();
        
        for(Rect rect:faces.toArray())  
        {  
             Point center= new Point(rect.x + rect.width*0.5, rect.y + rect.height*0.5 );  
             pnt[0] = (rect.x + rect.width*0.5) - 200;
             pnt[1] = (rect.y + rect.height*0.5) - 50;
             //Not really needed
             pnt[2] = (rect.width*0.5) * 5;
             pnt[3] = (rect.height*0.5) * 5;
             
             MatOfPoint l1=new MatOfPoint(new Point(center.x,rect.y),new Point(center.x,center.y+rect.height*.5));
             MatOfPoint l2=new MatOfPoint(new Point(rect.x,center.y),new Point(center.x+rect.width*.5,center.y));
             List<MatOfPoint> list=new ArrayList<>();
             list.add(l1);
             list.add(l2);
             
             Imgproc.polylines(m2i.mat, list, false, new Scalar( 0, 255, 0 ),2,8,0);
             Imgproc.ellipse( m2i.mat, center, new Size( rect.width*0.5, rect.height*0.5), 0, 0, 360, new Scalar( 0, 255, 0 ), 4, 8, 0 );  
        }
		return m2i.getFXImage(m2i.mat);
	}

}
