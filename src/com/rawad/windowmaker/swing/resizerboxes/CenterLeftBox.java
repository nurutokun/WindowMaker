package com.rawad.windowmaker.swing.resizerboxes;

public class CenterLeftBox extends ResizerBox {

	public CenterLeftBox(int containerWidth, int containerHeight) {
		super(containerWidth, containerHeight);
		
	}
	
	public void updatePosition() {
		
		setX(-BOX_WIDTH-1);
		setY((getHeight()/2) - (BOX_HEIGHT/2));
		
	}
	
}
