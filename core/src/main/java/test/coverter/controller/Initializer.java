package test.coverter.controller;

import test.coverter.gui.startup.StartUpWizard;

import javax.swing.*;
import java.awt.*;

public class Initializer {

	public static void main(String[] args) {

		try {
			UIManager
					.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			EventQueue.invokeLater(() -> {
				try {
					StartUpWizard.getInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
