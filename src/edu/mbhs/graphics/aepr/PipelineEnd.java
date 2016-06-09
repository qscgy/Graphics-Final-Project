package edu.mbhs.graphics.aepr;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

public class PipelineEnd extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//SET UP PIPELINE HERE
		VideoCapturer cap=new VideoCapturer();
		RGBConfig rgb=new RGBConfig(cap);
		FindFaces ff=new FindFaces(rgb);
		DrawFromRects dr=new DrawFromRects(ff);
		FrameStream end=dr;	//this should be the last element of the pipeline
		//END PIPELINE SETUP
		
		ImageView img=new ImageView(end.frame());
		Group root=new Group(img);
		
		GridPane controls=new GridPane();
		controls.setHgap(10);
		controls.setVgap(10);
		
		//set up size slider
		Label sizeLabel=new Label("Size: ");
		Slider size=new Slider(1,100,10);
		size.valueProperty().addListener(new ChangeListener<Number>(){
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// AUTO Auto-generated method stub
				dr.lineSize=newValue.intValue();
			}
		});
		controls.add(sizeLabel,0,0);
		controls.add(size,1,0);
		
		
		//set up filters combo box
		ComboBox<String> filters=new ComboBox<>();
		ObservableList<String> filterNames=FXCollections.observableArrayList(new 
				String[]{"None","HSV","Erode"});
		filters.getItems().addAll(filterNames);
		filters.setValue("None");
		filters.setOnAction(e->{
			dr.filter=filters.getValue();
		});
		Label filterLabel=new Label("Filter: ");
		controls.add(filterLabel,0,1);
		controls.add(filters, 1, 1);
		
		//reset tmp on mouse press to be ready to receive points and then add the start point
		List<Point> tmp=new ArrayList<>();
		root.setOnMousePressed(e->{
			tmp.clear();
			tmp.add(new Point(e.getSceneX(),e.getSceneY()));
		});
		//put each point in tmp (this fires continually while mouse is down)
		root.setOnMouseDragged(e->{
			tmp.add(new Point(e.getSceneX(),e.getSceneY()));
		        //System.out.println(l1);
		        //System.out.println(points);
		});
		root.setOnMouseReleased(e->{
			MatOfPoint l1=new MatOfPoint();
		    l1.fromList(tmp);	//get a MatOfPoint for the polyline that was just drawn
			dr.addMOP(l1);	//give it to dr
		    //dr.deltaS.add(new Point(e.getSceneX()-,e.getSceneY())))
		    //System.out.println(points.size());
		    dr.deltaSFilled=false;	//make dr calculate deltaS for the new points
		});
		
		//set up buttons
		Button redraw=new Button("Redraw");
		Button clear=new Button("Clear");
		redraw.setOnAction(e->{dr.deltaSFilled=true;});
		clear.setOnAction(e->{dr.lines.clear(); dr.deltaS.clear();});
		//controls.getChildren().add(redraw);
		controls.add(clear,0,2);
		
		//set up RGB sliders
		Slider red=new Slider(0,255,0);
		Slider green=new Slider(0,255,255);
		Slider blue=new Slider(0,255,0);
		red.valueProperty().addListener(new ChangeListener<Number>(){
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// AUTO Auto-generated method stub
				dr.setRed(newValue.intValue());
			}
		});
		blue.valueProperty().addListener(new ChangeListener<Number>(){
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// AUTO Auto-generated method stub
				dr.setBlue(newValue.intValue());
			}
		});
		green.valueProperty().addListener(new ChangeListener<Number>(){
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// AUTO Auto-generated method stub
				dr.setGreen(newValue.intValue());
			}
		});
		Label redLabel=new Label("Red: ");
		Label greenLabel=new Label("Green: ");
		Label blueLabel=new Label("Blue: ");
		controls.add(redLabel,0,3);
		controls.add(red,1,3);
		controls.add(greenLabel,0,4);
		controls.add(green,1,4);
		controls.add(blueLabel,0,5);
		controls.add(blue,1,5);
		
		root.getChildren().add(controls);
		
		//updates the image every 60 ms or something
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
