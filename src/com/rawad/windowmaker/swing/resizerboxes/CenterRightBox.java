package com.rawad.windowmaker.swing.resizerboxes;

public class CenterRightBox extends ResizerBox {

	public CenterRightBox(int containerWidth, int containerHeight) {
		super(containerWidth, containerHeight);
		
	}
	
	public void updatePosition() {
		
		setX(getWidth());
		setY((getHeight()/2) - (BOX_HEIGHT/2));
		
	}
	
}
