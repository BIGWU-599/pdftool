package test.coverter.gui.startup;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import test.coverter.controller.Startup;

public class StartUpWizard extends JFrame {

	private static StartUpWizard instance = null;
	public static StartUpWizard getInstance() {
		if (instance == null) instance = new StartUpWizard();
		return instance;
	}
	
	private StartUpWizard() {
		addWindowListener(Startup.getWindowAdapter());
		setTitle("PDFC Start Up Wizard");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(1000,300);
		initUI();
		setVisible(true);
	}
	
	private String[] buttons = {"Open PDFC Editor", "Simple PDF Conversion", "Preferences", "Exit" };
	
	private void initUI() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(10,10,10,10));
		buttonPanel.setLayout(new GridLayout(1,0));
		for (String btnText : buttons) {
			JButton btn = new JButton(btnText);
			btn.addActionListener(e -> {
				Startup.routeAction(btnText);
			});
			buttonPanel.add(btn);
		}
		add(buttonPanel, BorderLayout.CENTER);
	}
	
}
