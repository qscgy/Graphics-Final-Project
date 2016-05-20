package edu.mbhs.graphics.aepr.util;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import org.opencv.core.Core;
import org.opencv.core.Mat;

//NOT MY CODE...
public class Mat2Imag {
    public Mat mat = new Mat();
    BufferedImage img;
    byte[] dat;
    public Mat2Imag() {
    }
    public Mat2Imag(Mat mat) {
        getSpace(mat);
    }
    public void getSpace(Mat mat) {
        this.mat = mat;
        int w = mat.cols(), h = mat.rows();
        if (dat == null || dat.length != w * h * 3)
            dat = new byte[w * h * 3];
        if (img == null || img.getWidth() != w || img.getHeight() != h
            || img.getType() != BufferedImage.TYPE_3BYTE_BGR)
                img = new BufferedImage(w, h, 
                            BufferedImage.TYPE_3BYTE_BGR);
        }
        public BufferedImage getImage(Mat mat){
            getSpace(mat);
            mat.get(0, 0, dat);
            img.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), dat);
        return img;
    }
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
