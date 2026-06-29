package test.coverter.gui.editor;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import test.coverter.controller.Editor;

public class EditorWorkbench extends JTabbedPane {

	public EditorWorkbench() {
		setBorder(new EmptyBorder(10,0,10,10));
		setVisible(true);
	}
	
	public void regenerate() {
		removeAll();
		
		if (!Editor.getInstance().getData().getExtractedText().equals("")) generateTextPane();
		if (!Editor.getInstance().getData().getExtractedImages().isEmpty()) generateImagesPane();
		
		revalidate();
		repaint();
	}
	
	public void generateTextPane() {
		JPanel extractedTextPanel = new JPanel();
		extractedTextPanel.setLayout(new GridLayout(1,1));
		JTextPane textPane = new JTextPane();
		textPane.setMinimumSize(new Dimension(getWidth(), getHeight()));
		textPane.setText(Editor.getInstance().getData().getExtractedText());
		JScrollPane scrollPane = new JScrollPane(textPane);
		extractedTextPanel.add(scrollPane);
		addTab("Extracted Text", extractedTextPanel);
	}
	
	public void generateImagesPane() {
		JPanel extractedImagesPanel = new JPanel();
		JTabbedPane tabbedPane = new JTabbedPane();
		for (int i = 0; i < Editor.getInstance().getData().getExtractedImages().size(); i++) {
			RenderedImage img = Editor.getInstance().getData().getExtractedImage(i);
			BufferedImage bImg = Editor.getInstance().getConverter().convertRenderedImage(img);
			ImageIcon icon = new ImageIcon(bImg);
			JLabel imgLabel = new JLabel(icon);
			JScrollPane scrollPane = new JScrollPane(imgLabel);
			tabbedPane.addTab("Image " + i, scrollPane);
		}
		extractedImagesPanel.add(tabbedPane);
		addTab("Extracted Images", extractedImagesPanel);
	}
	
}
