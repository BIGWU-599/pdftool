package test.coverter.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import test.coverter.gui.editor.EditorWindow;
import test.coverter.gui.startup.Preferences;
import test.coverter.gui.startup.StartUpWizard;
import test.coverter.model.SimpleConverter;
import test.coverter.model.SimpleConverter.Method;
import test.coverter.model.Config;

public class Startup {

	public static void routeAction(String action) {
		if (action.equals("Open PDFC Editor")) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Portable Document Format (.pdf)", "pdf");
			chooser.addChoosableFileFilter(fileFilter);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setApproveButtonText("Open PDF in Editor");
			chooser.setDialogTitle("Open PDF");
			int returnVal = chooser.showOpenDialog(StartUpWizard.getInstance());
			if(returnVal == JFileChooser.APPROVE_OPTION) {
		        StartUpWizard.getInstance().dispose();
		        String path = chooser.getSelectedFile().getAbsolutePath();
		        Config.getInstance().setOriginalFileLocation(path);
				Editor.getInstance();
			}
		}
		else if (action.equals("Simple PDF Conversion")) {
			if (!Config.getInstance().getPDFBoxFolder().equals("")) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Portable Document Format (.pdf)", "pdf");
				chooser.addChoosableFileFilter(fileFilter);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setApproveButtonText("Select");
				chooser.setDialogTitle("Select PDF");
				int returnVal = chooser.showOpenDialog(StartUpWizard.getInstance());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
			        String path = chooser.getSelectedFile().getAbsolutePath();
			        Config.getInstance().setOriginalFileLocation(path);
			        if (Config.getInstance().getSelectedMethod().equals(Method.ExtractImages)) {
			        	new SimpleConverter();
			        } else {	
				        chooser.setDialogTitle("Create Converted File");
						chooser.setApproveButtonText("Create");
						chooser.removeChoosableFileFilter(fileFilter);
						chooser.addChoosableFileFilter(new FileNameExtensionFilter("Hypertext Markup Language (.html)", "html"));
						returnVal = chooser.showOpenDialog(StartUpWizard.getInstance());
						if(returnVal == JFileChooser.APPROVE_OPTION) {
					        path = chooser.getSelectedFile().getAbsolutePath();
					        Config.getInstance().setConvertedFileLocation(path + ".html");
					        // Config.getInstance().debug();
					        new SimpleConverter();
						}
			        }
				}	
			} else {
				JOptionPane.showMessageDialog(null, "Please set folder containing PDFBox.jar in Preferences");
			}
		} else if (action.equals("Preferences")) {
			new Preferences();
		} else if (action.equals("Exit")) {
			Config.getInstance().storeConfig();
			System.exit(0);
		}
	}
	
	public static WindowAdapter getWindowAdapter() {
		WindowAdapter adapter = new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				Startup.routeAction("Exit");
	       }        
	    };
	    return adapter;
	}
	
}
