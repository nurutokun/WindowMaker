package com.rawad.windowmaker.swing;

import java.awt.Cursor;

public enum Cursors {
	
	DEFAULT(new Cursor(Cursor.DEFAULT_CURSOR)),
	HORIZONTAL(new Cursor(Cursor.E_RESIZE_CURSOR)),
	VERTICAL(new Cursor(Cursor.S_RESIZE_CURSOR)),
	DIAGONAL(new Cursor(Cursor.SE_RESIZE_CURSOR)),
	CROSSHAIR(new Cursor(Cursor.CROSSHAIR_CURSOR));
	
	private final Cursor type;
	
	private Cursors(Cursor type) {
		this.type = type;
	}
	
	public Cursor getCursor() {
		return type;
	}
	
}