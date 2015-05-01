package com.rawad.windowmaker.swing;

public class TopLeftBox extends ResizerBox {

	public TopLeftBox(int width, int height) {
		super(width, height);
		
	}
	
	public void updatePosition() {
		
		setX(-BOX_WIDTH);
		setY(-BOX_HEIGHT);
		
	}
	
}
