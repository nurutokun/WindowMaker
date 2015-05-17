package com.rawad.windowmaker.swing.resizerboxes;

import com.rawad.windowmaker.swing.Cursors;

public class TopLeftBox extends ResizerBox {

	public TopLeftBox(int containerWidth, int containerHeight) {
		super(Cursors.NW_DIAGONAL, containerWidth, containerHeight);
		
	}
	
	public void updatePosition() {
		
		setX(-BOX_WIDTH-1);
		setY(-BOX_HEIGHT-1);
		
	}
	
}
