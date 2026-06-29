package test.coverter.gui.editor;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.pdfbox.pdmodel.PDDocument;

import test.coverter.controller.Editor;

public class EditorMenu extends JPanel {

	private JPanel infoPanel;
	
	public EditorMenu() {
		setBorder(new EmptyBorder(10,10,10,10));
		setLayout(new GridLayout(0,1));
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(0,1));
		add(infoPanel);
		generateButtons();
		setVisible(true);
	}
	
	public void generateInfoPanel(PDDocument doc) {
		infoPanel.add(new JLabel("Title: " + doc.getDocumentInformation().getTitle()));
		infoPanel.add(new JLabel("Author: " + doc.getDocumentInformation().getAuthor()));
		infoPanel.add(new JLabel("Pages: " + doc.getPages().getCount()));
		revalidate();
		repaint();
	}
	
	private String[] buttons = {"PLACEHOLDER", "Extract Text", "Extract Images", "PLACEHOLDER", "PLACEHOLDER", "Save", "Exit"};

	private void generateButtons() {
		for (String btnText : buttons) {
			if (btnText.equals("PLACEHOLDER")) {
				add(new JLabel());
			} else {
				JButton btn = new JButton(btnText);
				btn.addActionListener(e -> Editor.getInstance().routeAction(btnText));
				add(btn);
			}
		}
	}
	
}
