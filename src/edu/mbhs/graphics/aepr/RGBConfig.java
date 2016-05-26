package edu.mbhs.graphics.aepr;

import javafx.scene.image.WritableImage;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class RGBConfig implements FrameStream {

	Mat2Imag m2i = new Mat2Imag();
	FrameStream stream;
	
	public RGBConfig(FrameStream stream){
		this.stream=stream;
	}
	
	@Override
	public Mat readMat() {
		// AUTO Auto-generated method stub
		Mat mat=stream.readMat();
		Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGB);  
		return mat;
	}

	@Override
	public WritableImage frame() {
		m2i.mat=this.readMat();
		return m2i.getFXImage(m2i.mat);
	}

}
