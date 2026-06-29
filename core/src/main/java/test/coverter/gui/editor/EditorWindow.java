package test.coverter.gui.editor;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import test.coverter.controller.Startup;

public class EditorWindow extends JFrame {
	
	public EditorWindow() {
		addWindowListener(Startup.getWindowAdapter()); 
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("PDFC Editor");
		setSize(1500,1000);
		initUI();
		setVisible(true);
	}
	
	public EditorMenu editorMenu;
	public EditorWorkbench workbench;
	
	private void initUI() {
		editorMenu = new EditorMenu();
		add(editorMenu, BorderLayout.WEST);
		
		workbench = new EditorWorkbench();
		add(workbench, BorderLayout.CENTER);
		
		revalidate();
	}
	
	public void revalidateAndRepaint() {
		revalidate();
		repaint();
	}
		
}
