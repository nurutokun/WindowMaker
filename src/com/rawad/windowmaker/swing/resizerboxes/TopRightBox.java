package com.rawad.windowmaker.swing.resizerboxes;

public class TopRightBox extends ResizerBox {

	public TopRightBox(int containerWidth, int containerHeight) {
		super(containerWidth, containerHeight);
		
	}
	
	public void updatePosition() {
		
		setX(getWidth());
<<<<<<< HEAD
		setY(-BOX_HEIGHT-1);
=======
		setY(-BOX_HEIGHT);
>>>>>>> branch 'myBranch' of https://github.com/nurutokun/WindowMaker
		
	}
	
}
