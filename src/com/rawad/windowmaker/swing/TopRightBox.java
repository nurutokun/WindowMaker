package com.rawad.windowmaker.swing;

public class TopRightBox extends ResizerBox {

	public TopRightBox(int containerWidth, int containerHeight) {
		super(containerWidth, containerHeight);
		
	}
	
	public void updatePosition() {
		
		setX(getWidth() + BOX_WIDTH);
		setY(-BOX_HEIGHT);
		
	}
	
}
