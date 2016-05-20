package edu.mbhs.graphics.aepr.util;

public class PipelineController {
	FrameStream[] pipeline;
	
	public PipelineController(FrameStream... steps){
		pipeline=steps;
		for(int i=1;i<pipeline.length;i++){
			
		}
	}
}
