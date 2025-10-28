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

  @FXML
  private void onMostrarProductos() {
    try {
      Node vistaProductos = FXMLLoader.load(
          Objects.requireNonNull(getClass().getResource("/fxml/producto.fxml"))
      );
      contenidoCentral.getChildren().setAll(vistaProductos);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onMostrarLibros() {
    try {
      Node vistaLibros = FXMLLoader.load(
          Objects.requireNonNull(getClass().getResource("/fxml/libro.fxml"))
      );
      contenidoCentral.getChildren().setAll(vistaLibros);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onMostrarEmpleados() {
    try {
      Node vistaLibros = FXMLLoader.load(
          Objects.requireNonNull(getClass().getResource("/fxml/empleado.fxml"))
      );
      contenidoCentral.getChildren().setAll(vistaLibros);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
