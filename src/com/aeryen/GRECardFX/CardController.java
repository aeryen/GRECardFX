package com.aeryen.GRECardFX;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.text.Font;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardController {

	public Label detail;
	public Button option1;
	public Button option2;
	public Button option3;
	public Button option4;

	public int correctAnswer = -1;

	CardManager cm;

	public void setCardManager(CardManager cm) {
		this.cm = cm;
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

	public void wordEnglishToChineseTest(Word mainWord, Word alt1, Word alt2, Word alt3) {
		this.detail.setText(mainWord.english);
		ArrayList<String> options = new ArrayList<>(4);
		options.add(mainWord.chinese);
		options.add(alt1.chinese);
		options.add(alt2.chinese);
		options.add(alt3.chinese);

		Collections.shuffle(options);
		correctAnswer = options.indexOf(mainWord.chinese);

		this.detail.setText(mainWord.english);
		setButton(options);
	}

	public void handleAnswer(ActionEvent event) {
		Button sourceButton = (Button) event.getSource();
		int buttonNumber = whichButton(sourceButton);
		if (buttonNumber == correctAnswer) {
			//get back to card manager
			System.out.println("Correct.");

			resetButtonStyle();

			cm.continueTest();
		} else {
			//no, wrong answer
			System.out.println("Wrong.");

			sourceButton.setStyle("-fx-background-color: darkred");
		}
	}
}
