package edu.cnm.deepdive.rps.view;

import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    ClassLoader classLoader = getClass().getClassLoader();
    ResourceBundle bundle = ResourceBundle.getBundle("resources/ui-strings");
    FXMLLoader fxmlLoader = new FXMLLoader(classLoader.getResource("resources/main.fxml"), bundle);
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root, 620, 760);
    stage.setTitle(bundle.getString("window_title"));
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
  }

}
