package org.example.view.controller;

import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class MainController {

  @FXML
  private StackPane contenidoCentral;

  @FXML
  private void onMostrarAlumnos() {
    try {
      Node vistaAlumnos = FXMLLoader.load(
          Objects.requireNonNull(getClass().getResource("/fxml/alumno.fxml"))
      );
      contenidoCentral.getChildren().setAll(vistaAlumnos);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
