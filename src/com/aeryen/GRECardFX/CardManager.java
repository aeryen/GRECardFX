package com.aeryen.GRECardFX;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aeryen on 4/7/2016.
 */
public class CardManager {

	Stage mainWindowStage = null;

	Scene basicCardScene = null;

	Pane cardPaneRoot = null;

	WordList wordList = null;

	CardController controller = null;

	Timer timer;

	public static final int WINDOW_WIDTH = 450;
	public static final int WINDOW_HEIGHT = 280;

	final long INTERVAL = 1800000;
	public static final int NUMBER_OF_OPTION = 4;
	public static final int WORD_PER_SESSION = 30;

	public CardManager(String listName, Stage stageWindow) {
		Platform.setImplicitExit(false);
		this.mainWindowStage = stageWindow;
		mainWindowStage.hide();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cardScene.fxml"));

			cardPaneRoot = fxmlLoader.load();

			controller = fxmlLoader.getController();
			controller.setCardManager(this);

			basicCardScene = new Scene(cardPaneRoot, WINDOW_WIDTH, WINDOW_HEIGHT);

			mainWindowStage.setScene(basicCardScene);
			mainWindowStage.setTitle("Card");

			initWords(listName);
			scheduleNextSession();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	long delay = 1000;

	private void scheduleNextSession() {
		System.out.println("SCHEDULED: " + delay);
		timer = new Timer();

		timer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(() -> {
					startSession();
				});
			}
		}, delay);
	}

	private void startSession() {
		System.out.println("SESSION STARTED");
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
		delay = INTERVAL;
		scheduleNextSession();
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
		ArrayList<Word> wordSelection = new ArrayList<Word>(4);
		for (int i = 0; i < numberOfWords; i++) {
			int result = random.nextInt(wordList.list.size());
			if (wordSelection.contains(wordList.list.get(result))) {
				numberOfWords++;
			} else {
				wordSelection.add(wordList.list.get(result));
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
