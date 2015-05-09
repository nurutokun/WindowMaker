package com.rawad.windowmaker.swing;

import java.awt.Cursor;

public enum Cursors {
	
	DEFAULT(new Cursor(Cursor.DEFAULT_CURSOR)),
	HORIZONTAL(new Cursor(Cursor.E_RESIZE_CURSOR)),
	VERTICAL(new Cursor(Cursor.S_RESIZE_CURSOR)),
	NE_DIAGONAL(new Cursor(Cursor.NE_RESIZE_CURSOR)),
	NW_DIAGONAL(new Cursor(Cursor.NW_RESIZE_CURSOR)),
	SE_DIAGONAL(new Cursor(Cursor.SE_RESIZE_CURSOR)),
	SW_DIAGONAL(new Cursor(Cursor.SW_RESIZE_CURSOR)),
	MOVE(new Cursor(Cursor.MOVE_CURSOR));
	
	private final Cursor type;
	
	private Cursors(Cursor type) {
		this.type = type;
	}
	
	public Cursor getCursor() {
		return type;
	}
	
}