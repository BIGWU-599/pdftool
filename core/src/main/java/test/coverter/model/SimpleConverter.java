package test.coverter.model;

import java.io.IOException;
import java.io.PrintWriter;

import test.coverter.gui.editor.ConvertedViewer;
import test.coverter.gui.editor.EditorWindow;

public class SimpleConverter {

	//Enum to simplyfy coding through suggestions of existing methods of commandline control
	public enum Method {
		ExtractText("ExtractText"), ExtractImages("ExtractImages");
		private String value;
		private Method(String value) { this.value = value; };
		public String getMethod() { return value; }
	}
	
	public SimpleConverter() {
		try {
			Process p = Runtime.getRuntime().exec("cmd");
			new Thread(new Synchronizer(p.getErrorStream(), System.err)).start();
			new Thread(new Synchronizer(p.getInputStream(), System.out)).start();
			PrintWriter commandInput = new PrintWriter(p.getOutputStream());
			commandInput.println(Config.getInstance().getCommand());
			commandInput.close();
			int returnCode = p.waitFor();
			System.out.println("Return code = " + returnCode);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} 
	}	
	
}
