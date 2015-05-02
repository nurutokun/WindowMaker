package com.rawad.windowmaker.swing.resizerboxes;

public class TopRightBox extends ResizerBox {

	public TopRightBox(int containerWidth, int containerHeight) {
		super(containerWidth, containerHeight);
		
	}
	
	public void updatePosition() {
		
		setX(getWidth());
		setY(-BOX_HEIGHT);
		
	}
	
}
