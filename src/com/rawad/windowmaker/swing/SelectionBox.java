package com.rawad.windowmaker.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.rawad.windowmaker.swing.resizerboxes.BottomCenterBox;
import com.rawad.windowmaker.swing.resizerboxes.BottomLeftBox;
import com.rawad.windowmaker.swing.resizerboxes.BottomRightBox;
import com.rawad.windowmaker.swing.resizerboxes.CenterLeftBox;
import com.rawad.windowmaker.swing.resizerboxes.CenterRightBox;
import com.rawad.windowmaker.swing.resizerboxes.TopCenterBox;
import com.rawad.windowmaker.swing.resizerboxes.TopLeftBox;
import com.rawad.windowmaker.swing.resizerboxes.TopRightBox;

public class SelectionBox {
		
		private BufferedImage originalPicture;
		private final BufferedImage displayPicture;
		
		private TopLeftBox tlBox;
		private TopCenterBox tcBox;
		private TopRightBox trBox;
		private CenterLeftBox clBox;
		private CenterRightBox crBox;
		private BottomLeftBox blBox;
		private BottomCenterBox bcBox;
		private BottomRightBox brBox;
		
		private Cursor cursor;
		
		private int x;
		private int y;
		
		private int originX;
		private int originY;
		
		private int dx;
		private int dy;
		
		private int width;
		private int height;
		
		private int potentialWidth;
		private int potentialHeight;
		
		private int scaleFactor;
		
		private boolean resizing;
		private boolean dragging;
		
		public SelectionBox(BufferedImage originalPicture, int x, int y, int scaleFactor) {
			
			this.originalPicture = originalPicture;
			this.displayPicture = originalPicture;
			
			this.scaleFactor = scaleFactor;
			
			this.width = originalPicture.getWidth();
			this.height = originalPicture.getHeight();
			
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
			
			cursor = Cursors.DEFAULT.getCursor();
			
			resizing = false;
			dragging = false;
			
		}
		
		public void paint(Graphics g) {
			
			updateResizeBoxPositions();
			
			g.setColor(Color.GRAY);
			g.drawRect(x-1, y-1, width+1, height+1);
			
			tlBox.render(g, width, height);
			tcBox.render(g, width, height);
			trBox.render(g, width, height);
			clBox.render(g, width, height);
			crBox.render(g, width, height);
			blBox.render(g, width, height);
			bcBox.render(g, width, height);
			brBox.render(g, width, height);
			
			if(displayPicture != null) {
				g.drawImage(displayPicture, x, y, null);
			}
			
			if(resizing) {
				g.setColor(Color.GRAY);
				g.drawRect(x, y, potentialWidth, potentialHeight);
				
			}
			
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
		
		public Cursor getCursor(int x, int y) {
			
			Cursors re = Cursors.DEFAULT;
			
			if(tlBox.intersects(x, y)) {
				re = Cursors.DIAGONAL_SE;
				
			} else if(tcBox.intersects(x, y)) {
				re = Cursors.VERTICAL;
				
			} else if(trBox.intersects(x, y)) {
				re = Cursors.DIAGONAL_SW;
				
			} else if(clBox.intersects(x, y)) {
				re = Cursors.HORIZONTAL;
				
			} else if(crBox.intersects(x, y)) {
				re = Cursors.HORIZONTAL;
				
			} else if(blBox.intersects(x, y)) {
				re = Cursors.DIAGONAL_SW;
				
			} else if(bcBox.intersects(x, y)) {
				re = Cursors.VERTICAL;
				
			} else if(brBox.intersects(x, y)) {
				re = Cursors.DIAGONAL_SE;
				
			} else if(intersects(x, y)) {
				re = Cursors.CROSSHAIR;
				
			}
			
			return re.getCursor();
			
		}
		
		public void handleMousePress(int x, int y) {
			
		}
		
		public void handleMouseRelease(int x, int y) {
			
		}
		
		/**
		 * Uses coordinates on screen
		 * 
		 * @param x
		 * @param y
		 */
		public void move(int x, int y) {
			
			if(!dragging) {
				
				originX = x;
				originY = y;
				
				dx = originX - getX();
				dy = originY - getY();
				
				dragging = true;
				
			} else {
				
				setX(x - dx);
				setY(y - dy);
				
//				dragging = false;
				
			}
			
		}
		
		public BufferedImage getImage() {
			return displayPicture;
		}
		
		public Cursor getCursor() {
			return cursor;
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
		
		public void setDragging(boolean dragging) {
			this.dragging = dragging;
		}
		
		public boolean intersects(int x, int y) {
			
			if(	x > getX() && x < getX() + getWidth() &&
				y > getY() && y < getY() + getHeight()) {
				return true;
			}
			
			return false;
			
		}
		
	}