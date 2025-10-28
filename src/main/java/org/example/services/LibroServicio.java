package org.example.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.example.model.Libro;
import org.example.model.Producto;
import org.example.model.enums.CategoriaProducto;

public class LibroServicio {

  private final List<Libro> libros = new ArrayList<>();
  private final Random random = new Random();

  public void agregarLibro(Libro libro) {
    libros.add(libro);
  }

  public List<Libro> obtenerLibros() {
    return Collections.unmodifiableList(libros);
  }

  public List<Producto> obtenerProductoConPrecioMayorACien() {
    return libros.stream()
        .filter(producto -> producto.getPrecio() > 100)
        .sorted(Comparator.comparing(Producto::getPrecio).reversed())
        .collect(Collectors.toList());
  }

  public Map<CategoriaProducto, Integer> obtenerStockPorCategoria() {
    return libros.stream()
        .collect(Collectors.groupingBy(
            Producto::getCategoria,
            Collectors.summingInt(Producto::getStock)
        ));
  }

  public String obtenerProductosConPrecio() {
    return libros.stream()
        .map(p -> p.getNombre() + " = " + String.format("%.2f", p.getPrecio()))
        .collect(Collectors.joining("\n"));
  }

  public Map<String, String> calcularPrecioPromedioGeneralYPorCategoria() {
    Map<String, String> resultado = new HashMap<>();

    double promedioGeneral = libros.stream()
        .mapToDouble(Producto::getPrecio)
        .average()
        .orElse(0.0);

    resultado.put("GENERAL", String.format("%.2f", promedioGeneral));

    Map<CategoriaProducto, Double> promedioPorCategoria = libros.stream()
        .collect(Collectors.groupingBy(
            Producto::getCategoria,
            Collectors.averagingDouble(Producto::getPrecio)
        ));

    promedioPorCategoria.forEach((categoria, promedio) -> {
          String promedioString = String.format("%.2f", promedio);
          resultado.put(categoria.toString(), promedioString);
        }
    );

    return resultado;
  }

  public void generarProductosRandom() {
    String[][] nombresPorCategoria = {
        {"Detergente", "Lavandina", "Limpiador", "Esponja", "Trapo"},
        {"Papel Higiénico", "Servilletas", "Bolsas", "Aluminio", "Film"},
        {"Chocolate", "Caramelos", "Chicles", "Alfajor", "Galletitas"},
        {"Coca Cola", "Sprite", "Agua", "Jugo", "Gaseosa"},
        {"Cerveza", "Vino", "Fernet", "Whisky", "Vodka"},
        {"Jamón", "Queso", "Salame", "Mortadela", "Salchichas"}
    };

    CategoriaProducto[] categorias = CategoriaProducto.values();

    for (int i = 0; i < 16; i++) {
      CategoriaProducto categoria = categorias[random.nextInt(categorias.length)];
      int indexCategoria = categoria.ordinal();

      String codigo = "PROD-" + String.format("%04d", i + 1);
      String nombre = nombresPorCategoria[indexCategoria][random.nextInt(
          nombresPorCategoria[indexCategoria].length)];
      double precio = 50 + (random.nextDouble() * 950);
      int stock = random.nextInt(100) + 1;

      libros.add(Producto.builder()
          .codigo(codigo)
          .nombre(nombre)
          .categoria(categoria)
          .precio(precio)
          .stock(stock)
          .build());
    }

  }
}
