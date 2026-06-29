package test.coverter.controller;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.PDFText2HTML;

import test.coverter.gui.editor.EditorWindow;
import test.coverter.gui.editor.SaveDialog;
import test.coverter.gui.startup.StartUpWizard;
import test.coverter.model.Config;
import test.coverter.model.EditorConverter;
import test.coverter.model.EditorData;

public class Editor {

	//Singleton to ensure only one instance of Editor exists and give easy access to Editor functionality
	private static Editor instance = null;
	public static Editor getInstance() {
		if (instance == null) instance = new Editor();
		return instance;
	}
	
	private EditorWindow window;
	private EditorConverter converter;
	private EditorData data;
	
	//Getters for Editor-specific objects
	public EditorConverter getConverter() { return converter; }
	public EditorWindow getWindow() { return window; }
	public EditorData getData() { return data; }
	
	public Editor() {
		converter = new EditorConverter();
		data = new EditorData();
		window = new EditorWindow();
		window.editorMenu.generateInfoPanel(converter.getDocument());
	}
	
	public void routeAction(String action) {
		if (action.equals("Exit")) {
			Startup.routeAction(action);
		} else if (action.equals("Extract Text")) {
			String extractedText;
			try {
				PDFTextStripper stripper = new PDFText2HTML();
				extractedText = stripper.getText(converter.getDocument());
				data.setExtractedText(extractedText);
				window.workbench.regenerate();
			} catch (IOException e) {
				e.printStackTrace();
			}		
		} else if (action.equals("Extract Images")) {
			converter.extractImages();
			window.workbench.regenerate();
		} else if (action.equals("Save")) {
			new SaveDialog();
		} else if (action.equals("Save Text")) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter html = new FileNameExtensionFilter("Hypertext Markup Language (.html)", "html");
			FileNameExtensionFilter txt = new FileNameExtensionFilter("Plain Text Document (.txt)", "txt");
			chooser.addChoosableFileFilter(html);
			chooser.addChoosableFileFilter(txt);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setApproveButtonText("Save Extracted Text");
			chooser.setDialogTitle("Save");
			int returnVal = chooser.showOpenDialog(getWindow());
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				if (chooser.getFileFilter().equals(html)) getData().saveText(chooser.getSelectedFile().getAbsolutePath(), ".html");
				else getData().saveText(chooser.getSelectedFile().getAbsolutePath(), ".txt");
			}
		} else if (action.equals("Save Images") || action.equals("Save Text & Images")) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Select Image Folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setApproveButtonText("Select Folder");
			int returnVal = chooser.showOpenDialog(getWindow());
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				String selectedFolder = chooser.getSelectedFile().getAbsolutePath();
		        getData().saveImages(selectedFolder);
		        if (action.equals("Save Text & Images")) getData().saveText(selectedFolder + "/extractedText", ".html");
			}
		} 
	}
	
}
