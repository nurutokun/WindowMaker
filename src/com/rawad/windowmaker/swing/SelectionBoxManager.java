package com.rawad.windowmaker.swing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SelectionBoxManager {
	
	private BufferedImage currentPicture;
	
	// Could be an ArrayList
	private SelectionBox selection;
	
	private boolean showBox;
	
	public SelectionBoxManager() {
		
		selection = new SelectionBox(0, 0, 0, 0);
		
		showBox = false;
		
	}
	
	public SelectionBox getLastBox() {
		return selection;
	}
	
	public void render(Graphics g) {
		
		if(showBox) {
			selection.paint(g);
		}
		
	}
	
	private static class SelectionBox {
		
		private ResizerBox[] boxes;
		
		private int x;
		private int y;
		
		private int width;
		private int height;
		
		private int boxWidth = 6;
		private int boxHeight = 6;
		
		public SelectionBox(int x, int y, int width, int height) {
			
			boxes = new ResizerBox[8];
			
			for(int i = 0; i < boxes.length; i++) {
				boxes[i] = new ResizerBox(0, 0, boxWidth, boxHeight);
			}
			
			this.x = x;
			this.y = y;
			
			this.width = width;
			this.height = height;
			
		}
		
		public void paint(Graphics g) {
			
			updateResizeBoxPositions();
			
			for(ResizerBox b: boxes) {
				b.render(g);
			}
			
		}
		
		private void updateResizeBoxPositions() {
			
			// 0	1	2
			// 3		4
			// 5	6	7
			
			boxes[0].setX(x-boxWidth);
			boxes[0].setY(y-boxHeight);
			
			boxes[1].setX(x+(width/2)-(boxWidth/2));
			boxes[1].setY(y-boxHeight);
			
			boxes[2].setX(x+width+boxWidth);
			boxes[2].setY(y-boxHeight);
			
			boxes[3].setX(x-boxWidth);
			boxes[3].setY(y+(height/2)-(boxHeight/2));
			
			boxes[4].setX(x+width+boxWidth);
			boxes[4].setY(y+(height/2)-(boxHeight/2));
			
			boxes[5].setX(x-boxWidth);
			boxes[5].setY(y+height);
			
			boxes[6].setX(x+(width/2)-(boxWidth/2));
			boxes[6].setY(y+height);
			
			boxes[7].setX(x+width+boxHeight);
			boxes[7].setY(y+height);
			
		}
		
		public int getX() {
			return x;
		}
		
		public void setX(int x) {
			this.x = x;
		}
		
		public void setY(int y) {
			this.y = y;
		}
		
		public int getY() {
			return y;
		}
		
		public int getWidth() {
			return width;
		}
		
		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}
		
		public void setHeight(int height) {
			this.height = height;
		}
		
		public boolean intersects(int x, int y) {
			
			if(x > getX() && y > getY() && x < getX() + getWidth() && y < getY() + getHeight()) {
				return true;
			}
			
			return false;
			
		}
		
	}
	
}
