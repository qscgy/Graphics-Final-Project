package edu.mbhs.graphics.aepr.util;

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.mbhs.graphics.aepr.FrameStream;
import edu.mbhs.graphics.aepr.Mat2Imag;


public class ApplyBlur implements FrameStream {
	
	FrameStream stream;
	Mat2Imag m2i=new Mat2Imag();
	Size blurSize=new Size(25,25);
	
	public ApplyBlur(FrameStream stream){
		this.stream=stream;
	}
	
	public Mat readMat(){
		Mat m=stream.readMat();
		//System.out.println("blurring");
		Imgproc.blur(m, m, blurSize);
		return m;
	}
	
	@Override
	public BufferedImage frame() {
		// AUTO Auto-generated method stub
		m2i.mat=this.readMat();
        return m2i.getImage(m2i.mat);
	}
	
	public void setBlurSize(Size size){
		blurSize=size;
	}

}
