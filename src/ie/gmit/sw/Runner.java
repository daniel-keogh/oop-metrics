package ie.gmit.sw;

import ie.gmit.sw.ui.AppWindow;
import javafx.application.Application;

/**
 * Entry point to the application.
 */
public class Runner {
	public static void main(String[] args) {
		System.out.println("[INFO] Launching GUI...");

		try {
			Application.launch(AppWindow.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
