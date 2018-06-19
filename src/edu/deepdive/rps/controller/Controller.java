package edu.deepdive.rps.controller;

import edu.cnm.deepdive.rps.view.TerrainView;
import edu.deepdive.rps.model.Terrain;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

public class Controller {

  private static final int STEPS_PER_ITERATION = 100;
  private static final long MAX_SLEEP_PER_ITERATION = 10;

  @FXML
  private TerrainView terrainView;
  @FXML
  private ResourceBundle resources;
  @FXML
  private Text iterationsLabel;
  @FXML
  private Button reset;
  @FXML
  private Button stop;
  @FXML
  private Button start;
  @FXML
  private Slider mixingSlider;
  @FXML
  private ScrollPane viewScroller;
  @FXML
  private Slider speedSlider;
  @FXML
  private CheckBox fitCheckbox;

  private double defaultViewHeight;
  private double defaultViewWidth;
  private double fitViewHeight;
  private double fitViewWidth;
  private String iterationFormat;
  private Terrain terrain;
  private boolean running = false;
  private Runner runner = null;
  private final Object lock = new Object();
  private Timer timer;

  @FXML
  private void  initialize() {
  terrain = new Terrain(new Random());
   defaultViewHeight = terrainView.getHeight();
   defaultViewWidth = terrainView.getWidth();
   fitViewWidth = viewScroller.getPrefWidth();
   fitViewHeight = viewScroller.getPrefHeight();
   iterationFormat = iterationsLabel.getText();
   terrainView.setSource(terrain.getCells());
   draw();
   timer = new Timer();
  }

  @FXML
  private void fitView(javafx.event.ActionEvent actionEvent) {
    if (fitCheckbox.isSelected()) {
      terrainView.setWidth(fitViewWidth);
      terrainView.setHeight(fitViewHeight);
    } else {
      terrainView.setWidth(defaultViewWidth);
      terrainView.setHeight(defaultViewHeight);
    }
    if(!running) draw();
  }

  @FXML
  private void start(javafx.event.ActionEvent actionEvent) {
    running = true;
    start.setDisable(true);
    stop.setDisable(false);
    reset.setDisable(true);
    timer.start();
    runner = new Runner();
    runner.start();
  }

  @FXML
  private void stop(javafx.event.ActionEvent actionEvent) {
    running = false;
    runner = null;
    start.setDisable(false);
    stop.setDisable(true);
    reset.setDisable(false);
    timer.stop();
  }

  @FXML
  private void reset(javafx.event.ActionEvent actionEvent) {
    terrain.reset();
    draw();
  }

  private void draw(){
    synchronized (lock) {
      terrainView.draw();
      iterationsLabel.setText(String.format(iterationFormat, terrain.getIterations()));
    }
  }


  private class Timer extends AnimationTimer {

    @Override
    public void handle(long now) {
      draw();
    }
  }

  private class Runner extends Thread {

    @Override
    public void run() {
      while (running) {
        synchronized (lock) {
          terrain.iterate(STEPS_PER_ITERATION);
        }
        try {
          Thread.sleep(1 + MAX_SLEEP_PER_ITERATION - (long) speedSlider.getValue());
        } catch (InterruptedException e) {
          // DO NOTHING
        }
      }
    }
  }
}
