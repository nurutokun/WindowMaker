package com.rawad.windowmaker.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MakeWindow {

	public static final int MIN_ZOOM_TICK_SPACING = 25;

	private static MakeWindow window;

	private JFrame frmFrameTitle;

	private JTabbedPane tabbedPane;
	private JScrollPane scrollPane_1;

	private JTextArea textArea;
	private JSlider pensizeSlider;

	private JMenuItem mntmSaveFile;
	private CustomPanel customPanel;

	private JLabel infoLabel;
	private JMenuItem mntmUndo;
	private JMenuItem mntmRedo;
	private JSlider zoomSlider;
	private JLabel zoomLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new MakeWindow();
					window.initialize();
					window.frmFrameTitle.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MakeWindow() {
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFrameTitle = new JFrame();
		frmFrameTitle.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				// mntmSaveFile.doClick();// saves the file just in case

				if (customPanel.isEdited()) {
					int re = JOptionPane
							.showConfirmDialog(frmFrameTitle,
									"There are unsaved changes in this. Do you want to save them?");

					if (re == JOptionPane.OK_OPTION) {
						customPanel.saveImage();
					} else if (re == JOptionPane.CANCEL_OPTION) {
						return;// Don't close
					}
				}

				frmFrameTitle.dispose();
				System.exit(0);
			}
		});

		frmFrameTitle.setTitle("Frame Tit-le");
		frmFrameTitle.setBounds(0, 0, 500, 500);
		frmFrameTitle.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmFrameTitle.getContentPane().setLayout(new GridLayout(0, 1, 10, 10));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmFrameTitle.getContentPane().add(tabbedPane);

		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("Proper Text Editor", null, scrollPane, null);
		tabbedPane.setEnabledAt(0, true);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		JPanel panel = new JPanel();
		panel.setForeground(Color.WHITE);
		panel.setBackground(UIManager.getColor("Button.background"));
		tabbedPane.addTab("Image Editor", null, panel, null);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 97, 246 };
		gbl_panel.rowHeights = new int[] { 372, 0, 24, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 0.0 };
		gbl_panel.rowWeights = new double[] { 1.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridwidth = 2;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 0;
		panel.add(scrollPane_1, gbc_scrollPane_1);

		customPanel = new CustomPanel();
		scrollPane_1.setViewportView(customPanel);

		SliderChangeListener sliderChangeListener = new SliderChangeListener();

		pensizeSlider = new JSlider();
		pensizeSlider
				.setMajorTickSpacing((CustomPanel.MAX_PEN_HEIGHT + CustomPanel.MAX_PEN_WIDTH / 2) / 10);
		pensizeSlider
				.setMinorTickSpacing((CustomPanel.MAX_PEN_HEIGHT + CustomPanel.MAX_PEN_WIDTH / 2) / 10);
		pensizeSlider.setValue(25);
		pensizeSlider.addChangeListener(sliderChangeListener);

		infoLabel = new JLabel("x, y: 6969, 6969 | width, height: "
				+ customPanel.getImageWidth() + ", "
				+ customPanel.getImageHeight());
		GridBagConstraints gbc_infoLabel = new GridBagConstraints();
		gbc_infoLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_infoLabel.insets = new Insets(0, 0, 5, 5);
		gbc_infoLabel.gridx = 0;
		gbc_infoLabel.gridy = 1;
		panel.add(infoLabel, gbc_infoLabel);
		pensizeSlider.setToolTipText("Pen Size");
		pensizeSlider.setPaintLabels(true);
		GridBagConstraints gbc_pensizeSlider = new GridBagConstraints();
		gbc_pensizeSlider.insets = new Insets(0, 0, 5, 0);
		gbc_pensizeSlider.fill = GridBagConstraints.HORIZONTAL;
		gbc_pensizeSlider.gridx = 1;
		gbc_pensizeSlider.gridy = 1;
		panel.add(pensizeSlider, gbc_pensizeSlider);

		zoomSlider = new JSlider();
		zoomSlider.setSnapToTicks(true);
		zoomSlider.setValue(100);
		zoomSlider.addChangeListener(sliderChangeListener);

		zoomLabel = new JLabel("Zoom: " + zoomSlider.getValue() + "%");
		GridBagConstraints gbc_zoomLabel = new GridBagConstraints();
		gbc_zoomLabel.fill = GridBagConstraints.BOTH;
		gbc_zoomLabel.insets = new Insets(0, 0, 0, 5);
		gbc_zoomLabel.gridx = 0;
		gbc_zoomLabel.gridy = 2;
		panel.add(zoomLabel, gbc_zoomLabel);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setMinorTickSpacing(MIN_ZOOM_TICK_SPACING);
		zoomSlider.setMajorTickSpacing(MIN_ZOOM_TICK_SPACING * 4);
		zoomSlider.setToolTipText("Zoom: " + zoomSlider.getValue() + "%");
		zoomSlider.setMaximum(MIN_ZOOM_TICK_SPACING * 32);
		zoomSlider.setMinimum(MIN_ZOOM_TICK_SPACING);
		GridBagConstraints gbc_zoomSlider = new GridBagConstraints();
		gbc_zoomSlider.fill = GridBagConstraints.BOTH;
		gbc_zoomSlider.gridx = 1;
		gbc_zoomSlider.gridy = 2;
		panel.add(zoomSlider, gbc_zoomSlider);

		JMenuBar menuBar = new JMenuBar();
		frmFrameTitle.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOpenFile = new JMenuItem("Open File");
		mntmOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser f = new JFileChooser();

				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"All Files", "*");

				int selected = tabbedPane.getSelectedIndex();

				switch (selected) {

				case 0:
					filter = new FileNameExtensionFilter("Text Files", "txt",
							"bat");
					break;

				case 1:
					filter = new FileNameExtensionFilter("Image Files", "jpg",
							"png", "jpeg", "gif");
					break;
				}

				f.setFileFilter(filter);

				int returnVal = f.showOpenDialog(frmFrameTitle);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = f.getSelectedFile();

					String filterDescription = filter.getDescription();

					if (filterDescription.equals("Text Files")) {
						String fileContent = getFileContent(file.getPath());

						textArea.setText(fileContent);
					} else if (filterDescription.equals("Image Files")) {
						BufferedImage pic = openPicture(file);

						customPanel.setDisplayPicture(pic, file.getPath());

						frmFrameTitle.repaint();
						refreshScrollPane();
					}

				}
			}
		});
		mntmOpenFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmOpenFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(1);
			}
		});

		mntmSaveFile = new JMenuItem("Save File");
		mntmSaveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				customPanel.saveImage();
			}
		});
		mntmSaveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmSaveFile);
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmExit);

		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);

		JMenuItem mntmColour = new JMenuItem("Colour");
		mntmColour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color c = JColorChooser.showDialog(frmFrameTitle,
						"Choose your Color", customPanel.getPenColor());

				customPanel.setPenColor(c);

			}
		});
		mnOptions.add(mntmColour);

		JMenuItem mntmPenType = new JMenuItem("Pen Type");
		mntmPenType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] options = CustomPanel.Shapes.getIds();

				Object returnVal = JOptionPane.showInputDialog(frmFrameTitle,
						"Select Pen Type", "Pen Type",
						JOptionPane.INFORMATION_MESSAGE, null, options,
						customPanel.getPenShape().getId());

				customPanel.setPenShape(CustomPanel.Shapes
						.getById((String) returnVal));

			}
		});
		mnOptions.add(mntmPenType);

		MenuItemActionListener mntmListener = new MenuItemActionListener();

		mntmUndo = new JMenuItem("Undo");
		mntmUndo.addActionListener(mntmListener);
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				InputEvent.CTRL_MASK));
		mnOptions.add(mntmUndo);

		mntmRedo = new JMenuItem("Redo");
		mntmRedo.addActionListener(mntmListener);
		mntmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				InputEvent.CTRL_MASK));
		mnOptions.add(mntmRedo);
	}

	private String getFileContent(String path) {
		String content = "";

		try (BufferedReader br = new BufferedReader(new FileReader(path));) {

			String line = null;

			while ((line = br.readLine()) != null) {
				content += line + "\r\n";
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return content;
	}

	private BufferedImage openPicture(File file) {

		BufferedImage temp = null;

		try {
			temp = ImageIO.read(file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return temp;

	}

	public void repaint() {
		frmFrameTitle.repaint();
	}

	public void refreshScrollPane() {
		scrollPane_1.invalidate();
		scrollPane_1.validate();
		scrollPane_1.revalidate();
	}

	public int getHorizontalScroll() {
		return scrollPane_1.getHorizontalScrollBar().getValue();
	}

	public int getVerticalScroll() {
		return scrollPane_1.getVerticalScrollBar().getValue();
	}

	public int getViewPortWidth() {
		return scrollPane_1.getViewport().getWidth();
	}

	public int getViewPortHeight() {
		return scrollPane_1.getViewport().getHeight();
	}

	public void setInfo(int x, int y, int width, int height) {
		infoLabel.setText("x, y: " + x + ", " + y + " | width, height: "
				+ width + ", " + height);
	}

	public void changeZoom(int delta) {

		int value = zoomSlider.getValue();

		if (delta < 0) {
			zoomSlider.setValue(value - MIN_ZOOM_TICK_SPACING);// adds exactly
																// the minimum
																// amount of
																// tick spacing
		} else if (delta > 0) {
			zoomSlider.setValue(value + MIN_ZOOM_TICK_SPACING);
		}

	}

	public static MakeWindow instance() {
		return window;
	}

	@SuppressWarnings("unused")
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	private class SliderChangeListener implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			int value = source.getValue();

			if (source.equals(pensizeSlider)) {
				customPanel.setPenSize(value);

			} else if (source.equals(zoomSlider)) {
				customPanel.rescaleImage(value);

				zoomSlider.setToolTipText("Zoom: " + value + "%");
				zoomLabel.setText("Zoom: " + value + "%");
			}

		}
	}

	private class MenuItemActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem) e.getSource();

			if (source == mntmUndo) {
				customPanel.undo();
			} else if (source == mntmRedo) {
				customPanel.redo();
			}

		}
	}

}
