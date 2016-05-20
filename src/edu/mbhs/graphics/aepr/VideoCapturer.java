package edu.mbhs.graphics.aepr;
import java.awt.image.BufferedImage;

import javafx.scene.image.Image;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;


public class VideoCapturer implements FrameStream{
	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	VideoCapture capture;
	Mat2Imag m2i;
	public VideoCapturer(){
		capture=new VideoCapture();
		m2i=new Mat2Imag();
		capture.open(0);
	}
	
	public BufferedImage frame(){
		capture.read(m2i.mat);
        return m2i.getImage(m2i.mat);
	}
	
	@Override
	public Mat readMat() {
		// AUTO Auto-generated method stub
		Mat m=new Mat();
		capture.read(m);
		return m;
	}
}
