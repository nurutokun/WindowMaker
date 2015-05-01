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
		
		private TopLeftBox tlBox;
		private TopCenterBox tcBox;
		private TopRightBox trBox;
		private CenterLeftBox clBox;
		private CenterRightBox crBox;
		private BottomLeftBox blBox;
		private BottomCenterBox bcBox;
		private BottomRightBox brBox;
		
		private int x;
		private int y;
		
		private int width;
		private int height;
		
		private int boxWidth = 6;
		private int boxHeight = 6;
		
		public SelectionBox(int x, int y, int width, int height) {
			
			tlBox = new TopLeftBox(width, height);
			tcBox = new TopCenterBox(width, height);
			trBox = new TopRightBox(width, height);
			clBox = new CenterLeftBox(width, height);
			crBox = new CenterRightBox(width, height);
			blBox = new BottomLeftBox(width, height);
			bcBox = new BottomCenterBox(width, height);
			brBox = new BottomRightBox(width, height);
			
			this.x = x;
			this.y = y;
			
			this.width = width;
			this.height = height;
			
		}
		
		public void paint(Graphics g) {
			
			updateResizeBoxPositions();
			
			tlBox.render(g, width, height);
			tcBox.render(g, width, height);
			trBox.render(g, width, height);
			clBox.render(g, width, height);
			crBox.render(g, width, height);
			blBox.render(g, width, height);
			bcBox.render(g, width, height);
			brBox.render(g, width, height);
			
		}
		
		private void updateResizeBoxPositions() {
			
			// 0	1	2
			// 3		4
			// 5	6	7
			
			tlBox.setContainerX(x);
			tlBox.setContainerY(y);
			
			tcBox.setContainerX(x);
			tcBox.setContainerY(y);
			
			trBox.setContainerX(x);
			trBox.setContainerY(y);
			
			clBox.setContainerX(x);
			clBox.setContainerY(y);
			
			crBox.setContainerX(x);
			crBox.setContainerY(y);
			
			blBox.setContainerX(x);
			blBox.setContainerY(y);
			
			bcBox.setContainerX(x);
			bcBox.setContainerY(y);
			
			brBox.setContainerX(x);
			brBox.setContainerY(y);
			
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
