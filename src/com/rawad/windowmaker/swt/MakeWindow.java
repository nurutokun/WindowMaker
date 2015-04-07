package com.rawad.windowmaker.swt;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.PaintEvent;

public class MakeWindow {

	protected Shell shlFrameTitle;
	private TabFolder tabFolder;
	private Text text;
	private CustomCanvas customCanvas;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				try {
					MakeWindow window = new MakeWindow();
					window.open();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
		shlFrameTitle.setMinimumSize(new Point(0, 0));
		shlFrameTitle.setSize(500, 500);
		shlFrameTitle.setText("Frame Title");
		shlFrameTitle.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(shlFrameTitle, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		tabFolder = new TabFolder(composite, SWT.BORDER);

		TabItem textEditorTab = new TabItem(tabFolder, SWT.NONE);
		textEditorTab.setText("Text Editor");

		text = new Text(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL | SWT.MULTI);
		textEditorTab.setControl(text);

		TabItem imageEditorTab = new TabItem(tabFolder, SWT.NONE);
		imageEditorTab.setText("Image Editor");

		customCanvas = new CustomCanvas(tabFolder, SWT.NONE);
		customCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				customCanvas.render(e.gc);
			}
		});
		imageEditorTab.setControl(customCanvas);

	}

}
