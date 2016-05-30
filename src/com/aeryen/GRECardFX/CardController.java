package com.aeryen.GRECardFX;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;

public class CardController {

	public Label detail;
	public Button option1;
	public Button option2;
	public Button option3;
	public Button option4;

	public Label timeLabel;

	public Button startNow;

	public Word correctWord = null;
	public int correctIndex = -1;

	CardManager cm;
	TimerManager ts;

	public void setCardManager(CardManager cm) {
		this.cm = cm;
	}

	public void setTimerManager(TimerManager ts) {
		this.ts = ts;
	}

	public void handleStartNow(ActionEvent event) {
		ts.finishCountDown();
	}

	private int whichButton(Button b) {
		if (b == option1) {
			return 0;
		} else if (b == option2) {
			return 1;
		} else if (b == option3) {
			return 2;
		} else if (b == option4) {
			return 3;
		} else {
			throw new RuntimeException("unexpected button?");
		}
	}

	private void setButton(ArrayList<String> options) {
		this.option1.setText(options.get(0));
		this.option2.setText(options.get(1));
		this.option3.setText(options.get(2));
		this.option4.setText(options.get(3));
	}

	public void resetButtonStyle() {
		option1.setStyle("-fx-background-color: inherit");
		option2.setStyle("-fx-background-color: inherit");
		option3.setStyle("-fx-background-color: inherit");
		option4.setStyle("-fx-background-color: inherit");
	}

	boolean madeMistake = false;
	public void wordEnglishToChineseTest(Word mainWord, Word alt1, Word alt2, Word alt3) {
		correctWord = mainWord;

		this.detail.setText(mainWord.english);
		ArrayList<String> options = new ArrayList<>(4);
		options.add(mainWord.chinese);
		options.add(alt1.chinese);
		options.add(alt2.chinese);
		options.add(alt3.chinese);

		Collections.shuffle(options);
		correctIndex = options.indexOf(mainWord.chinese);

		this.detail.setText(mainWord.english);
		setButton(options);

		madeMistake = false;
	}

	public void delayContinue() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
			resetButtonStyle();
			cm.continueTest();
		}));
		timeline.setCycleCount(1);
		timeline.play();
	}

	public void handleAnswer(ActionEvent event) {
		Button sourceButton = (Button) event.getSource();
		int buttonNumber = whichButton(sourceButton);
		if (buttonNumber == correctIndex) {
			//get back to card manager
			System.out.println("Correct.");
			sourceButton.setStyle("-fx-background-color: darkgreen");

			if (madeMistake) {
				delayContinue();
			} else {
				cm.wordList.putInDone(correctWord);
				resetButtonStyle();
				cm.continueTest();
			}
		} else {
			//no, wrong answer
			System.out.println("Wrong.");
			madeMistake = true;
			sourceButton.setStyle("-fx-background-color: darkred");
		}
	}

	public void setTimerLabel(String text) {
		timeLabel.setText(text);
	}
}
