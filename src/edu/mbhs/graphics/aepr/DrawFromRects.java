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
	public List<MatOfPoint> deltaS = new ArrayList<>(); // points representing
														// vectors for the
														// offset of each point
														// from the center of
														// the face
	public boolean deltaSFilled = false;
	public double faceRadius = 1;
	public int lineSize = 15;
	public String filter = "None";
	int red = 0;
	int blue = 0;
	int green = 255;

	public DrawFromRects(FindFaces ff) {
		this.ff = ff;
		this.stream = ff.stream;
		mGrey = new Mat();
		this.lines = new ArrayList<MatOfPoint>();
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
		
		//Only use the largest face
		Rect[] rects = faces.toArray();
		if(rects.length > 0) {
			Rect rect = rects[0];
			for (Rect r : rects) {
				if (r.width * r.height > rect.width * rect.height) {
					rect = r;
				}
			}

			Point center = new Point(rect.x + rect.width * 0.5, rect.y + rect.height * 0.5);
			pnt[0] = (rect.x + rect.width * 0.5) - 200;
			pnt[1] = (rect.y + rect.height * 0.5) - 50;
			// Not really needed
			pnt[2] = (rect.width * 0.5) * 5;
			pnt[3] = (rect.height * 0.5) * 5;

			/*
			 * if(deltaS==null){ deltaS=new ArrayList<MatOfPoint>(); List<Point>
			 * list=new ArrayList<>(); for(MatOfPoint mp:lines){ for(Point
			 * p:mp.toArray()){ list.add(new Point(p.x-center.x,p.y-center.y)); }
			 * 
			 * } MatOfPoint mof = new MatOfPoint(); mof.fromList(list);
			 * deltaS.add(mof); }
			 * 
			 * 
			 * List<MatOfPoint> list=new ArrayList<>(); for(MatOfPoint mp:deltaS){
			 * List<Point> list2=new ArrayList<>(); for(Point p:mp.toArray()){
			 * list2.add(new Point(p.x+center.x,p.y+center.y)); } MatOfPoint mof =
			 * new MatOfPoint(); mof.fromList(list2); list.add(mof); }
			 */

			// deltaSFilled=false;
			// do this whenever a point is added to update deltaS
			if (!deltaSFilled) {
				// List<MatOfPoint> deltaS = new ArrayList<>();
				// System.out.println(deltaS);
				// System.out.println(lines.size());
				// long startTime=System.nanoTime();

				List<MatOfPoint> tmp = new ArrayList<>(); // put everything into a
															// new list
				for (MatOfPoint mp : lines) { // go through each MatOfPoint, which
												// is an individual polyline
					List<Point> list2 = new ArrayList<>();
					for (Point p : mp.toArray()) { // go through each point to get
													// the offset from the center
						list2.add(new Point(p.x - center.x, p.y - center.y));
					}
					// stick it into a new MatOfPoint
					MatOfPoint mofp = new MatOfPoint();
					mofp.fromList(list2);
					tmp.add(mofp);
				}
				// System.out.println("Time: "+((System.nanoTime()-startTime)/1000.0)+" us");

				deltaS = tmp; // make deltaS the list we were storing into (tmp)
				deltaSFilled = true; // don't keep doing this step (unless updated
										// from elsewhere)
				faceRadius = rect.width; // scale with respect to this face (in
											// practice, the first one in the
											// MatOfRect)
			}
			// System.out.println(deltaS.size());
			//

			/*
			 * //For multiple polylines List<MatOfPoint> list = new ArrayList<>();
			 * for (MatOfPoint mp : deltaS) { List<Point> list2 = new ArrayList<>();
			 * for (Point p : mp.toArray()) { list2.add(new Point(p.x + center.x,
			 * p.y + center.y)); } MatOfPoint mofp = new MatOfPoint();
			 * mofp.fromList(list2); list.add(mofp); }
			 */

			// Takes deltaS and draws it on the face
			double scale = rect.width / faceRadius;
			// double scale=1;
			// System.out.println(scale);
			List<MatOfPoint> list = new ArrayList<>();
			for (MatOfPoint mp : deltaS) {
				List<Point> list2 = new ArrayList<>();
				for (Point p : mp.toArray()) {
					list2.add(new Point((p.x * scale + center.x), (p.y * scale + center.y))); // draw
																								// at
																								// the
																								// proper
																								// offset
																								// from
																								// the
																								// center,
																								// scaled
																								// to
																								// the
																								// size
																								// of
																								// the
																								// face
				}
				MatOfPoint mofp = new MatOfPoint();
				mofp.fromList(list2);
				list.add(mofp);
			}
			lines.clear();
			lines = list;
			// System.out.println(red+" "+green+" "+blue);
			Imgproc.polylines(m2i.mat, list, false, new Scalar(red, green, blue), (int) (lineSize * scale), 8, 0);
			//Imgproc.ellipse(m2i.mat, center, new Size(rect.width * 0.5, rect.height * 0.5), 0, 0, 360,
			//		new Scalar(0, 255, 0), 4, 8, 0);
		}

		switch (filter) {
		case "HSV":
			Imgproc.cvtColor(m2i.mat, m2i.mat, Imgproc.COLOR_RGB2HSV);
			break;
		case "Erode":
			Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * 12 + 1, 2 * 12 + 1));
			Imgproc.dilate(m2i.mat, m2i.mat, element);
			Imgproc.erode(m2i.mat, m2i.mat, element);
			Imgproc.cvtColor(m2i.mat, m2i.mat, Imgproc.COLOR_BGR2RGB);
			break;
		}
		return m2i.getFXImage(m2i.mat);
	}

	public boolean addMOP(MatOfPoint mop) {
		return lines.add(mop);
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
