package com.rawad.windowmaker.swing.resizerboxes;

public class BottomLeftBox extends ResizerBox {

	public BottomLeftBox(int containerWidth, int containerHeight) {
		super(containerWidth, containerHeight);
		
	}
	
	public void updatePosition() {
		
		setX(-BOX_WIDTH-1);
		setY(getHeight());
		
	}
	
}
