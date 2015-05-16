package com.rawad.windowmaker.swing.resizerboxes;

public class TopLeftBox extends ResizerBox {

	public TopLeftBox(int width, int height) {
		super(width, height);
		
	}
	
	public void updatePosition() {
		
		setX(-BOX_WIDTH-1);
		setY(-BOX_HEIGHT-1);
		
	}
	
}
