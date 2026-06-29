package test.coverter.gui.editor;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import test.coverter.controller.Editor;

public class SaveDialog extends JDialog {

	public SaveDialog() {
		setTitle("Save");
		setLocationRelativeTo(null);
		generateButtons();
		setVisible(true);
		pack();
	}
	
	private String[] buttons = {"Save Text", "Save Images", "Save Text & Images", "Cancel" };
	
	private void generateButtons() {
		JPanel btnPanel = new JPanel();
		btnPanel.setBorder(new EmptyBorder(10,10,10,10));
		btnPanel.setLayout(new GridLayout(1,0));
		for (String btnText : buttons) {
			JButton btn = new JButton(btnText);
			if (btnText.equals("Cancel")) {
				btn.addActionListener(e -> dispose());
			} else { 
				btn.addActionListener(e -> {
					Editor.getInstance().routeAction(btnText);
					dispose();
				}); 
			}
			if (Editor.getInstance().getData().getExtractedText().equals("") || Editor.getInstance().getData().getExtractedImages().isEmpty()) {
				if (btnText.equals("Save Text") && Editor.getInstance().getData().getExtractedText().equals("")) btn.setEnabled(false);
				if (btnText.equals("Save Images") && Editor.getInstance().getData().getExtractedImages().isEmpty()) btn.setEnabled(false);
				if (btnText.equals("Save Text & Images")) btn.setEnabled(false);
			}
			btnPanel.add(btn);
		}
		add(btnPanel);
	}
	
}
