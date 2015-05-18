package com.rawad.windowmaker.swing.resizerboxes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.rawad.windowmaker.swing.Cursors;

public abstract class ResizerBox {
	
	public static final int BOX_WIDTH = 6;
	public static final int BOX_HEIGHT = 6;
	
	private final Cursors cursor;
	
	private final int width;
	private final int height;
	
	private final int xScale;
	private final int yScale;
	
	// ^^^
	// -1,-1	0,-1	1,-1
	// -1,0		--,--	1,0
	// -1,1		0,1		1,1
	
	private int x;
	private int y;
	
	private int containerX;
	private int containerY;
	
	private int containerWidth;
	private int containerHeight;
	
	private boolean dragging;
	private boolean resizing;
	
	public ResizerBox(Cursors cursor, int containerWidth, int containerHeight, int xScale, int yScale) {
		this.x = 0;
		this.y = 0;
		
		this.cursor = cursor;
		
		this.xScale = xScale;
		this.yScale = yScale;
		
		this.containerWidth = containerWidth;
		this.containerHeight = containerHeight;
		
		this.width = BOX_WIDTH;
		this.height = BOX_HEIGHT;
		
		dragging = false;
	}
	
	public void render(Graphics g, int containerWidth, int containerHeight) {
		
		setWidth(containerWidth);
		setHeight(containerHeight);
		
		updatePosition();
		
		g.setColor(Color.GRAY);
		g.drawRect(containerX + x, containerY + y, width, height);
		
		g.setColor(Color.WHITE);
		g.fillRect(containerX + x + 1, containerY + y + 1 , width-1, height-1);
		
	}
	
	public abstract void updatePosition();
	
	/**
	 * Coordinates are where the mouse has moved
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Rectangle getResizedPosition(int x, int y) {
		
		int oldX = containerX;
		int oldY = containerY;
		
		int width;
		int height;
		
		if(xScale < 0) {
			
			containerX = x;
			
		}
		
		if(yScale < 0) {
			
			containerY = y;
			
		}
		
		width = containerWidth + (xScale * ((x - oldX) - getX() - (BOX_WIDTH/2)));
		height =  containerHeight + (yScale * ((y - oldY) - getY() - (BOX_HEIGHT/2)));
		
		return new Rectangle(containerX, containerY, width, height);
		
	}
	
	public boolean intersects(int x, int y) {
		
		if(	(x > this.x + containerX && x < (this.x + containerX + width)) &&
			(y > this.y + containerY && y < (this.y + containerY + height))) {
			return true;
		}
		
		return false;
		
	}
	
	public Cursors getCursor() {
		return this.cursor;
	}
	
	public void setContainerX(int x) {
		this.containerX = x;
	}
	
	public void setContainerY(int y) {
		this.containerY = y;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getY() {
		return y;
	}
	
	public void setWidth(int width) {
		this.containerWidth = width;
	}
	
	public int getWidth() {
		return containerWidth;
	}
	
	public void setHeight(int height) {
		this.containerHeight = height;
	}
	
	public int getHeight() {
		return containerHeight;
	}
	
	public boolean isResizing() {
		return resizing;
	}
	
	public void setResizing(boolean resizing) {
		this.resizing = resizing;
	}
	
	public boolean isDragging() {
		return dragging;
	}

	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}
	
}
