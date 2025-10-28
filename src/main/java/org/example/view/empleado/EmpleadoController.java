package org.example.view.empleado;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.example.model.Empleado;
import org.example.model.Producto;
import org.example.model.enums.CategoriaProducto;
import org.example.services.EmpleadoServicio;
import org.example.view.producto.DTOCategoriaStock;

public class EmpleadoController {

  private final EmpleadoServicio empleadoServicio = new EmpleadoServicio();
  private final ObservableList<Empleado> listaObservable = FXCollections.observableArrayList();

  @FXML
  public Label infoEstadisticas;

  @FXML
  private TableView<Empleado> tablaEmpleados;

  @FXML
  private TableView<DTODepartamentoSalarios> tablaPromedios = new TableView<>();

  @FXML
  private AnchorPane contenedorTablas;

  @FXML
  private TableColumn<Empleado, String> colLegajo;

  @FXML
  private TableColumn<Empleado, String> colNombre;

  @FXML
  private TableColumn<Empleado, String> colDepartamento;

  @FXML
  private TableColumn<Empleado, String> colSalario;

  @FXML
  private TableColumn<Empleado, Integer> colEdad;

  @FXML
  private TextField txtLegajo;

  @FXML
  private TextField txtNombre;

  @FXML
  private TextField txtDepartamento;

  @FXML
  private TextField txtSalario;

  @FXML
  private TextField txtEdad;

  @FXML
  public void initialize() {
    colLegajo.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().getLegajo()));
    colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().getNombre()));
    colDepartamento.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().getDepartamento()));
    colSalario.setCellValueFactory(data -> {
      double precio = data.getValue().getSalario();
      return new SimpleStringProperty(String.format("%.2f", precio));
    });
    colEdad.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
        cellData.getValue().getEdad()));

    tablaEmpleados.setItems(listaObservable);

    TableColumn<DTODepartamentoSalarios, String> colDepartamento = new TableColumn<>("Departamento");
    colDepartamento.setCellValueFactory(data ->
        new SimpleStringProperty(data.getValue().getDepartamento())
    );
    colDepartamento.prefWidthProperty().bind(tablaPromedios.widthProperty().multiply(0.6));

    TableColumn<DTODepartamentoSalarios, String> colSalaario = new TableColumn<>("Salario");
    colSalaario.setCellValueFactory(data ->
        new SimpleStringProperty(data.getValue().getSalario())
    );
    colSalaario.prefWidthProperty().bind(tablaPromedios.widthProperty().multiply(0.4));

    tablaPromedios.getColumns().addAll(colDepartamento, colSalaario);
    tablaPromedios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    tablaPromedios.setPrefHeight(tablaEmpleados.getHeight());
    tablaPromedios.setPrefWidth(tablaEmpleados.getWidth());

    AnchorPane.setTopAnchor(tablaPromedios, AnchorPane.getTopAnchor(tablaEmpleados));
    AnchorPane.setBottomAnchor(tablaPromedios, AnchorPane.getBottomAnchor(tablaEmpleados));
    AnchorPane.setLeftAnchor(tablaPromedios, AnchorPane.getLeftAnchor(tablaEmpleados));
    AnchorPane.setRightAnchor(tablaPromedios, AnchorPane.getRightAnchor(tablaEmpleados));

    contenedorTablas.getChildren().add(tablaPromedios);
    tablaPromedios.setVisible(false);
  }

  @FXML
  private void onAgregarProducto() {
    try {
      String legajo = txtLegajo.getText().trim();
      String nombre = txtNombre.getText().trim();
      String departamento = txtDepartamento.getText().trim();
      double salario = Double.parseDouble(txtSalario.getText().trim());
      int edad = Integer.parseInt(txtEdad.getText().trim());

      if (legajo.isEmpty() || nombre.isEmpty() || departamento.isEmpty()) {
        mostrarAlerta("Campos incompletos", "Por favor complete todos los campos.",
            AlertType.WARNING);
        return;
      } else if (edad < 0 || edad > 60) {
        mostrarAlerta("Stock incorrecta", "El edad debe estar entre 0 y 60.", AlertType.ERROR);
        return;
      }

      Empleado nuevoEmpleado = Empleado.builder()
          .legajo(legajo)
          .nombre(nombre)
          .departamento(departamento)
          .salario(salario)
          .edad(edad)
          .build();

      empleadoServicio.agregarEmpleado(nuevoEmpleado);
      listaObservable.add(nuevoEmpleado);

      limpiarFormulario();

    } catch (NumberFormatException e) {
      mostrarAlerta("Error de formato", "El stock debe ser un número entero.", AlertType.WARNING);
    }
  }

  @FXML
  private void onObtenerEmpleadosConSalarioMayorA2000() {
    listaObservable.setAll(empleadoServicio.obtenerEmpleadosConSalarioMayorA2000());
  }

  @FXML
  private void onCalcularSalarioPromedioGeneral() {
    double promedioGeneral = empleadoServicio.calcularSalarioPromedioGeneral();
    infoEstadisticas.setText(String.format("%.2f", promedioGeneral));
  }

  @FXML
  private void onAgruparPorDepartamentoYSumarSalarios() {
    Map<String, Double> filtradoDepartamentos = empleadoServicio.agruparPorDepartamentoYSumarSalarios();
    ObservableList<DTODepartamentoSalarios> listaPromedios = FXCollections.observableArrayList(
        filtradoDepartamentos.entrySet().stream()
            .map(e -> new DTODepartamentoSalarios(e.getKey(), String.format("%.2f", e.getValue())))
            .toList()
    );

    if (tablaEmpleados.isVisible()) {
      tablaEmpleados.setVisible(false);
      tablaPromedios.setVisible(true);
    }

    tablaPromedios.setItems(listaPromedios);
  }

  @FXML
  private void onObtenerDosEmpleadosMasJovenes() {
    List<String> promedios = empleadoServicio.obtenerDosEmpleadosMasJovenes();
    infoEstadisticas.setText(String.join("\n", promedios));
  }

  @FXML
  private void onMostrarTodos() {
    if (!tablaEmpleados.isVisible()) {
      tablaPromedios.setVisible(false);
      tablaEmpleados.setVisible(true);
    }

    listaObservable.setAll(empleadoServicio.obtenerTodos());
  }

  @FXML
  private void onGenerarListaRandom() {
    empleadoServicio.crearListaRandom();
    listaObservable.setAll(empleadoServicio.obtenerTodos());
  }

  private void limpiarFormulario() {
    txtLegajo.clear();
    txtNombre.clear();
    txtDepartamento.clear();
    txtSalario.clear();
    txtEdad.clear();
  }

  private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
    Alert alerta = new Alert(tipo);
    alerta.setTitle(titulo);
    alerta.setHeaderText(null);
    alerta.setContentText(mensaje);
    alerta.showAndWait();
  }
}
