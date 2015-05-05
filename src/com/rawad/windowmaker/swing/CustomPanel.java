package com.rawad.windowmaker.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.rawad.windowmaker.swing.resizerboxes.BottomCenterBox;
import com.rawad.windowmaker.swing.resizerboxes.BottomRightBox;
import com.rawad.windowmaker.swing.resizerboxes.CenterRightBox;

public class CustomPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
	
	public static final int MAX_PEN_WIDTH = 100;
	public static final int MAX_PEN_HEIGHT = 100;
	
	private static final Color INIT_PIC_BACKGROUND = new Color(0xFFFFFFFF);
	
	/**
	 * Big, Long, number
	 */
	private static final long serialVersionUID = 6101949493654211445L;
	
	private static MakeWindow window;
	
	private BufferedImage originalPicture;
	private BufferedImage displayPicture;
	
	private ChangeManager changeManager;
	private SelectionBoxManager selectionManager;
	
	private Color penColor;
	
	private Shape penShape;
	
	private Cursor cursor;
	
	private String filePath;
	
	private int x1;
	private int y1;
	
	private int x2;
	private int y2;
	
	private int penWidth;
	private int penHeight;
	
	private int viewWidth = 400;
	private int viewHeight = 300;
	
	private CenterRightBox rightBox;
	private BottomCenterBox bottomBox;
	private BottomRightBox cornerBox;
	
	private int boxWidth = 6;
	private int boxHeight = 6;
	
	private int potentialWidth;
	private int potentialHeight;
	
	private int scaleFactor;
	
	private boolean dragging;
	private boolean resizing;
	private boolean edited;
	
	public CustomPanel() {
		super();
		
		penColor = new Color(0, 0, 0);
		
		scaleFactor = 100;
		
		dragging = false;
		resizing = false;
		edited = false;
		
		window = MakeWindow.instance();
		
		changeManager = new ChangeManager();
		selectionManager = new SelectionBoxManager();
		
		penWidth = 20;
		penHeight = 20;
		
		penShape = Shape.RECTANGLE;
		
		filePath = "res/test.png";
		
		initPicture();
		
//		mainView = new SelectionBox(originalPicture, 0, 0);
		
		viewWidth = displayPicture.getWidth();
		viewHeight = displayPicture.getHeight();
		
		potentialWidth = viewWidth;
		potentialHeight = viewHeight;
		
		rightBox = new CenterRightBox(viewWidth, viewHeight);
		bottomBox = new BottomCenterBox(viewWidth, viewHeight);
		cornerBox = new BottomRightBox(viewWidth, viewHeight);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(displayPicture != null) {
			drawDisplayPicture(g);
			g.drawImage(displayPicture, 0, 0, null);
		} else {
			g.drawString("Put A Pic In Me", getWidth()/2 - (50), getHeight()/2);
		}
		
		if(resizing) {
			g.setColor(Color.GRAY);
			g.drawRect(0, 0, potentialWidth, potentialHeight);
		}
		
		rightBox.render(g, viewWidth, viewHeight);
		bottomBox.render(g, viewWidth, viewHeight);
		cornerBox.render(g, viewWidth, viewHeight);
		
		selectionManager.render(g);
		
		g.dispose();
	}
	
	/**
	 * Draws only the visible part of the image for optimization purposes
	 * 
	 * @param g {@code Graphics Object} used to draw directly onto the {@code JPanel}
	 */
	private void drawDisplayPicture(Graphics g) {
		
		int x = window.getHorizontalScroll();
		int y = window.getVerticalScroll();
		
		int maxWidth = window.getViewPortWidth() > viewWidth? viewWidth:window.getViewPortWidth();
		int maxHeight = window.getViewPortHeight() > viewHeight? viewHeight:window.getViewPortHeight();
		
		for(int i = y; i < maxHeight; i++) {
			for(int j = x; j < maxWidth; j++) {
				
				g.setColor(new Color(displayPicture.getRGB(j, i)));
				g.fillRect(j, i, displayPicture.getWidth()/scaleFactor, displayPicture.getHeight()/scaleFactor);
				
			}
		}
		
	}
	
	public void saveImage() {
		
		try {
			if(!filePath.equals("") && isEdited()) {
				
				ImageIO.write(originalPicture, filePath.substring(filePath.length()-3), new File(filePath));
				
				edited = false;
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	private void initPicture() {
		
		try {
			displayPicture = ImageIO.read(new File("res/test.png"));
			
			/*
			for(int i = 0; i < displayPicture.getWidth(); i++) {
				for(int j = 0; j < displayPicture.getHeight(); j++) {
					
					if(i%2 == 0 && j%2 == 0) {
						displayPicture.setRGB(i, j, Color.BLUE.getRGB());
					}
					
				}
			}
			/**/
			
			setTotalPreferredSize(displayPicture.getWidth(), displayPicture.getHeight());
			
		} catch(IOException ex) {
			
			displayPicture = new BufferedImage(viewWidth, viewHeight, BufferedImage.TYPE_INT_ARGB);
			
			setTotalPreferredSize(viewWidth, viewHeight);
			
			for(int i = 0; i < displayPicture.getHeight(); i++) {
				for(int j = 0; j < displayPicture.getWidth(); j++) {
					displayPicture.setRGB(j, i, INIT_PIC_BACKGROUND.getRGB());
				}
			}
			
		} finally {
			originalPicture = displayPicture;
		}
		
	}
	
	private void update(long timePassed) {
		
//		System.out.println("potential(width, height): " + potentialWidth + ", " + potentialHeight +
//				" picture(width, height): " + displayPicture.getWidth() + ", " + displayPicture.getHeight());
		
		handleMouse();
		
		if(dragging) {
			
			edited = true;
		}
		
		if(penShape == Shape.SELECT) {
			
//			selectionManager.c
			
		}
		
		if(rightBox.isDragging() || bottomBox.isDragging() || cornerBox.isDragging()) {
			
			int dx = x2 - x1;
			int dy = y2 - y1;
			
			if(viewWidth + dx > 0 && viewHeight + dy > 0) {
				if(rightBox.isDragging()) {
					
					potentialWidth += (dx);
					
					cursor = Cursors.HORIZONTAL.getCursor();
					
					resizing = true;
					
				} else if(bottomBox.isDragging()) {
					
					potentialHeight += (dy);
					
					cursor = Cursors.VERTICAL.getCursor();
					
					resizing = true;
					
				} else if(cornerBox.isDragging()) {
					
					potentialWidth += (dx);
					potentialHeight += (dy);
					
					cursor = Cursors.DIAGONAL.getCursor();
					
				}
				
				resizing = true;
				
			}
			
			
		}
		
		x1 = x2;
		y1 = y2;
		
		setCursor(cursor);
		
		window.setInfo(x2*100/scaleFactor, y2*100/scaleFactor, potentialWidth*100/scaleFactor, potentialHeight*100/scaleFactor);
		window.repaint();
	}
	
	private void handleMouse() {
		
		PenType type = penShape.getPenType();
		
		switch(type) {
		
		case DRAW:
			if(dragging) {
				renderPenStrokes();
			}
			break;
		
		case TWO_COORDINATES:
			handleTwoCoordinateStroke();
			break;
			
		default:
			break;
		
		}
		
	}
	
	private void handleTwoCoordinateStroke() {
		
		switch(penShape) {
		
		case SELECT:
			if(dragging) {
				selectionManager.updateBoxCreation(x1, y1);
			}
			break;
		
		case LINE:
			
			break;
		
		default:
			break;
		
		}
		
	}
	
	private void renderPenStrokes() {
		
		bresenhamLineAlgorithm(x1, y1, x2, y2);// Draws a single stroke from x1,y1 to x2,y2
		
	}
	
	private void bresenhamLineAlgorithm(int x1, int y1, int x2, int y2) {
		
		boolean xy_swap = false;
		
		//slope is outside of range [-1, 1] thus swap x and y
		if(abs(y2 - y1) > abs(x2 - x1)) {
			xy_swap = true;
			
			int temp = x1;
			
			x1 = y1;
			y1 = temp;
			
			temp = x2;
			
			x2 = y2;
			y2 = temp;
			
		}
		
		//if line goes from right to left, swap coordinates
		if(x2 - x1 < 0) {
			int temp = x1;
			
			x1 = x2;
			x2 = temp;
			
			temp = y1;
			
			y1 = y2;
			y2 = temp;
			
		}
		
		int x;
		int y = y1;
		int e = 0;
		int m_num = y2 - y1;
		int m_denom = x2 - x1;
		int threshold = m_denom/2;
		
		for(x = x1; x < x2; x++) {
			
			if(xy_swap) {
				drawPenStroke(y, x);
			} else {
				drawPenStroke(x, y);
			}
			
			e += m_num;
			
			if(m_num < 0) {
				if(e < -threshold) {
					e += m_denom;
					y--;
				}
			} else if(e > threshold) {
				e -= m_denom;
				y++;
			}
			
			if(xy_swap) {
				drawPenStroke(y, x);
			} else {
				drawPenStroke(x, y);
			}
			
		}
		
		if(x1 == x2 && y1 == y2) {
			drawPenStroke(x, y);
		}
		
	}
	
	private void drawPenStroke(int x, int y) {
		
		int scaledStrokeWidth = (penWidth*scaleFactor)/100/4;// Take quarter of original size for some reason
		int scaledStrokeHeight = (penHeight*scaleFactor)/100/4;// Can't divide by 400 all at once either...
		
		scaledStrokeWidth = scaledStrokeWidth == 0? 1:scaledStrokeWidth;
		scaledStrokeHeight = scaledStrokeHeight == 0? 1:scaledStrokeHeight;
		
		x -= (scaledStrokeWidth/2);
		y -= (scaledStrokeHeight/2);
		
		switch(penShape) {
		
		case RECTANGLE:
			drawRectangle(x, y, scaledStrokeWidth, scaledStrokeHeight);
			break;
			
		case CIRCLE:
			drawCircle(x, y, scaledStrokeWidth, scaledStrokeHeight);
			break;
			
		case TRIANGLE:
			drawTriangle(x, y, scaledStrokeWidth, scaledStrokeHeight);
			break;
			
		default:
//			g.setColor(Color.RED);
//			g.drawString("no penType error; this shouldn't happen. It is actaully impressive you made this happen. Bravo to you.", x, y);
			break;
			
		}
		
	}
	
	private void drawRectangle(int x, int y, int strokeWidth, int strokeHeight) {
		
//		System.out.printf("x,y: %s,%s | width, height: %s,%s\n", x, y, strokeWidth, strokeHeight);
		
		int width = strokeWidth + x;
		int height = strokeHeight + y;
		
		for(int i = x; i < width; i++) {
			for(int j = y; j < height; j++) {
				
				if(	i < 0 || j < 0) {
					continue;
				}
				
				try {
					
					setPixel(i, j, getPenRGB());
					
				} catch(ArrayIndexOutOfBoundsException ex) {
					break;
				}
				
			}
		}
		
	}
	
	private void drawCircle(int x, int y, int strokeWidth, int strokeHeight) {
		
		int radius = strokeWidth + strokeHeight /2;
		
		for(int i = radius; i >= 1; i--) {
			
			int radiusError = 1-i;
			
			int outerX = i;
			int outerY = 0;
			
			while(outerY <= outerX) {
				
				setPixel(outerX + x, outerY + y, getPenRGB());
				setPixel(outerY + x, outerX + y, getPenRGB());
				setPixel(-outerX + x, outerY + y, getPenRGB());
				setPixel(-outerY + x, outerX + y, getPenRGB());
				setPixel(-outerX + x, -outerY + y, getPenRGB());
				setPixel(-outerY + x, -outerX + y, getPenRGB());
				setPixel(outerX + x, -outerY + y, getPenRGB());
				setPixel(outerY + x, -outerX + y, getPenRGB());
				
				outerY++;
				
				if(radiusError < 0) {
					radiusError += 2 * outerY + 1;
				} else {
					outerX --;
					radiusError += 2 * (outerY - outerX) + 1;
				}
				
			}
		}
		
	}
	
	private void drawTriangle(int x, int y, int strokeWidth, int strokeHeight) {
		
		//	0,0				strokeWidth,0
		//	0,strokeHeight	strokeWidth,strokeHeight
		
		double invslope1 = (-strokeWidth/2d) / (strokeHeight);
		double invslope2 = (strokeWidth/2d) / strokeHeight;
		
		double curX1 = (strokeWidth)/2d;
		double curX2 = (strokeWidth)/2d;
		
		for(int scanLineY = y; scanLineY <= y+strokeHeight; scanLineY ++) {
			
			for(double i = curX1; i < curX2; i++) {
				setPixel((int) i+x, scanLineY, getPenRGB());
			}
			
			curX1 += invslope1;
			curX2 += invslope2;
			
		}
		
	}
	
	/**
	 * Snaps pixel to grid of the resized version and draws on original picture. {@code color} variable can be used to
	 * soften edges and stuff
	 * 
	 * @param x X-coordinate on the scaled image
	 * @param y Y-coordinate on the scaled image
	 * @param color Sets the pixel to this color
	 */
	public void setPixel(int x, int y, int color) {
		
		int scaledX = (x * 100)/scaleFactor;
		int scaledY = (y * 100)/scaleFactor;
		
		int prevColor;
		
		try {
			prevColor = originalPicture.getRGB(scaledX, scaledY);
		} catch(ArrayIndexOutOfBoundsException ex) {
			return;
		}
		
		if(prevColor != color) {
			try {
				
				originalPicture.setRGB(scaledX, scaledY, color);
				
				changeManager.changePixel(x, y, prevColor);
					
//				System.out.printf("setting pixel: %s, %s from: %s to color: %s\n", scaledX, scaledY,
//					Integer.toHexString(prevColor), Integer.toHexString(color));
			} catch(Exception ex) {}
			
			double pixelLength = scaleFactor/100d;
			
			pixelLength = Math.round(pixelLength) == 0? 1:pixelLength;
			
			x = (int) (Math.floor((double)(x)/pixelLength));
			y = (int) (Math.floor((double)(y)/pixelLength));
			
			x *= pixelLength;
			y *= pixelLength;
			
//			System.out.printf("pixelLength: %s. x,y: %s, %s\n", pixelLength, x, y);
			
			for(int i = x; i < pixelLength + x; i++) {
				for(int j = y; j < pixelLength + y; j++) {
					
					try {
						displayPicture.setRGB(i, j, color);
						
					} catch(Exception ex) {
						return;
					}
					
				}
			}
		}
		
	}
	
	public void undo() {
		changeManager.undoChange(this);
		rescaleImage(scaleFactor);
	}
	
	public void redo() {
		changeManager.redoChange(this);
		rescaleImage(scaleFactor);
	}
	
	private int abs(int a) {
		
		return Math.abs(a);
		
	}
	
	public void setNewImageDimensions(int width, int height) {
		
		//TODO: Copy over current image data and add new empty pixels
		
		width = width * 100/scaleFactor;
		height = height * 100/scaleFactor;
		
		if(width <= 0) {
			width = 1;
		}
		
		if(height <= 0) {
			height = 1;
		}
		
		if(width < originalPicture.getWidth()) {
			
			System.out.println("width less than actual");
			
			for(int i = width; i < originalPicture.getWidth(); i++) {
				for(int j = 0; j < originalPicture.getHeight(); j++) {
					
					changeManager.changePixel(i*scaleFactor/100, j*scaleFactor/100, originalPicture.getRGB(i, j));
				}
			}
			
		}
		
		if(height < originalPicture.getHeight()) {
			
			for(int i = height; i < originalPicture.getHeight(); i++) {
				for(int j = 0; j < originalPicture.getWidth(); j++) {
					
					changeManager.changePixel(j*scaleFactor/100, i*scaleFactor/100, originalPicture.getRGB(j, i));
					
				}
			}
			
		}
		
		changeManager.changeDimensions(originalPicture.getWidth()*scaleFactor/100, originalPicture.getHeight()*scaleFactor/100);
		
		BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				
				try {
					temp.setRGB(x, y, originalPicture.getRGB(x, y));
				} catch(Exception ex) {
					temp.setRGB(x, y, INIT_PIC_BACKGROUND.getRGB());
				}
			}
		}
		
		originalPicture = displayPicture = temp;
		
		setTotalPreferredSize(width, height);
		
	}
	
	/**
	 * Accounts for boxWidth, boxHeight and a small margin of 1 pixel, as well.
	 * 
	 * @param width
	 * @param height
	 */
	private void setTotalPreferredSize(int width, int height) {
		
		setPreferredSize(new Dimension(width + boxWidth + 1, height + boxHeight + 1));
		
		viewWidth = width;
		viewHeight = height;
		
		if(window != null) {
			window.refreshScrollPane();
		}
		
	}
	
	public void setDisplayPicture(BufferedImage pic, String filePath) {
		originalPicture = displayPicture = pic;
		
		this.filePath = filePath;
		
		setTotalPreferredSize(pic.getWidth(), pic.getHeight());
	}
	
	/**
	 * {@code factor} should be as a percent multiplied by a hundred, not decimal form
	 * 
	 * @param factor
	 */
	public void rescaleImage(int scaleFactor) {
		
		this.scaleFactor = scaleFactor;
		
		displayPicture = getScaledImage(originalPicture, this.scaleFactor);
		
		/* Could convert this into a helper method*/
		viewWidth = displayPicture.getWidth();
		viewHeight = displayPicture.getHeight();
		
		potentialWidth = viewWidth;
		potentialHeight = viewHeight;
		/**/
		
		update(0);
		
		setTotalPreferredSize(viewWidth, viewHeight);
		
	}
	
	public static BufferedImage getScaledImage(BufferedImage original, double scaleFactor) {
		
		int w1 = original.getWidth();
		int w2 = (int) (w1 * (scaleFactor/100d));
		
		int h1 = original.getHeight();
		int h2 = (int) (h1 * (scaleFactor/100d));
		
		if(w2 <= 0 || h2 <= 0) {
			return original;
		}
		
		BufferedImage resizedImage = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_ARGB);
		
		int x_ratio = (int) ((w1 << 16)/w2) + 1;// 1 is the error factor, preventing a zero caused by premature rounding
		int y_ratio = (int) ((h1 << 16)/h2) + 1;
		
		int x2, y2;
		
		for(int i = 0; i < h2; i++) {
			for(int j = 0; j < w2; j++) {
				
				x2 = ((j*x_ratio)>>16);
				y2 = ((i*y_ratio)>>16);
				
				resizedImage.setRGB(j, i, original.getRGB(x2, y2));
				
			}
		}
		
		return resizedImage;
		
	}
	
	public boolean isEdited() {
		return edited;
	}
	
	public int getScaleFactor() {
		return scaleFactor;
	}
	
	public void setPenColor(Color c) {
		
		if(c == null) {
			return;
		}
		
		penColor = c;
	}
	
	public Color getPenColor() {
		return penColor;
	}
	
	public int getPenRGB() {
		return penColor.getRGB();
	}
	
	public void setPenShape(Shape penShape) {
		
		if(penShape != null) {
			this.penShape = penShape;
		}
	}
	
	public Shape getPenShape() {
		return penShape;
	}
	
	public void setPenSize(int size) {
		
		if(size == 0) {
			size = 1;
		}
		
		penWidth = penHeight = size;
		
	}
	
	public int getImageWidth() {
		return viewWidth;
	}
	
	public int getImageHeight() {
		return viewHeight;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		
		int x = e.getX();
		int y = e.getY();
		
		handleCursorHover(x, y);
		
		Color temp = penColor;
		
		if(e.isMetaDown()) {
			penColor = Color.WHITE;
		}
		
		x2 = x;
		y2 = y;
		
		update(0);
		
		penColor = temp;
		
	}
	
	private void handleCursorHover(int x, int y) {

		if(rightBox.intersects(x, y)) {
			
			cursor = Cursors.HORIZONTAL.getCursor();
			
		} else if(bottomBox.intersects(x, y)) {
			
			cursor = Cursors.VERTICAL.getCursor();
			
		} else if(cornerBox.intersects(x, y)) {
			
			cursor = Cursors.DIAGONAL.getCursor();
			
		} else {
			cursor = Cursors.DEFAULT.getCursor();
		}
		
	}
	
	public static enum PenType {
		
		DRAW,
		TWO_COORDINATES;
		
	}
	
	public static enum Shape {
		
		RECTANGLE("Rectangle", PenType.DRAW),
		CIRCLE("Circle", PenType.DRAW),
		TRIANGLE("Triangle", PenType.DRAW),
		SELECT("Selection Box", PenType.TWO_COORDINATES),
		LINE("Line", PenType.TWO_COORDINATES);
		
		private final String id;
		private final PenType type;
		
		private Shape(String id, PenType type) {
			this.id = id;
			this.type = type;
		}
		
		public String getId() {
			return id;
		}
		
		public PenType getPenType() {
			return type;
		}
		
		public static Shape getById(String id) {
			
			for(Shape shape: Shape.values()) {
				if(shape.getId().equals(id)) {
					return shape;
				}
			}
			
			return null;
			
		}
		
		public static String[] getIds() {
			Shape[] shapes = Shape.values();
			
			String[] re = new String[shapes.length];
			
			for(int i = 0; i < shapes.length; i++) {
				re[i] = shapes[i].getId();
			}
			
			return re;
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		if(e.isMetaDown()) {
			
			if(resizing) {
				cancelResizing();
				
				stopDragging();
				
			} else {
				
				dragging = true;
				
				x2 = x1 = x;
				y2 = y1 = y;
				
				Color temp = penColor;
				
				penColor = Color.WHITE;
				
				update(0);
				
				penColor = temp;
			}
			
		} else if(e.isAltDown()) {
			
		} else {
			
			if(!boxesDragging(x, y)) { // boxesDragging handles all the dragging of the boxes
				
				dragging = true;
				
				x2 = x1 = x;
				y2 = y1 = y;
				
				update(0);
			}
			
		}
	}
	
	private boolean boxesDragging(int x, int y) {
		
		boolean re = false;
		
		if(rightBox.intersects(x, y)) {
			re = true;
			rightBox.setDragging(re);
			
		} else if(bottomBox.intersects(x, y)) {
			re = true;
			bottomBox.setDragging(re);
			
		} else if(cornerBox.intersects(x, y)) {
			re = true;
			cornerBox.setDragging(re);
			
		}
		
		return re;
		
	}
	
	private void cancelResizing() {
		
		resizing = false;
		
		potentialWidth = viewWidth;
		potentialHeight = viewHeight;
		
		cursor = Cursors.DEFAULT.getCursor();
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
		if(e.isMetaDown()) {
			
			dragging = false;
			
			stopDragging();
			
			x1 = x2 = e.getX();
			y1 = y2 = e.getY();
			
			Color temp = penColor;
			
			penColor = Color.WHITE;
			
			rescaleImage(scaleFactor);
			
			changeManager.stopRecordingUndoChange();
			
			update(0);
			
			penColor = temp;
			
		} else if(e.isAltDown()) {
			
		} else {
			dragging = false;
			
			potentiallyResize();
			
			stopDragging();
			
			x1 = x2 = e.getX();
			y1 = y2 = e.getY();
			
			rescaleImage(scaleFactor);// update the strokes to fix the pixels
			
			changeManager.stopRecordingUndoChange();
			
			update(0);
		}
		
	}
	
	private void potentiallyResize() {
		
		if(resizing) {
			
			viewWidth = potentialWidth;
			viewHeight = potentialHeight;
			
			setNewImageDimensions(viewWidth, viewHeight);
			
			resizing = false;
			
		}
	}
	
	private void stopDragging() {
		
		rightBox.setDragging(false);
		bottomBox.setDragging(false);
		cornerBox.setDragging(false);
		
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		if(e.isControlDown()) {
			window.changeZoom(-e.getUnitsToScroll());
		}
		
	}
	
}
