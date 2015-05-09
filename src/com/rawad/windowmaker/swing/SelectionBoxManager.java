package com.rawad.windowmaker.swing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SelectionBoxManager {
	
	// Could be an ArrayList
	private SelectionBox selection;
	
	private boolean movingBox;
	
	public SelectionBoxManager() {
		
		selection = new SelectionBox(0, 0);
		
		movingBox = false;
	}
	
	public SelectionBox getLastBox() {
		return selection;
	}
	
	public void render(Graphics g) {
		
		SelectionBox box = getLastBox();
		
		box.paint(g);
		
	}
	
	public void handleMouse(CustomPanel drawingCanvas, int x, int y) {
		
		SelectionBox box = getLastBox();
		
		if(movingBox) {
			box.move(x, y);
			
		} else if(box.isCreating()) {
			
			box.updateMousePosition(x, y);
			
		} else if(box.intersects(x, y)) {
			
			movingBox = true;
			
		} else {
			
			if(box.isCreated()) {
				copySelectionOntoCanvas(drawingCanvas);
				
			}
			
			box.initCreation(x, y);// This stays for when we copy onto original image to make the box disappear, since the x/y are the same
			
		}
		
	}
	
	public void handleMouseRelease(CustomPanel drawingCanvas, int x, int y) {
		
		SelectionBox box = getLastBox();
		
		box.finalizeCreation(drawingCanvas, x, y);
		
		box.handleHover(x, y);
		
		movingBox = false;
	}
	
	public void copySelectionOntoCanvas(CustomPanel drawingCanvas) {
		
		SelectionBox box = getLastBox();
		BufferedImage temp = box.getImage();
		
		for(int i = box.getX(); i < box.getX() + box.getHeight(); i++) {
			for(int j = box.getY(); j < box.getY() + box.getHeight(); j++) {
				
				try {
					drawingCanvas.setPixel(i, j, temp.getRGB(i - box.getX(), j - box.getY()));
				} catch(ArrayIndexOutOfBoundsException ex) {
					System.out.println((i-box.getX()) + ", " + (j-box.getY()) + " is out of bounds");
				}
				
			}
		}
		
	}
	
}
