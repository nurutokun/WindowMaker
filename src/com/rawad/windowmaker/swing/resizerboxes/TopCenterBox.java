package com.rawad.windowmaker.swing.resizerboxes;

public class TopCenterBox extends ResizerBox {

	public TopCenterBox(int containerWidth, int containerHeight) {
		super(containerWidth, containerHeight);
		
	}
	
	public void updatePosition() {
		
		setX((getWidth()/2) - (BOX_WIDTH/2));
		setY(-BOX_HEIGHT-1);
		
	}
	
}
