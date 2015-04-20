package com.rawad.windowmaker.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

	private static MakeWindow window;

	private JFrame frmFrameTitle;

	private JTabbedPane tabbedPane;
	private JScrollPane scrollPane_1;

	private JTextArea textArea;
	private JSlider slider;

	private JMenuItem mntmSaveFile;
	private CustomPanel customPanel;

	private JLabel infoLabel;
	private JMenuItem mntmUndo;
	private JMenuItem mntmRedo;

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
		frmFrameTitle.getContentPane().setLayout(new BorderLayout(0, 0));

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
		gbl_panel.rowHeights = new int[] { 372, 24, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 0.0 };
		gbl_panel.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
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

		slider = new JSlider();
		slider.setMajorTickSpacing((CustomPanel.MAX_PEN_HEIGHT + CustomPanel.MAX_PEN_WIDTH / 2) / 10);
		slider.setMinorTickSpacing((CustomPanel.MAX_PEN_HEIGHT + CustomPanel.MAX_PEN_WIDTH / 2) / 10);
		slider.setValue(25);
		slider.addChangeListener(new SliderChangeListener());

		infoLabel = new JLabel("x, y: 6969, 6969 | width, height: "
				+ customPanel.getImageWidth() + ", "
				+ customPanel.getImageHeight());
		GridBagConstraints gbc_infoLabel = new GridBagConstraints();
		gbc_infoLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_infoLabel.insets = new Insets(0, 0, 0, 5);
		gbc_infoLabel.gridx = 0;
		gbc_infoLabel.gridy = 1;
		panel.add(infoLabel, gbc_infoLabel);
		slider.setToolTipText("Pen Size");
		slider.setPaintLabels(true);
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider.gridx = 1;
		gbc_slider.gridy = 1;
		panel.add(slider, gbc_slider);

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
						customPanel.getPenType().getId());

				customPanel.setPenType(CustomPanel.Shapes
						.getById((String) returnVal));

			}
		});
		mnOptions.add(mntmPenType);

		mntmUndo = new JMenuItem("Undo");
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				InputEvent.CTRL_MASK));
		mnOptions.add(mntmUndo);

		mntmRedo = new JMenuItem("Redo");
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

	public void setInfo(int x, int y, int width, int height) {
		infoLabel.setText("x, y: " + x + ", " + y + " | width, height: "
				+ width + ", " + height);
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

			customPanel.setPenSize(source.getValue());
		}
	}
}
