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
	
	private Color penColor;
	
	private Shapes penType;
	
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
	
	private ResizerBox rightBox;
	private ResizerBox bottomBox;
	private ResizerBox cornerBox;
	
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
		
		penWidth = 20;
		penHeight = 20;
		
		penType = Shapes.RECTANGLE;//rectangle by default
		
		filePath = "res/test.png";
		
		initPicture();
		
		viewWidth = displayPicture.getWidth();
		viewHeight = displayPicture.getHeight();
		
		potentialWidth = viewWidth;
		potentialHeight = viewHeight;
		
		rightBox = new ResizerBox(viewWidth, viewHeight/2 - (boxHeight/2), boxWidth, boxHeight);
		bottomBox = new ResizerBox(viewWidth/2 - (boxWidth/2), viewHeight, boxWidth, boxHeight);
		cornerBox = new ResizerBox(viewWidth, viewHeight, boxWidth, boxHeight);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(displayPicture != null) {
			g.drawImage(displayPicture, 0, 0, null);
		} else {
			g.drawString("Put A Pic In Me", getWidth()/2 - (50), getHeight()/2);
		}
		
		if(resizing) {
			g.setColor(Color.GRAY);
			g.drawRect(0, 0, potentialWidth, potentialHeight);
		}
		
		rightBox.setX(viewWidth);
		rightBox.setY(viewHeight/2 - (boxHeight/2));
		
		rightBox.render(g);
		
		bottomBox.setX(viewWidth/2 - (boxWidth/2));
		bottomBox.setY(viewHeight);
		
		bottomBox.render(g);
		
		cornerBox.setX(viewWidth);
		cornerBox.setY(viewHeight);
		
		cornerBox.render(g);
		
		g.dispose();
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
		
		if(dragging) {//could check if we're in image's range but Graphics object don't care 'bout where it draws its pixels
			
			renderPenStrokes();
			
			edited = true;
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
		
		window.setInfo(x2, y2, potentialWidth, potentialHeight);
		window.repaint();
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
		
		int tempWidth = (penWidth*scaleFactor)/100;
		int tempHeight = (penHeight*scaleFactor)/100;
		
		x = x - (tempWidth/2);
		y = y - (tempHeight/2);
		
		switch(penType) {
		
		case RECTANGLE:
			drawRectangle(x, y, tempWidth, tempHeight);
//			originalPicture.setRGB((x*100)/scaleFactor, (y*100)/scaleFactor, getPenRGB());
			//displayPicture.setRGB(x, y, getPenRGB());// scale the displayPicture's pixels
			break;
			
		case CIRCLE:
			break;
			
		case TRIANGLE:
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
					displayPicture.setRGB(i, j, getPenRGB());
					originalPicture.setRGB((i * 100)/scaleFactor, (j * 100)/scaleFactor, getPenRGB());
				} catch(ArrayIndexOutOfBoundsException ex) {
					break;
				}
				
			}
		}
		
	}
	
	private int abs(int a) {
		
		return Math.abs(a);
		
	}
	
	private void setNewImageDimensions(int width, int height) {
		//copy over current image data and add new empty pixels
		
		BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				try {
					temp.setRGB(x, y, displayPicture.getRGB(x, y));
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
	
	private BufferedImage getScaledImage(BufferedImage original, double scaleFactor) {
		
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
	
	public void setPenType(Shapes penType) {
		
		if(penType != null) {
			this.penType = penType;
		}
	}
	
	public Shapes getPenType() {
		return penType;
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
	
	public static enum Shapes {
		
		RECTANGLE("Rectangle"),
		CIRCLE("Circle"),
		TRIANGLE("Triangle");
		
		private final String id;
		
		private Shapes(String id) {
			this.id = id;
		}
		
		public String getId() {
			return id;
		}
		
		public static Shapes getById(String id) {
			
			for(Shapes shape: Shapes.values()) {
				if(shape.getId().equals(id)) {
					return shape;
				}
			}
			
			return null;
			
		}
		
		public static String[] getIds() {
			Shapes[] shapes = Shapes.values();
			
			String[] re = new String[shapes.length];
			
			for(int i = 0; i < shapes.length; i++) {
				re[i] = shapes[i].getId();
			}
			
			return re;
		}
		
	}
	
	private static enum Cursors {
		
		DEFAULT(new Cursor(Cursor.DEFAULT_CURSOR)),
		HORIZONTAL(new Cursor(Cursor.E_RESIZE_CURSOR)),
		VERTICAL(new Cursor(Cursor.S_RESIZE_CURSOR)),
		DIAGONAL(new Cursor(Cursor.SE_RESIZE_CURSOR));
		
		private final Cursor type;
		
		private Cursors(Cursor type) {
			this.type = type;
		}
		
		public Cursor getCursor() {
			return type;
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
				
				x2 = y2 = x;
				y1 = y2 = y;
				
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
	
	private class ResizerBox {
		
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
	
}
