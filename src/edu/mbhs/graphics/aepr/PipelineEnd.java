package edu.mbhs.graphics.aepr;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PipelineEnd extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		//SET UP PIPELINE HERE
		VideoCapturer cap=new VideoCapturer();
		RGBConfig rgb=new RGBConfig(cap);
		FindFaces ff=new FindFaces(rgb);
		DrawFromRects dr=new DrawFromRects(ff);
		
		FrameStream end=dr;	//this should be the last element of the pipeline
		
		ImageView img=new ImageView(end.frame());
		StackPane root=new StackPane(img);
		
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
