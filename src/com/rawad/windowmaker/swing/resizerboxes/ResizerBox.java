package com.rawad.windowmaker.swing.resizerboxes;

import java.awt.Color;
import java.awt.Graphics;

public abstract class ResizerBox {
	
	public static final int BOX_WIDTH = 6;
	public static final int BOX_HEIGHT = 6;
	
	private final int width;
	private final int height;
	
	private int x;
	private int y;
	
	private int containerX;
	private int containerY;
	
	private int containerWidth;
	private int containerHeight;
	
	private boolean dragging;
	
	public ResizerBox(int containerWidth, int containerHeight) {
		this.x = 0;
		this.y = 0;
		
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
	
	public void updatePosition() {
		
	}
	
	public boolean intersects(int x, int y) {
		
		if(	(x > this.x + containerX && x < (this.x + containerX + width)) &&
			(y > this.y + containerY && y < (this.y + containerY + height))) {
			return true;
		}
		
		return false;
		
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

	public boolean isDragging() {
		return dragging;
	}

	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}
	
}
