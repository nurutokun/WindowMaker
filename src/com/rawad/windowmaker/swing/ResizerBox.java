package com.rawad.windowmaker.swing;

import java.awt.Color;
import java.awt.Graphics;

public class ResizerBox {
	
	private int x;
	private int y;
	
	private int width;
	private int height;
	
	private boolean dragging;
	
	public ResizerBox(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
		dragging = false;
	}
	
	public void render(Graphics g) {
		
		g.setColor(Color.GRAY);
		g.drawRect(x, y, width, height);
		
		g.setColor(Color.WHITE);
		g.fillRect(x+1, y+1 , width-1, height-1);
		
	}
	
	public boolean intersects(int x, int y) {
		
		if(	(x > this.x && x < (this.x + width)) &&
			(y > this.y && y < (this.y + height))) {
			return true;
		}
		
		return false;
		
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isDragging() {
		return dragging;
	}

	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}
	
}