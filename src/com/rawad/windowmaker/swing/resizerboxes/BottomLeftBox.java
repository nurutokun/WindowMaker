package com.rawad.windowmaker.swing.resizerboxes;

public class BottomLeftBox extends ResizerBox {

	public BottomLeftBox(int containerWidth, int containerHeight) {
		super(containerWidth, containerHeight);
		
	}
	
	public void updatePosition() {
		
<<<<<<< HEAD
		setX(-BOX_WIDTH-1);
=======
		setX(-BOX_WIDTH);
>>>>>>> branch 'myBranch' of https://github.com/nurutokun/WindowMaker
		setY(getHeight());
		
	}
	
}
