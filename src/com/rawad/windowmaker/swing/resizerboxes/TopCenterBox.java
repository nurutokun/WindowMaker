package com.rawad.windowmaker.swing.resizerboxes;

public class TopCenterBox extends ResizerBox {

	public TopCenterBox(int containerWidth, int containerHeight) {
		super(containerWidth, containerHeight);
		
	}
	
	public void updatePosition() {
		
		setX((getWidth()/2) - (BOX_WIDTH/2));
<<<<<<< HEAD
		setY(-BOX_HEIGHT);
=======
<<<<<<< HEAD
		setY(-BOX_HEIGHT-1);
=======
		setY(-BOX_HEIGHT);
>>>>>>> branch 'myBranch' of https://github.com/nurutokun/WindowMaker
>>>>>>> stash
		
	}
	
}
