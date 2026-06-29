package test.coverter.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import test.coverter.model.SimpleConverter.Method;

public class Config {
	
	private static Config instance = null;
	public static Config getInstance() {
		if (instance == null) instance = new Config();
		return instance;
	}
	private static String CONFIG_FILENAME = "config.properties"; 
	
	private String PDFBoxFolder = "";
	private String originalFileLocation = "";
	private String convertedFileLocation = "";
	private Method selectedMethod = Method.ExtractText;

	public void setPDFBoxLocation(String PDFBoxFolder) { this.PDFBoxFolder = PDFBoxFolder; }
	public void setOriginalFileLocation(String originalFileLocation) { this.originalFileLocation = originalFileLocation; }
	public void setConvertedFileLocation(String convertedFileLocation) { this.convertedFileLocation = convertedFileLocation; }
	public void setSelectedMethod(Method selectedMethod) { this.selectedMethod = selectedMethod; }
	
	public String getPDFBoxFolder() { return PDFBoxFolder; }
	public String getOriginalFileLocation() { return originalFileLocation; }
	public String getConvertedFileLocation() { return convertedFileLocation; }
	public Method getSelectedMethod() { return selectedMethod; }
	public String getCommand() {
		StringBuilder sb = new StringBuilder();
		sb.append("java -jar" + " ");
		sb.append(PDFBoxFolder + "/pdfbox-app-2.0.9.jar" + " ");
		sb.append(selectedMethod.getMethod() + " ");
		if (selectedMethod.equals(Method.ExtractText)) sb.append("-html" + " ");
		sb.append('"' + originalFileLocation + '"' + " ");
		if (selectedMethod.equals(Method.ExtractText)) sb.append('"' + convertedFileLocation + '"');
		return sb.toString();
	}
	
	public Config() {
		loadConfig();
	}
	
	private void loadConfig() {
		File check = new File(CONFIG_FILENAME);
		if (check.exists()) { 
			Properties prop = new Properties();
			InputStream input = null;
			try {
				input = new FileInputStream(CONFIG_FILENAME);
				prop.load(input);
				if (prop.getProperty("PDFBoxFolder") != null) {
					PDFBoxFolder = "";
				} else {
					PDFBoxFolder = prop.getProperty("PDFBoxFolder");
				}
				if (prop.getProperty("SelectedMethod") != null) {
					selectedMethod = Method.ExtractText;
				} else {
					if (prop.getProperty("SelectedMethod").equals("ExtractText")) selectedMethod = Method.ExtractText;
					else selectedMethod = Method.ExtractImages;
				}
				System.out.println("Current PDF Box Folder: " + prop.getProperty("PDFBoxFolder"));
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void storeConfig() {
		Properties prop = new Properties();
		OutputStream output = null;
		try {
			output = new FileOutputStream(CONFIG_FILENAME);
			prop.setProperty("PDFBoxFolder", PDFBoxFolder);
			prop.setProperty("SelectedMethod", selectedMethod.getMethod());
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void debug() {
		System.out.println(PDFBoxFolder);
		System.out.println(originalFileLocation);
		System.out.println(convertedFileLocation);
	}
	
}
