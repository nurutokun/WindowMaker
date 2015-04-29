package com.rawad.windowmaker.swing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SelectionBoxManager {
	
	private BufferedImage currentPicture;
	
	
	
	// Could be an ArrayList
	private SelectionBox selection;
	
	public SelectionBoxManager() {
		
		
		
	}
	
	public void paint(Graphics g) {
		
		selection.paint(g);
		
	}
	
	private class SelectionBox {
		
		private ResizerBox[] boxes;
		
		private int x;
		private int y;
		
		private int width;
		private int height;
		
		public SelectionBox(int x, int y, int width, int height) {
			
			boxes = new ResizerBox[8];
			
			this.x = x;
			this.y = y;
			
			this.width = width;
			this.height = height;
			
		}
		
		public void paint(Graphics g) {
			
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
