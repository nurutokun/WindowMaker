package com.rawad.windowmaker.swing.resizerboxes;

public class CenterLeftBox extends ResizerBox {

	public CenterLeftBox(int containerWidth, int containerHeight) {
		super(containerWidth, containerHeight);
		
	}
	
	public void updatePosition() {
		
<<<<<<< HEAD
		setX(-BOX_WIDTH);
=======
<<<<<<< HEAD
		setX(-BOX_WIDTH-1);
=======
		setX(-BOX_WIDTH);
>>>>>>> branch 'myBranch' of https://github.com/nurutokun/WindowMaker
>>>>>>> stash
		setY((getHeight()/2) - (BOX_HEIGHT/2));
		
	}
	
}
