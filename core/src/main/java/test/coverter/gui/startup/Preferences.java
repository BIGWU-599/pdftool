package test.coverter.gui.startup;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import test.coverter.model.Config;
import test.coverter.model.SimpleConverter.Method;

public class Preferences extends JDialog {

	public Preferences() {
		setTitle("Preferences");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		initUI();
		setVisible(true);
		pack();
	}
	
	private void initUI() {
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10,10,10,10));
		contentPane.setLayout(new GridLayout(0,2));
		JLabel folderLabel = new JLabel("PDFBox Folder: " + Config.getInstance().getPDFBoxFolder() + "   ");
		JButton selectFolderBtn = new JButton("Select Folder");
		selectFolderBtn.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Select PDFBox Folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setApproveButtonText("Select Folder");
			int returnVal = chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				String selectedFolder = chooser.getSelectedFile().getAbsolutePath();
		        Config.getInstance().setPDFBoxLocation(selectedFolder);
		        folderLabel.setText("PDFBox Folder: " + Config.getInstance().getPDFBoxFolder());
			}
		});
		contentPane.add(folderLabel);
		contentPane.add(selectFolderBtn);
		JLabel methodLabel = new JLabel("Extract Method");
		ButtonGroup btnGrp = new ButtonGroup();
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new GridLayout(1,0));
		JRadioButton text = new JRadioButton("Text");
		if (Config.getInstance().getSelectedMethod().equals(Method.ExtractText)) text.setEnabled(true);
		text.addActionListener(e -> Config.getInstance().setSelectedMethod(Method.ExtractText));
		btnGrp.add(text);
		JRadioButton images = new JRadioButton("Images");
		if (Config.getInstance().getSelectedMethod().equals(Method.ExtractImages)) images.setEnabled(true);
		images.addActionListener(e -> Config.getInstance().setSelectedMethod(Method.ExtractImages));
		btnGrp.add(images);
		radioPanel.add(text);
		radioPanel.add(images);
		contentPane.add(methodLabel);
		contentPane.add(radioPanel);
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new GridLayout(1,1));
		btnPanel.setBorder(new EmptyBorder(0,10,10,10));
		JButton exit = new JButton("Exit");
		exit.addActionListener(e -> dispose());
		btnPanel.add(exit);
		add(contentPane, BorderLayout.CENTER);
		add(btnPanel, BorderLayout.SOUTH);
	}
	
}
