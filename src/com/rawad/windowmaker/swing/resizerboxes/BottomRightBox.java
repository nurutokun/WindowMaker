package com.rawad.windowmaker.swing.resizerboxes;

public class BottomRightBox extends ResizerBox {

	public BottomRightBox(int containerWidth, int containerHeight) {
		super(containerWidth, containerHeight);
		
	}
	
	public void updatePosition() {
		
		setX(getWidth());
		setY(getHeight());
		
	}
	
}
