package com.rawad.windowmaker.swing.resizerboxes;

public class BottomCenterBox extends ResizerBox {

	public BottomCenterBox(int containerWidth, int containerHeight) {
		super(containerWidth, containerHeight);
		
	}
	
	public void updatePosition() {
		
		setX((getWidth()/2) - (BOX_WIDTH/2));
		setY(getHeight());
		
	}
	
}
