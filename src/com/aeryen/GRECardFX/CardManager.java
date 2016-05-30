package com.aeryen.GRECardFX;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by aeryen on 4/7/2016.
 */
public class CardManager {

	Stage mainWindowStage = null;

	Scene basicCardScene = null;

	Pane cardPaneRoot = null;

	public WordList wordList = null;

	CardController controller = null;

	public static final int WINDOW_WIDTH = 450;
	public static final int WINDOW_HEIGHT = 280;

	public static final int NUMBER_OF_OPTION = 4;
	public static final int WORD_PER_SESSION = 15;

	TimerManager ts = null;

	public CardManager(String listName, Stage stageWindow) {
		this.mainWindowStage = stageWindow;
		mainWindowStage.hide();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cardScene.fxml"));

			cardPaneRoot = fxmlLoader.load();

			controller = fxmlLoader.getController();
			controller.setCardManager(this);

			basicCardScene = new Scene(cardPaneRoot, WINDOW_WIDTH, WINDOW_HEIGHT);

			initWords(listName);

			ts = new TimerManager(this, stageWindow);

			mainWindowStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					ts.cancelTimeLine();
					mainWindowStage.close();
					Platform.exit();
					System.exit(0);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	boolean listCompleted = false;

	public void startSession() {
		System.out.println("SESSION STARTED");

		mainWindowStage.setScene(basicCardScene);
		if(!listCompleted) {
			mainWindowStage.setTitle("Card");
		} else {
			mainWindowStage.setTitle("[List Completed]");
		}

		mainWindowStage.show();

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		mainWindowStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - WINDOW_WIDTH - 20);
		mainWindowStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - WINDOW_HEIGHT - 40);
		mainWindowStage.setAlwaysOnTop(true);

		continueTest();
	}

	private void finishSession() {
		mainWindowStage.hide();
		mainWindowStage.setAlwaysOnTop(false);

		sessionWordCounter = WORD_PER_SESSION;

		ts.oneSessionDone();
	}


	private int sessionWordCounter = WORD_PER_SESSION;

	public void continueTest() {
		if (sessionWordCounter > 0) {
			ArrayList<Word> wordSelection = drawWords(wordList, NUMBER_OF_OPTION);
			controller.wordEnglishToChineseTest(wordSelection.get(0), wordSelection.get(1), wordSelection.get(2), wordSelection.get(3));
			sessionWordCounter--;
			System.out.println("remaining cards this session: " + sessionWordCounter);
		} else {
			finishSession();
		}
	}

	Random random = new Random();

	private ArrayList<Word> drawWords(WordList wordList, int numberOfWords) {
		boolean drawMainWord = true;
		ArrayList<Word> wordSelection = new ArrayList<Word>(4);
		for (int i = 0; i < numberOfWords; i++) {
			boolean haveRemain = wordList.checkRemainTodo();
			if(!haveRemain) {
				listCompleted = true;
				mainWindowStage.setTitle("[List Completed]");
			}
			if(i==0) {  // draw main word
				int result = random.nextInt(wordList.toDoList.size());
				wordSelection.add(wordList.toDoList.get(result));
			}
			else { // draw remain option
				int result = random.nextInt(wordList.CompleteList.size());
				if (wordSelection.contains(wordList.CompleteList.get(result))) { // avoid duplicate
					i--;
				} else {
					wordSelection.add(wordList.CompleteList.get(result));
				}
			}
		}
		return wordSelection;
	}

	private void initWords(String listName) {
		System.out.println(listName);
		try {
			this.wordList = new WordList(new File(Main.INPUTPATH + listName + ".txt"));

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
