package edu.mbhs.graphics.aepr;

import javafx.scene.image.WritableImage;

import org.opencv.core.Mat;

public class VariableFilter implements FrameStream{
	
	Mat2Imag m2i=new Mat2Imag();
	FrameStream stream;
	String filter="None";
	
	public VariableFilter(FrameStream stream){
		this.stream=stream;
	}
	
	@Override
	public Mat readMat() {
		// AUTO Auto-generated method stub
		m2i.mat=stream.readMat();
	}

	@Override
	public WritableImage frame() {
		// AUTO Auto-generated method stub
		return m2i.getFXImage(readMat());
	}
	
	public void setFilter(String filter){
		this.filter=filter;
	}

}
