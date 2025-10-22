package org.example.view.alumno;

import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.model.Alumno;
import org.example.model.enums.Curso;
import org.example.services.AlumnoServicio;

public class AlumnoController {

  private final AlumnoServicio alumnoServicio = new AlumnoServicio();
  private final ObservableList<Alumno> listaObservable = FXCollections.observableArrayList();

  @FXML
  private TableView<Alumno> tablaAlumnos;

  @FXML
  private TableColumn<Alumno, String> colLegajo;

  @FXML
  private TableColumn<Alumno, String> colNombre;

  @FXML
  private TableColumn<Alumno, Integer> colNota;

  @FXML
  private TableColumn<Alumno, Curso> colCurso;

  @FXML
  private TextField txtLegajo;

  @FXML
  private TextField txtNombre;

  @FXML
  private TextField txtNota;

  @FXML
  private ComboBox<Curso> comboCurso;

  @FXML
  public void initialize() {
    colLegajo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
        cellData.getValue().getLegajo()));
    colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
        cellData.getValue().getNombre()));
    colNota.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(
        cellData.getValue().getNota()));
    colCurso.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(
        cellData.getValue().getCurso()));

    comboCurso.setItems(FXCollections.observableArrayList(Curso.values()));
    tablaAlumnos.setItems(listaObservable);
  }

  @FXML
  private void onAgregarAlumno(ActionEvent event) {
    try {
      String legajo = txtLegajo.getText().trim();
      String nombre = txtNombre.getText().trim();
      int nota = Integer.parseInt(txtNota.getText().trim());
      Curso curso = comboCurso.getValue();

      if (legajo.isEmpty() || nombre.isEmpty() || curso == null) {
        mostrarAlerta("Campos incompletos", "Por favor complete todos los campos.", AlertType.WARNING);
        return;
      } else if (nota < 0 || nota > 10) {
        mostrarAlerta("Nota incorrecta", "La nota debe estar entre 0 y 10.", AlertType.ERROR);
        return;
      }

      Alumno nuevoAlumno = Alumno.builder()
          .legajo(legajo)
          .nombre(nombre)
          .nota(nota)
          .curso(curso)
          .build();
      alumnoServicio.agregarAlumno(nuevoAlumno);
      listaObservable.add(nuevoAlumno);

      limpiarFormulario();

    } catch (NumberFormatException e) {
      mostrarAlerta("Error de formato", "La nota debe ser un número entero.", AlertType.WARNING);
    }
  }

  @FXML
  private void onFiltrarAprobados(ActionEvent event) {
    listaObservable.setAll(alumnoServicio.obtenerAlumnosConNotaMayorOIgualASiete());
  }

  @FXML
  private void onPromedioGeneral() {
    Double promedio = alumnoServicio.calcularPromedioGeneral();
    mostrarAlerta("PROMEDIO GENERAL", "El promedio general de los alumnos es: " + promedio, AlertType.INFORMATION);
  }

  @FXML
  private void onAgruparPorCurso(ActionEvent event) {
    Map<Curso, List<Alumno>> alumnosPorCurso = alumnoServicio.agruparPorCurso();

    List<Alumno> alumnosOrdenados = alumnosPorCurso.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .flatMap(entry -> entry.getValue().stream())
        .toList();

    listaObservable.setAll(alumnosOrdenados);
  }

  @FXML
  private void onMostrarTresMejores() {
    listaObservable.setAll(alumnoServicio.obtenerTresMejores());
  }

  @FXML
  private void onMostrarTodos(ActionEvent event) {
    listaObservable.setAll(alumnoServicio.obtenerTodos());
  }

  @FXML
  private void onGenerarListaRandom() {
    alumnoServicio.crearListaRandom();
    listaObservable.setAll(alumnoServicio.obtenerTodos());
  }

  private void limpiarFormulario() {
    txtLegajo.clear();
    txtNombre.clear();
    txtNota.clear();
    comboCurso.getSelectionModel().clearSelection();
  }

  private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
    Alert alerta = new Alert(tipo);
    alerta.setTitle(titulo);
    alerta.setHeaderText(null);
    alerta.setContentText(mensaje);
    alerta.showAndWait();
  }
}
