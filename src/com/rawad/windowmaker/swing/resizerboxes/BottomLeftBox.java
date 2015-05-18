package com.rawad.windowmaker.swing.resizerboxes;

import com.rawad.windowmaker.swing.Cursors;

public class BottomLeftBox extends ResizerBox {

	public BottomLeftBox(int containerWidth, int containerHeight) {
		super(Cursors.SW_DIAGONAL, containerWidth, containerHeight, -1, 1);
		
	}
	
	public void updatePosition() {
		
		setX(-BOX_WIDTH-1);
		setY(getHeight());
		
	}
	
}
