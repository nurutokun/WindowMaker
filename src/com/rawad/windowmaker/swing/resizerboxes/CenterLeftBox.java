package com.rawad.windowmaker.swing.resizerboxes;

import com.rawad.windowmaker.swing.Cursors;

public class CenterLeftBox extends ResizerBox {

	public CenterLeftBox(int containerWidth, int containerHeight) {
		super(Cursors.HORIZONTAL, containerWidth, containerHeight, -1, 0);
		
	}
	
	public void updatePosition() {
		
		setX(-BOX_WIDTH-1);
		setY((getHeight()/2) - (BOX_HEIGHT/2));
		
	}
	
}
