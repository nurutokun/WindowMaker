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
import com.rawad.windowmaker.swing.resizerboxes.ResizerBox;
import com.rawad.windowmaker.swing.resizerboxes.TopCenterBox;
import com.rawad.windowmaker.swing.resizerboxes.TopLeftBox;
import com.rawad.windowmaker.swing.resizerboxes.TopRightBox;

public class SelectionBox {
		
		private BufferedImage originalPicture;
		private BufferedImage displayPicture;
		
		private TopLeftBox tlBox;
		private TopCenterBox tcBox;
		private TopRightBox trBox;
		private CenterLeftBox clBox;
		private CenterRightBox crBox;
		private BottomLeftBox blBox;
		private BottomCenterBox bcBox;
		private BottomRightBox brBox;
		
		private ResizerBox[] boxes;
		
		private Cursor cursor;
		
		private int x;
		private int y;
		
		private int prevX;
		private int prevY;
		
		private int width;
		private int height;
		
		private int potentialWidth;
		private int potentialHeight;
		
		private int scaleFactor;
		
		private boolean resizing;
		private boolean creating;
		private boolean created;
		private boolean moved;
		
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
			
			boxes = new ResizerBox[]{tlBox, tcBox, trBox, clBox, crBox, clBox, blBox, bcBox, brBox};
			
			this.x = x;
			this.y = y;
			
			cursor = Cursors.DEFAULT.getCursor();
			
			resizing = false;
			creating = false;
			created = false;
			moved = false;
			
		}
		
		public SelectionBox(int x, int y) {
			this(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), x, y);
		}
		
		public void paint(Graphics g) {
			
			updateResizeBoxPositions();
			
			if(created) {
				
				for(ResizerBox box: boxes) {
					box.render(g, width, height);
				}
				
			}
			
			if(displayPicture != null && created) {
				
				g.drawImage(displayPicture, x, y, null);
				
				g.setColor(Color.GRAY);
				g.drawRect(x - 1, y - 1, width + 1, height + 1);
				
			} else {
//				g.setColor(Color.WHITE);
//				g.fillRect(x + 1, y + 1, potentialWidth - 1, potentialHeight - 1);
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
			
			for(ResizerBox box: boxes) {
				
				box.setContainerX(x);
				box.setContainerY(y);
				
			}
			
			
		}
		
		public void initCreation(int x, int y) {
			
			this.x = x;
			this.y = y;
			
			prevX = x;
			prevY = y;
			
			width = potentialWidth = 0;
			height = potentialHeight = 0;
			
			creating = true;
			created = false;
			
		}
		
		public void updateMousePosition(int x, int y) {
			
			int dx = x - prevX;
			int dy = y - prevY;
			
			if(dx < 0) {
				
				dx = -dx;
				
				this.x = x;
				
			}
			
			if(dy < 0) {
				
				dy = -dy;
				
				this.y = y;
				
			}
			
			dx = dx == 0? 1:dx;
			dy = dy == 0? 1:dy;
			
			potentialWidth = dx;
			potentialHeight = dy;
			
		}
		
		public void finalizeCreation(CustomPanel drawingCanvas, int x, int y) {
			
			if(creating && prevX != x && prevY != y) {
				
//				potentialWidth = potentialWidth == 0? 1:potentialWidth;
//				potentialHeight = potentialHeight == 0? 1:potentialHeight;
				
				originalPicture = drawingCanvas.getSubimage(this.x, this.y, potentialWidth, potentialHeight);
				displayPicture = originalPicture;
				
				width = potentialWidth;
				height = potentialHeight;
				
				created = true;
				moved = false;
			}
			
			creating = false;
			
		}
		
		public void handleMouse(CustomPanel drawingCanvas, int x, int y) {
			
			
			
		}
		
		public void handleHover(int x, int y) {
			
			// Makes it so that mouse-hovering of the main resizer boxes works fine; might change
			
			for(ResizerBox box: boxes) {
				
				if(box.intersects(x, y)) {
					Cursors temp = box.getCursor();
					
					cursor = temp.getCursor();
					
					return;
				}
				
			}
			
			cursor = null;
			
		}
		
		public void move(CustomPanel drawingCanvas, int x, int y) {
			
			if(!moved) {
				drawingCanvas.fillRectangle(this.x, this.y, potentialWidth, potentialHeight, CustomPanel.INIT_PIC_BACKGROUND.getRGB());
			}
			
			//TODO: this sucks, make it better
			setX(x-(width/2));
			setY(y-(height/2));
			
			moved = true;
			
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
		
		public boolean isResizing() {
			return resizing;
		}
		
		public boolean isCreating() {
			return creating;
		}
		
		public boolean isCreated() {
			return created;
		}
		
		public boolean intersects(int x, int y) {
			
			if(	x > getX() - ResizerBox.BOX_WIDTH && x < getX() + getWidth() + ResizerBox.BOX_WIDTH &&
				y > getY() - ResizerBox.BOX_HEIGHT && y < getY() + getHeight() + ResizerBox.BOX_HEIGHT) {
				return true;
			}
			
			return false;
			
		}
		
	}
