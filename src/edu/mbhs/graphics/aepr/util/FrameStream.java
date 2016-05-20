package edu.mbhs.graphics.aepr.util;

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;

public interface FrameStream {
	/**
	 * Gives the Mat for the current frame.
	 * @return a Mat for the current frame for this stream
	 */
	public Mat readMat();
	
	/**
	 * Returns the current frame as an image
	 * @return the current frame for this stream as an image
	 */
	public BufferedImage frame();
}
