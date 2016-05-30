package com.aeryen.GRECardFX;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.util.Duration;


import java.io.IOException;
import java.util.Timer;

/**
 * Created by aerye on 5/29/2016.
 */
public class TimerManager {

	final long INTERVAL = 1800; // 1800000

	Stage mainWindowStage = null;
	Scene timerScene = null;
	Pane timerPaneRoot = null;
	CardController controller = null;
	Timeline timeline = null;
	Timer timer;

	private CardManager theCM = null;

	public TimerManager(CardManager cm, Stage stageWindow) {
		theCM = cm;

		this.mainWindowStage = stageWindow;
		mainWindowStage.hide();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("timerScene.fxml"));

			timerPaneRoot = fxmlLoader.load();

			controller = fxmlLoader.getController();

			timerScene = new Scene(timerPaneRoot, 120, 99);

			controller.setTimerManager(this);

			scheduleNextSession();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	long delay = 10;

	public void scheduleNextSession() {
		mainWindowStage.setScene(timerScene);
		mainWindowStage.setTitle("Timer");

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		mainWindowStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 120 - 20);
		mainWindowStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 100 - 40);
		mainWindowStage.show();

		System.out.println("SCHEDULED: " + delay);

		bindToTime();
	}

	private void bindToTime() {

		timeline = new Timeline(
				new KeyFrame(Duration.seconds(0),
						new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent actionEvent) {
								if (delay == 0) {
									finishCountDown();

								} else {
									int minute = (int) Math.floor(delay / 60.0);
									int second = (int) (delay % 60.0);
									delay--;
									controller.setTimerLabel(minute + ":" + second);
								}
							}
						}
				),
				new KeyFrame(Duration.seconds(1))
		);

		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	public void finishCountDown() {
		mainWindowStage.hide();
		cancelTimeLine();
		theCM.startSession();
	}

	public void oneSessionDone() {
		delay = INTERVAL;
		scheduleNextSession();
	}

	public void cancelTimer() {
		System.out.println("canceling timer");
		if (timer != null) {
			System.out.println("timer is not null");
			timer.cancel();
			System.out.println("timer canceled");
		}
	}

	public void cancelTimeLine() {
		System.out.println("canceling timeline");
		if (timeline != null) {
			System.out.println("timeline is not null");
			timeline.stop();
			timeline = null;
			System.out.println("timeline stopped");
		}
	}
}
