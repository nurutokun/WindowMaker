package com.rawad.windowmaker.swing.resizerboxes;

import com.rawad.windowmaker.swing.Cursors;

public class BottomRightBox extends ResizerBox {

	public BottomRightBox(int containerWidth, int containerHeight) {
		super(Cursors.SE_DIAGONAL, containerWidth, containerHeight, 1, 1);
		
	}
	
	public void updatePosition() {
		
		setX(getWidth());
		setY(getHeight());
		
	}
	
}
