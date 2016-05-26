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
		// AUTO Auto-generated method stub
		
		//SET UP PIPELINE HERE
		VideoCapturer cap=new VideoCapturer();
		//blur=new ApplyBlur(cap);
		//blur.setBlurSize(new Size(10,10));
		FindFaces ff=new FindFaces(cap);
		DrawFromRects dr=new DrawFromRects(ff);
		FrameStream end=dr;
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
