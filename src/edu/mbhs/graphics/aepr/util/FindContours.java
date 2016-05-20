package edu.mbhs.graphics.aepr.util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.mbhs.graphics.aepr.FrameStream;
import edu.mbhs.graphics.aepr.Mat2Imag;

public class FindContours implements FrameStream {
	FrameStream stream;
	Mat2Imag m2i=new Mat2Imag();
	
	public FindContours(FrameStream stream){
		this.stream=stream;
	}
	
	@Override
	public Mat readMat() {
		// AUTO Auto-generated method stub
		Mat src = stream.readMat();
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2HSV);
        Mat dest = new Mat();
      // Mat dest = new Mat(src.width(), src.height(), src.type());
        Core.inRange(src, new Scalar(26,71,0), new Scalar(181,107,256), dest);
        Mat erode = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
        Mat dilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
        //Imgproc.erode(dest, dest, erode);
        //Imgproc.erode(dest, dest, erode);

        //Imgproc.dilate(dest, dest, dilate);
        //Imgproc.dilate(dest, dest, dilate);

        List<MatOfPoint> contours = new ArrayList<>();

        Imgproc.findContours(dest, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(dest, contours, 1, new Scalar(255,255,0));
        return dest;
	}

	@Override
	public BufferedImage frame() {
		// AUTO Auto-generated method stub
		m2i.mat=this.readMat();
        return m2i.getImage(m2i.mat);
	}

}
