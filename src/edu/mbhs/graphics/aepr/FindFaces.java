package edu.mbhs.graphics.aepr;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Detects faces with Haar cascades to be used by other pipeline elements.
 * @author Sam Ehrenstein
 *
 */
public class FindFaces {
	Mat2Imag m2i = new Mat2Imag();
	Mat mGrey=new Mat();
	FrameStream stream;
	//CascadeClassifier face_cascade =new CascadeClassifier("/usr/local/opt/opencv3/share/OpenCV/haarcascades/haarcascade_frontalface_alt.xml");
	CascadeClassifier face_cascade =new CascadeClassifier("res/haarcascade_frontalface_alt.xml");
	public FindFaces(FrameStream stream){
		this.stream=stream;
	}
	
	public MatOfRect getRects() {
		Mat mat=stream.readMat();
		MatOfRect faces = new MatOfRect(); 
        mat.copyTo(mGrey);  
        Imgproc.cvtColor(mat, mGrey, Imgproc.COLOR_RGB2GRAY);  
        Imgproc.equalizeHist( mGrey, mGrey );  
        face_cascade.detectMultiScale(mGrey, faces); 
		return faces;
	}

}
