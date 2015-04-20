package com.rawad.windowmaker.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class CustomPanel extends JPanel implements MouseListener, MouseMotionListener {
	
	public static final int MAX_PEN_WIDTH = 100;
	public static final int MAX_PEN_HEIGHT = 100;
	
	/**
	 * Big, Long, number
	 */
	private static final long serialVersionUID = 6101949493654211445L;
	
	private static final Color INIT_PIC_BACKGROUND = new Color(0xFFFFFFFF);
	
	private static MakeWindow window;
	
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
	
	private boolean dragging;
	private boolean resizing;
	private boolean edited;
	
	public CustomPanel() {
		super();
		
		penColor = new Color(0, 0, 0);
		
		dragging = false;
		
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
			g.drawRect(-1, -1, potentialWidth, potentialHeight);
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
			if(!filePath.equals("") && edited) {
				ImageIO.write(displayPicture, filePath.substring(filePath.length()-3), new File(filePath));
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	private void initPicture() {
		
		try {
			displayPicture = ImageIO.read(new File("res/test.png"));
			
			setPreferredSize(new Dimension(displayPicture.getWidth() + boxWidth + 1, displayPicture.getHeight() + boxHeight + 1));
			
		} catch(IOException ex) {
			
			displayPicture = new BufferedImage(viewWidth, viewHeight, BufferedImage.TYPE_INT_ARGB);
			
			setPreferredSize(new Dimension(viewWidth + boxWidth + 1, viewHeight + boxHeight + 1));
			
			for(int i = 0; i < displayPicture.getHeight(); i++) {
				for(int j = 0; j < displayPicture.getWidth(); j++) {
					displayPicture.setRGB(j, i, INIT_PIC_BACKGROUND.getRGB());
				}
			}
		}
	}
	
	private void update(long timePassed) {
		
//		System.out.println("potential(width, height): " + potentialWidth + ", " + potentialHeight +
//				" picture(width, height): " + displayPicture.getWidth() + ", " + displayPicture.getHeight());
		
		if(dragging) {//could check if we're in image's range but Graphics object don't care 'bout where it draws its pixels
//			Using Raster:
//			int[] pixels = new int[4];
//			
//			pixels[0] = penColor.getRed();
//			pixels[1] = penColor.getGreen();
//			pixels[2] = penColor.getBlue();
//			pixels[3] = penColor.getAlpha();
//			
//			raster.setPixel(x, y, pixels);
//			
//			displayPicture.setData(raster);
//		
//			Using Graphics Object:
			Graphics g = displayPicture.createGraphics();
			
			g.setColor(penColor);
			
			g.translate(-(penWidth/2), -(penHeight/2));
			
			render(g);
			
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
	
	private void render(Graphics g) {
		
		bresenhamAlgorithm(g, x1, y1, x2, y2);
		
	}
	
	private void bresenhamAlgorithm(Graphics g, int x1, int y1, int x2, int y2) {
		
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
				setPixel(g, y, x);
			} else {
				setPixel(g, x, y);
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
				setPixel(g, y, x);
			} else {
				setPixel(g, x, y);
			}
			
		}
		
		if(x1 == x2 && y1 == y2) {
			setPixel(g, x, y);
		}
		
	}
	
	private void setPixel(Graphics g, int x, int y) {
		
		switch(penType) {
		
		case RECTANGLE:
			g.fillRect(x, y, penWidth, penHeight);
			break;
			
		case CIRCLE:
			g.fillOval(x, y, penWidth, penHeight);
			break;
			
		case TRIANGLE:
			g.drawRect(x, y, penWidth, penHeight);
			break;
			
		default:
			g.setColor(Color.RED);
			g.drawString("no penType error; this shouldn't happen. It is actaully impressive you made this happen. Bravo to you.", x, y);
			break;
			
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
		
		displayPicture = temp;
		
		setPreferredSize(new Dimension(width + boxWidth + 1, height + boxHeight + 1));
		
		window.refreshScrollPane();
		
	}
	
	public void setDisplayPicture(BufferedImage pic, String filePath) {
		displayPicture = pic;
		
		this.filePath = filePath;
		
		setPreferredSize(new Dimension(pic.getWidth() + boxWidth + 1, pic.getHeight() + boxHeight + 1));
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
	public void mouseEntered(MouseEvent e) {
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
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
