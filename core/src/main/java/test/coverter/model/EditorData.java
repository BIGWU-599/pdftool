package test.coverter.model;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import test.coverter.controller.Editor;

public class EditorData {

	private String extractedText;
	public void setExtractedText(String extractedText) { this.extractedText = extractedText; }
	public String getExtractedText() { return extractedText; }
	
	private ArrayList<RenderedImage> extractedImages;
	public void setExtractedImages(ArrayList<RenderedImage> extractedImages) { this.extractedImages = extractedImages; }
	public RenderedImage getExtractedImage( int imageIndex) { return extractedImages.get(imageIndex); }
	public ArrayList<RenderedImage> getExtractedImages() { return extractedImages; }
	
	public EditorData() {
		extractedText = "";
		extractedImages = new ArrayList<RenderedImage>();
	}
	
	public void saveText(String filepath, String extension) {
		File file = new File(filepath + extension);
	    if (!file.exists()) {
	        try {
				if (file.createNewFile()) {
				    PrintWriter out = new PrintWriter(file);
				    out.print(extractedText);
				    out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	public void saveImages(String directoryPath) {
		for (int i = 0; i < extractedImages.size(); i++) {
			File file = new File(directoryPath + "/image" + i + ".png");
			if (!file.exists()) {
				try {
					if (file.createNewFile()) {
						ImageIO.write(Editor.getInstance().getConverter().convertRenderedImage(extractedImages.get(i)), "PNG", file);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
}
