package test.coverter.model;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import test.coverter.controller.Editor;

public class EditorConverter {

	private PDDocument document;
	public PDDocument getDocument() { return document; }
	public void loadDocument(File pdf) {
		try {
			document = PDDocument.load(pdf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public EditorConverter() {
		File pdf = new File(Config.getInstance().getOriginalFileLocation());
		loadDocument(pdf);
	}
	
	public void extractImages() {
		try {
			Editor.getInstance().getData().setExtractedImages(getImagesFromPDF(Editor.getInstance().getConverter().getDocument()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<RenderedImage> getImagesFromPDF(PDDocument document) throws IOException {
        ArrayList<RenderedImage> images = new ArrayList<>();
        for (PDPage page : document.getPages()) {
        	images.addAll(getImagesFromResources(page.getResources()));
        }
        return images;
	}

	private ArrayList<RenderedImage> getImagesFromResources(PDResources resources) throws IOException {
	    ArrayList<RenderedImage> images = new ArrayList<>();
	    for (COSName xObjectName : resources.getXObjectNames()) {
	        PDXObject xObject = resources.getXObject(xObjectName);
		        if (xObject instanceof PDFormXObject) {
		            images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
		        } else if (xObject instanceof PDImageXObject) {
		            images.add(((PDImageXObject) xObject).getImage());
		        }
	    }
	    return images;
	}
	
	public BufferedImage convertRenderedImage(RenderedImage img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage)img;	
		}	
		ColorModel cm = img.getColorModel();
		int width = img.getWidth();
		int height = img.getHeight();
		WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		Hashtable properties = new Hashtable();
		String[] keys = img.getPropertyNames();
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				properties.put(keys[i], img.getProperty(keys[i]));
			}
		}
		BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
		img.copyData(raster);
		return result;
	}
	
}
