package com.rawad.windowmaker.swing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SelectionBoxManager {
	
	// Could be an ArrayList
	private SelectionBox selection;
	
	private boolean showBox;
	
	public SelectionBoxManager() {
		
		selection = new SelectionBox(0, 0);
		
		showBox = false;
		
	}
	
	public SelectionBox getLastBox() {
		return selection;
	}
	
	public void render(Graphics g) {
		
//		if(showBox) {
			selection.paint(g);
//		}
		
	}
	
	public void updateBoxCreation(int x, int y) {
		
		selection.updateMousePosition(x, y);
		
	}
	
}
