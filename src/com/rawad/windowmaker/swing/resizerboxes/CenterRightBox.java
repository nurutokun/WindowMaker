package com.rawad.windowmaker.swing.resizerboxes;

import com.rawad.windowmaker.swing.Cursors;

public class CenterRightBox extends ResizerBox {

	public CenterRightBox(int containerWidth, int containerHeight) {
		super(Cursors.HORIZONTAL, containerWidth, containerHeight, 1, 0);
		
	}
	
	public void updatePosition() {
		
		setX(getWidth());
		setY((getHeight()/2) - (BOX_HEIGHT/2));
		
	}
	
}
