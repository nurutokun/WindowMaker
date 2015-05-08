package com.rawad.windowmaker.swing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SelectionBoxManager {
	
	// Could be an ArrayList
	private SelectionBox selection;
	
	private boolean creatingBox;
	
	public SelectionBoxManager() {
		
		selection = new SelectionBox(0, 0);
		
		creatingBox = false;
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
		
	public void createSelectionBox(BufferedImage picture, int x, int y, int scaleFactor) {
		
	public void createSelectionBox(BufferedImage picture, int x, int y) {
		
		selection.updateMousePosition(x, y);
		
		selection = new SelectionBox(picture, x, y, scaleFactor);
		
		selection = new SelectionBox(picture, x, y);
		
	}
	
	public void setFirstPosition(int x, int y) {
		
		if(!creatingBox) {
			this.x1 = x;
			this.y1 = y;
			
			creatingBox = true;
		} else {
			potentialWidth = Math.abs(x - x1);
			potentialHeight = Math.abs(y - y1);
			
			if(potentialWidth == 0) {
				potentialWidth = 1;
			}
			
			if(potentialHeight == 0) {
				potentialHeight = 1;
			}
			
		}
		
	}
	
	public void setLastPosition(CustomPanel drawingCanvas, int x, int y) {
		
		if(creatingBox) {
			
			if(x1 > x) {
				x2 = x1;
				x1 = x;
			} else {
				x2 = x;
			}
			
			if(y1 > y) {
				y2 = y1;
				y1 = y;
			} else {
				y2 = y;
			}
			
			creatingBox = false;
			
		}
		
		int width = Math.abs(x2-x1);
		int height = Math.abs(y2-y1);
		
		width = width == 0? 1:width;
		height = height == 0? 1:height;
		
		createSelectionBox(drawingCanvas.getSubImage(x1, y1, width, height), x1, y1, drawingCanvas.getScaleFactor());
		
		for(int i = x1; i < x1+width; i++) {
			for(int j = y1; j < y1+height; j++) {
				
				drawingCanvas.setPixel(i, j, CustomPanel.INIT_PIC_BACKGROUND.getRGB());
				
			}
		}
		
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
		
		// TODO
		// Be sure to change this if/when adding more boxes
		selection = null;
		showBox = false;
		
	}
	
	public void moveBox(int x, int y) {
		
		getLastBox().move(x, y);
			
	}
	
	public void stopDragging() {
		SelectionBox box = getLastBox();
		
		if(box != null) {
			getLastBox().setDragging(false);
		}
		
	}
	
	public boolean isCreating() {
		return creatingBox;
	}
	
}
