package test.coverter.gui.editor;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import test.coverter.model.Config;

public class ConvertedViewer extends JPanel {

	public ConvertedViewer() throws IOException {
		setBorder(new LineBorder(Color.BLACK, 2));
		JLabel label = new JLabel();
		BufferedReader br = new BufferedReader(new FileReader(Config.getInstance().getConvertedFileLocation()));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    label.setText(sb.toString().substring(102));
		} finally {
		    br.close();
		}
		add(label);
	}
	
}
