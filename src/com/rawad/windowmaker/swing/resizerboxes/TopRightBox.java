package com.rawad.windowmaker.swing.resizerboxes;

import com.rawad.windowmaker.swing.Cursors;

public class TopRightBox extends ResizerBox {

	public TopRightBox(int containerWidth, int containerHeight) {
		super(Cursors.NE_DIAGONAL, containerWidth, containerHeight, 1, -1);
		
	}
	
	public void updatePosition() {
		
		setX(getWidth());
		setY(-BOX_HEIGHT-1);
		
	}
	
}
