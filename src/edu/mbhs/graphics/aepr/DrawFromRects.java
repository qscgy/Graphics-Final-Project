package edu.mbhs.graphics.aepr;

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
	List<MatOfPoint> lines = new ArrayList<>();
	CascadeClassifier face_cascade = new CascadeClassifier(
			"/usr/local/opt/opencv3/share/OpenCV/haarcascades/haarcascade_frontalface_alt.xml");
	public List<MatOfPoint> deltaS = new ArrayList<>();
	public boolean deltaSFilled=false;
	public int a = 0;
	public double faceRadius=1;
	public int lineSize=15;
	public String filter="None";
	int red=0;
	int blue=0;
	int green=0;
	
	public DrawFromRects(FindFaces ff, List<MatOfPoint> lines) {
		this.ff = ff;
		this.stream = ff.stream;
		mGrey = new Mat();
		this.lines = lines;
	}

	@Override
	public Mat readMat() {
		// AUTO Auto-generated method stub
		return null;
	}

	@Override
	public WritableImage frame() {
		double[] pnt = new double[4];

		/*
		 * Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new
		 * Size(2*12 + 1, 2*12 + 1)); Imgproc.dilate(m2i.mat, m2i.mat, element);
		 * Imgproc.erode(m2i.mat, m2i.mat, element); Imgproc.cvtColor(m2i.mat,
		 * m2i.mat, Imgproc.COLOR_BGR2RGB);
		 */

		MatOfRect faces = ff.getRects();
		m2i.mat = stream.readMat();

		for (Rect rect : faces.toArray()) {
			Point center = new Point(rect.x + rect.width * 0.5, rect.y + rect.height * 0.5);
			pnt[0] = (rect.x + rect.width * 0.5) - 200;
			pnt[1] = (rect.y + rect.height * 0.5) - 50;
			// Not really needed
			pnt[2] = (rect.width * 0.5) * 5;
			pnt[3] = (rect.height * 0.5) * 5;
			
			/*
			 * if(deltaS==null){ deltaS=new ArrayList<MatOfPoint>(); List<Point>
			 * list=new ArrayList<>(); for(MatOfPoint mp:lines){ for(Point
			 * p:mp.toArray()){ list.add(new Point(p.x-center.x,p.y-center.y));
			 * }
			 * 
			 * } MatOfPoint mof = new MatOfPoint(); mof.fromList(list);
			 * deltaS.add(mof); }
			 * 
			 * 
			 * List<MatOfPoint> list=new ArrayList<>(); for(MatOfPoint
			 * mp:deltaS){ List<Point> list2=new ArrayList<>(); for(Point
			 * p:mp.toArray()){ list2.add(new Point(p.x+center.x,p.y+center.y));
			 * } MatOfPoint mof = new MatOfPoint(); mof.fromList(list2);
			 * list.add(mof); }
			 */
			
			//do this whenever a point is added to update deltaS
			if (!deltaSFilled) {
				//List<MatOfPoint> deltaS = new ArrayList<>();
				//System.out.println(deltaS);
				//System.out.println(lines.size());
				//long startTime=System.nanoTime();
				List<MatOfPoint> tmp=new ArrayList<>();	//put everything into a new list
				for (MatOfPoint mp : lines) {
					List<Point> list2 = new ArrayList<>();
					for (Point p : mp.toArray()) {
						list2.add(new Point(p.x - center.x, p.y - center.y));
					}
					MatOfPoint mofp = new MatOfPoint();
					mofp.fromList(list2);
					tmp.add(mofp);
				}
				//System.out.println("Time: "+((System.nanoTime()-startTime)/1000.0)+" us");
				
				deltaS=tmp;	//make deltaS the list with all of the coordinates relative to the center			
				deltaSFilled=true;
				faceRadius=rect.width;
			}
			//System.out.println(deltaS.size());
			//
			
			/*
			//For multiple polylines
			List<MatOfPoint> list = new ArrayList<>();
			for (MatOfPoint mp : deltaS) {
				List<Point> list2 = new ArrayList<>();
				for (Point p : mp.toArray()) {
					list2.add(new Point(p.x + center.x, p.y + center.y));
				}
				MatOfPoint mofp = new MatOfPoint();
			mofp.fromList(list2);
			list.add(mofp);
			}
			*/
			
			//Turns everything into a single polyline
			double scale=rect.width/faceRadius;
			//System.out.println(scale);
			List<MatOfPoint> list = new ArrayList<>();
			for (MatOfPoint mp : deltaS) {
				List<Point> list2 = new ArrayList<>();
				for (Point p : mp.toArray()) {
					list2.add(new Point((p.x*scale + center.x), (p.y*scale + center.y)));
				}
				MatOfPoint mofp = new MatOfPoint();
				mofp.fromList(list2);
				list.add(mofp);
			}

			//System.out.println(red+" "+green+" "+blue);
			Imgproc.polylines(m2i.mat, list, false, new Scalar(red, green, blue), (int)(lineSize*scale), 8, 0);
			//Imgproc.ellipse(m2i.mat, center, new Size(rect.width * 0.5, rect.height * 0.5), 0, 0, 360, new Scalar(0, 255, 0), 4, 8, 0);
		}
		switch(filter){
		case "HSV":
			Imgproc.cvtColor(m2i.mat, m2i.mat, Imgproc.COLOR_RGB2HSV);
			break;
		case "Erode":
			Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*12 + 1, 2*12 + 1));
	        Imgproc.dilate(m2i.mat, m2i.mat, element);
	        Imgproc.erode(m2i.mat, m2i.mat, element);
	        Imgproc.cvtColor(m2i.mat, m2i.mat, Imgproc.COLOR_BGR2RGB);
	        break;
		}
		return m2i.getFXImage(m2i.mat);
	}

	public void setRed(int red) {
		this.red = red;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public void setGreen(int green) {
		this.green = green;
	}

}
