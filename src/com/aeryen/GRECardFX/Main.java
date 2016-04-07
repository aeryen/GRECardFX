package com.aeryen.GRECardFX;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;

public class Main extends Application implements EventHandler {

	public static final String INPUTPATH = "." + File.separator + "data" + File.separator + "08red" + File.separator;

	GridPane listSelectionRoot = null;
	Scene listSelectionScene = null;

	public void initializeButtons() {
		File rootFolder = new File(INPUTPATH);
		if (!rootFolder.exists()) {
			System.err.println("CANNOT FIND DATA FOLDER: " + rootFolder.getAbsolutePath());
			return;
		}
		File[] listFiles = rootFolder.listFiles();
		System.out.println("A total of " + listFiles.length + " list files are found.");

		Arrays.sort(listFiles);
		for (int i = 0; i < listFiles.length; i++) {
			addButton(i, listFiles[i].getName().substring(0, 2));
		}
	}

	static final private int BTN_PER_ROW = 5;

	private void addButton(int index, String name) {
		Button btn = new Button();
		btn.setText(name);
		btn.setOnAction(this);
		int row = index / BTN_PER_ROW;
		int column = index % BTN_PER_ROW;
		row = row + 1;
		listSelectionRoot.add(btn, column, row);
	}

	@Override
	public void handle(Event event) {
		String listName = ((Button) event.getSource()).getText();

		System.out.println("selected List: " + listName);
	}

    @Override
    public void start(Stage primaryStage) {

		try {
			listSelectionRoot = FXMLLoader.load(getClass().getResource("listSelection.fxml"));

			listSelectionScene = new Scene(listSelectionRoot, 280, 450);

			primaryStage.setTitle("Hello World!");
			primaryStage.setScene(listSelectionScene);

			initializeButtons();
			primaryStage.setResizable(false);

			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

    }


    public static void main(String[] args) {
        launch(args);
    }


}
