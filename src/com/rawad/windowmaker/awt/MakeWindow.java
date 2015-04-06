package com.rawad.windowmaker.awt;

import org.eclipse.core.databinding.DataBindingContext;
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

public class MakeWindow {

	private DataBindingContext m_bindingContext;

	protected Shell shlFrameTitle;
	private TabFolder tabFolder;

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

		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Text Editor");

		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("Image Editor");
		m_bindingContext = initDataBindings();

	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}
}
