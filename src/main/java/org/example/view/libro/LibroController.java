package org.example.view.libro;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
import org.example.model.Libro;
import org.example.services.LibroServicio;

public class LibroController {

  private final LibroServicio libroServicio = new LibroServicio();
  private final ObservableList<Libro> listaObservable = FXCollections.observableArrayList();

  @FXML
  public Label infoEstadisticas;

  @FXML
  private TableView<Libro> tablaLibros;

  @FXML
  private TableColumn<Libro, String> colCodigo;

  @FXML
  private TableColumn<Libro, String> colTitulo;

  @FXML
  private TableColumn<Libro, String> colPrecio;

  @FXML
  private TableColumn<Libro, Integer> colPaginas;

  @FXML
  private TableColumn<Libro, String> colAutor;

  @FXML
  private TextField txtCodigo;

  @FXML
  private TextField txtAutor;

  @FXML
  private TextField txtPrecio;

  @FXML
  private TextField txtPaginas;

  @FXML
  private TextField txtTitulo;

  @FXML
  public void initialize() {
    colCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().getCodigo()));
    colTitulo.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().getTitulo()));
    colPrecio.setCellValueFactory(data -> {
      double precio = data.getValue().getPrecio();
      return new SimpleStringProperty(String.format("%.2f", precio));
    });
    colPaginas.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
        cellData.getValue().getPaginas()));
    colAutor.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().getAutor()));

    tablaLibros.setItems(listaObservable);
  }

  @FXML
  private void onAgregarLibro() {
    try {
      String codigo = txtCodigo.getText().trim();
      String titulo = txtTitulo.getText().trim();
      String autor = txtAutor.getText().trim();
      double precio = Double.parseDouble(txtPrecio.getText().trim());
      int paginas = Integer.parseInt(txtPaginas.getText().trim());

      if (codigo.isEmpty() || titulo.isEmpty() || autor.isEmpty()) {
        mostrarAlerta("Campos incompletos", "Por favor complete todos los campos.",
            AlertType.WARNING);
        return;
      } else if (precio < 0 || paginas > 1000) {
        mostrarAlerta("Stock incorrecta", "El numero de paginas es demasiado grande", AlertType.ERROR);
        return;
      }

      Libro nuevoLibro = Libro.builder()
          .codigo(codigo)
          .autor(autor)
          .precio(precio)
          .paginas(paginas)
          .titulo(titulo)
          .build();

      libroServicio.agregarLibro(nuevoLibro);
      listaObservable.add(nuevoLibro);

      limpiarFormulario();

    } catch (NumberFormatException e) {
      mostrarAlerta("Error de formato", "El stock debe ser un número entero.", AlertType.WARNING);
    }
  }

  @FXML
  private void onListarTitulosConMasDe300Paginas() {
    List<String> promedioGeneral = libroServicio.listarTitulosConMasDe300Paginas();
    infoEstadisticas.setText(String.join(", ", promedioGeneral));
  }

  @FXML
  private void onCalcularPromedioPaginas() {
    double promedios = libroServicio.calcularPromedioPaginas();
    infoEstadisticas.setText(String.format("%.2f", promedios));
  }

  @FXML
  private void onObtenerLibroMasCaro() {
    Optional<Libro> libroMasCaro = libroServicio.obtenerLibroMasCaro();
    libroMasCaro.ifPresent(libro -> listaObservable.setAll(List.of(libro)));
  }

  @FXML
  private void onContarLibrosPorAutor() {
    Map<String, Long> librosPorAutor = libroServicio.contarLibrosPorAutor();

    String resultado = librosPorAutor.entrySet().stream()
        .map(entry -> String.format("%s: %d libro%s",
            entry.getKey(),
            entry.getValue(),
            entry.getValue() > 1 ? "s" : ""))
        .collect(Collectors.joining("\n"));

    infoEstadisticas.setText(resultado);
  }

  @FXML
  private void onMostrarTodos() {
    listaObservable.setAll(libroServicio.obtenerLibros());
  }

  @FXML
  private void onGenerarListaRandom() {
    libroServicio.crearLibrosAleatorios();
    listaObservable.setAll(libroServicio.obtenerLibros());
  }

  private void limpiarFormulario() {
    txtCodigo.clear();
    txtAutor.clear();
    txtPrecio.clear();
    txtPaginas.clear();
    txtTitulo.clear();
  }

  private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
    Alert alerta = new Alert(tipo);
    alerta.setTitle(titulo);
    alerta.setHeaderText(null);
    alerta.setContentText(mensaje);
    alerta.showAndWait();
  }
}
