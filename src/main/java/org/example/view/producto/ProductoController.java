package org.example.view.producto;

import java.util.Map;
import java.util.Objects;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.example.model.Producto;
import org.example.model.enums.CategoriaProducto;
import org.example.services.ProductoServicio;

public class ProductoController {

  private final ProductoServicio productoServicio = new ProductoServicio();
  private final ObservableList<Producto> listaObservable = FXCollections.observableArrayList();
  @FXML
  public Label infoEstadisticas;
  @FXML
  private TableView<Producto> tablaProductos;
  @FXML
  private TableView<DTOCategoriaStock> tablaPromedios = new TableView<>();
  @FXML
  private AnchorPane contenedorTablas;
  @FXML
  private TableColumn<Producto, String> colCodigo;
  @FXML
  private TableColumn<Producto, String> colNombre;
  @FXML
  private TableColumn<Producto, String> colPrecio;
  @FXML
  private TableColumn<Producto, Integer> colStock;
  @FXML
  private TableColumn<Producto, CategoriaProducto> colCategoria;
  @FXML
  private TextField txtCodigo;
  @FXML
  private TextField txtNombre;
  @FXML
  private TextField txtPrecio;
  @FXML
  private TextField txtStock;
  @FXML
  private ComboBox<CategoriaProducto> comboCategoria;

  @FXML
  public void initialize() {
    colCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().getCodigo()));
    colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().getNombre()));
    colPrecio.setCellValueFactory(data -> {
      double precio = data.getValue().getPrecio();
      return new SimpleStringProperty(String.format("%.2f", precio));
    });
    colStock.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
        cellData.getValue().getStock()));
    colCategoria.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
        cellData.getValue().getCategoria()));

    comboCategoria.setItems(FXCollections.observableArrayList(CategoriaProducto.values()));
    tablaProductos.setItems(listaObservable);

    TableColumn<DTOCategoriaStock, String> colCategoria = new TableColumn<>("Categoría");
    colCategoria.setCellValueFactory(data ->
        new SimpleStringProperty(data.getValue().getCategoria())
    );
    colCategoria.prefWidthProperty().bind(tablaPromedios.widthProperty().multiply(0.6));

    TableColumn<DTOCategoriaStock, String> colStock = new TableColumn<>("Stock");
    colStock.setCellValueFactory(data ->
        new SimpleStringProperty(data.getValue().getStock())
    );
    colStock.prefWidthProperty().bind(tablaPromedios.widthProperty().multiply(0.4));

    tablaPromedios.getColumns().addAll(colCategoria, colStock);
    tablaPromedios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    tablaPromedios.setPrefHeight(tablaProductos.getHeight());
    tablaPromedios.setPrefWidth(tablaProductos.getWidth());

    AnchorPane.setTopAnchor(tablaPromedios, AnchorPane.getTopAnchor(tablaProductos));
    AnchorPane.setBottomAnchor(tablaPromedios, AnchorPane.getBottomAnchor(tablaProductos));
    AnchorPane.setLeftAnchor(tablaPromedios, AnchorPane.getLeftAnchor(tablaProductos));
    AnchorPane.setRightAnchor(tablaPromedios, AnchorPane.getRightAnchor(tablaProductos));

    contenedorTablas.getChildren().add(tablaPromedios);
    tablaPromedios.setVisible(false);
  }

  @FXML
  private void onAgregarProducto() {
    try {
      String codigo = txtCodigo.getText().trim();
      String nombre = txtNombre.getText().trim();
      double precio = Double.parseDouble(txtPrecio.getText().trim());
      int stock = Integer.parseInt(txtStock.getText().trim());
      CategoriaProducto categoria = comboCategoria.getValue();

      if (codigo.isEmpty() || nombre.isEmpty() || categoria == null) {
        mostrarAlerta("Campos incompletos", "Por favor complete todos los campos.",
            AlertType.WARNING);
        return;
      } else if (stock < 0 || stock > 100) {
        mostrarAlerta("Stock incorrecta", "El stock debe estar entre 0 y 100.", AlertType.ERROR);
        return;
      }

      Producto nuevoProducto = Producto.builder()
          .codigo(codigo)
          .nombre(nombre)
          .precio(precio)
          .stock(stock)
          .categoria(categoria)
          .build();
      productoServicio.agregarProducto(nuevoProducto);
      listaObservable.add(nuevoProducto);

      limpiarFormulario();

    } catch (NumberFormatException e) {
      mostrarAlerta("Error de formato", "El stock debe ser un número entero.", AlertType.WARNING);
    }
  }

  @FXML
  private void onFiltrarMayorACien() {
    listaObservable.setAll(productoServicio.obtenerProductoConPrecioMayorACien());
  }

  @FXML
  private void onMostrarStockPorCategoria() {
    Map<CategoriaProducto, Integer> promedios = productoServicio.obtenerStockPorCategoria();

    ObservableList<DTOCategoriaStock> listaPromedios = FXCollections.observableArrayList(
        promedios.entrySet().stream()
            .map(e -> new DTOCategoriaStock(e.getKey().toString(), e.getValue().toString()))
            .toList()
    );

    tablaPromedios.getColumns().stream()
        .filter(col -> Objects.equals(col.getText(), "Promedio"))
        .findFirst()
        .ifPresent(col -> col.setText("Stock"));

    if (tablaProductos.isVisible()) {
      tablaProductos.setVisible(false);
      tablaPromedios.setVisible(true);
    }

    tablaPromedios.setItems(listaPromedios);
  }

  @FXML
  private void onConsultarProductosYPrecios() {
    String promedioGeneral = productoServicio.obtenerProductosConPrecio();
    infoEstadisticas.setText(promedioGeneral);
  }

  @FXML
  private void onMostrarPrecioPromedioPorCategoria() {
    Map<String, String> promedios = productoServicio.calcularPrecioPromedioGeneralYPorCategoria();

    ObservableList<DTOCategoriaStock> listaPromedios = FXCollections.observableArrayList(
        promedios.entrySet().stream()
            .map(e -> new DTOCategoriaStock(e.getKey(), e.getValue()))
            .toList()
    );

    tablaPromedios.getColumns().stream()
        .filter(col -> Objects.equals(col.getText(), "Stock"))
        .findFirst()
        .ifPresent(col -> col.setText("Promedio"));

    if (tablaProductos.isVisible()) {
      tablaProductos.setVisible(false);
      tablaPromedios.setVisible(true);
    }

    tablaPromedios.setItems(listaPromedios);
  }

  @FXML
  private void onMostrarTodos() {
    if (!tablaProductos.isVisible()) {
      tablaPromedios.setVisible(false);
      tablaProductos.setVisible(true);
    }

    listaObservable.setAll(productoServicio.obtenerProductos());
  }

  @FXML
  private void onGenerarListaRandom() {
    productoServicio.generarProductosRandom();
    listaObservable.setAll(productoServicio.obtenerProductos());
  }

  private void limpiarFormulario() {
    txtCodigo.clear();
    txtNombre.clear();
    txtPrecio.clear();
    txtStock.clear();
    comboCategoria.getSelectionModel().clearSelection();
  }

  private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
    Alert alerta = new Alert(tipo);
    alerta.setTitle(titulo);
    alerta.setHeaderText(null);
    alerta.setContentText(mensaje);
    alerta.showAndWait();
  }
}
