package com.rawad.windowmaker.awt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wb.swt.SWTResourceManager;

public class MakeWindow {

	protected Shell shlFrameTitle;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MakeWindow window = new MakeWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlFrameTitle.open();
		shlFrameTitle.layout();
		while (!shlFrameTitle.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlFrameTitle = new Shell();
		shlFrameTitle
				.setImage(SWTResourceManager
						.getImage("C:\\Users\\Rawad\\Documents\\Programming Tools\\Window Maker\\res\\test.png"));
		shlFrameTitle.setMinimumSize(new Point(500, 500));
		shlFrameTitle.setSize(500, 500);
		shlFrameTitle.setText("Frame Title");

	}

}
