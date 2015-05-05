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
<<<<<<< HEAD
		private BufferedImage displayPicture;
=======
<<<<<<< HEAD
		private final BufferedImage displayPicture;
>>>>>>> stash
		
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
		
		private int width;
		private int height;
		
		private int potentialWidth;
		private int potentialHeight;
		
		private int scaleFactor;
		
		private boolean resizing;
		private boolean creating;
		
		public SelectionBox(BufferedImage originalPicture, int x, int y) {
			
			this.originalPicture = originalPicture;
			this.displayPicture = originalPicture;
			
			scaleFactor = 100;
			
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
			creating = false;
			
		}
		
		public SelectionBox(int x, int y) {
			this(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), x, y);
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
			
			if(displayPicture != null) {
				g.drawImage(displayPicture, x, y, null);
				
			} else {
				g.setColor(Color.WHITE);
				g.fillRect(x + 1, y + 1, potentialWidth - 1, potentialHeight - 1);
			}
			
			if(creating) {
				g.setColor(Color.GRAY);
				g.drawRect(x, y, potentialWidth, potentialHeight);
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
		
		public void updateMousePosition(int x, int y) {
			
			int dx = x - this.x;
			int dy = y - this.y ;
			
			System.out.printf("original: %s, %s. new: %s, %s. deltas: %s, %s\n", this.x, this.y, x, y, dx, dy);
			
			if(dx < 0) {
				
				dx = -dx;
				
			}
			
			if(dy < 0) {
				
				dy = -dy;
				
			}
			
			dx = dx == 0? 1:dx;
			dy = dy == 0? 1:dy;
			
			potentialWidth = dx;
			potentialHeight = dy;
			
			if(!creating) {
				this.x = x;
				this.y = y;
			}
			
			setCreating(true);
			
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
		
<<<<<<< HEAD
		public int getPotentialWidth() {
			return potentialWidth;
		}
		
		public void setPotentialWidth(int potentialWidth) {
			this.potentialWidth = potentialWidth;
		}
		
		public int getPotentialHeight() {
			return potentialHeight;
		}
		
		public void setPotentialHeight(int potentialHeight) {
			this.potentialHeight = potentialHeight;
		}
		
		public void setImage(BufferedImage picture) {
			this.originalPicture = picture;
			this.displayPicture = picture;
			
			setCreating(false);
			
		}
		
		public void setCreating(boolean creating) {
			this.creating = creating;
		}
		
		public boolean isCreating() {
			return creating;
=======
		public void setDragging(boolean dragging) {
			this.dragging = dragging;
=======
		private BufferedImage displayPicture;
		
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
		
		private int width;
		private int height;
		
		private int potentialWidth;
		private int potentialHeight;
		
		private int scaleFactor;
		
		private boolean resizing;
		
		public SelectionBox(BufferedImage originalPicture, int x, int y) {
			
			this.originalPicture = originalPicture;
			this.displayPicture = originalPicture;
			
			scaleFactor = 100;
			
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
		
		public void handleMouseMove(int x1, int y1, int x2, int y2) {
			
		}
		
		public void handleMousePress(int x, int y) {
			
		}
		
		public void handleMouseRelease(int x, int y) {
			
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
>>>>>>> branch 'myBranch' of https://github.com/nurutokun/WindowMaker
>>>>>>> stash
		}
		
		public boolean intersects(int x, int y) {
			
			if(	x > getX() && x < getX() + getWidth() &&
				y > getY() && y < getY() + getHeight()) {
				return true;
			}
			
			return false;
			
		}
		
	}
