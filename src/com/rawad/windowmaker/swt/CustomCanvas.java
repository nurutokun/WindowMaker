package com.rawad.windowmaker.swt;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class CustomCanvas extends Canvas {
	
	private Image picture;
	
	public CustomCanvas(Composite parent, int style) {
		super(parent, style);
		
		picture = new Image(this.getDisplay(), "res/test.png");
		
	}
	
	public void render(GC g) {
		
		ImageData data = picture.getImageData();
		
		g.drawImage(picture, getClientArea().width/2 - (data.width/2), getClientArea().height/2 - (data.height/2));
		
	}
	
}
