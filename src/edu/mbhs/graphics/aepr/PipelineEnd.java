package edu.mbhs.graphics.aepr;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

public class PipelineEnd extends Application {
	
	ArrayList<MatOfPoint> points=new ArrayList<>();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//SET UP PIPELINE HERE
		VideoCapturer cap=new VideoCapturer();
		RGBConfig rgb=new RGBConfig(cap);
		FindFaces ff=new FindFaces(rgb);
		DrawFromRects dr=new DrawFromRects(ff,points);
		
		FrameStream end=dr;	//this should be the last element of the pipeline
		
		ImageView img=new ImageView(end.frame());
		StackPane root=new StackPane(img);
		
		List<Point> tmp=new ArrayList<>();
		root.setOnMouseDragged(e->{
			tmp.clear();
			tmp.add(new Point(e.getSceneX(),e.getSceneY()));
		    MatOfPoint l1=new MatOfPoint();
		    l1.fromList(tmp);
		    points.add(l1);
		    //dr.deltaS.add(new Point(e.getSceneX()-,e.getSceneY())))
		    dr.a++;
		    System.out.println(points.size());
		    dr.deltaSFilled=false;
		        //System.out.println(l1);
		        //System.out.println(points);
		});

		
		AnimationTimer timer=new AnimationTimer(){
			public void handle(long now){
				img.setImage(end.frame());
			}
		};
		
		timer.start();
		
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}

}
