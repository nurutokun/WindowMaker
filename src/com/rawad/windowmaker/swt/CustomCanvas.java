package com.rawad.windowmaker.swt;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class CustomCanvas extends Canvas {
	
	private Image picture;
	
	private AudioInputStream audioStream;
	
	private Clip sound;
	
	public CustomCanvas(Composite parent, int style) {
		super(parent, style);
		
		picture = new Image(getDisplay(), "res/test.png");
		
		try {
			audioStream = AudioSystem.getAudioInputStream(new File("res/test.wav"));
			
			sound = AudioSystem.getClip();
			
			sound.open(audioStream);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void render(GC g) {
		
		ImageData data = picture.getImageData();
		
		g.drawImage(picture, getClientArea().width/2 - (data.width/2), getClientArea().height/2 - (data.height/2));
		
	}
	
	public void mouseDown(MouseEvent e) {
		
		System.out.println("playing sound...");
		
		if(sound.isRunning()) {
			sound.stop();
			sound.setFramePosition(0);
		}
		
		sound.start();
		
	}
	
}
