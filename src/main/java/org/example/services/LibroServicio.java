package org.example.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import org.example.model.Libro;

public class LibroServicio {

  private final List<Libro> libros = new ArrayList<>();
  private final Random random = new Random();

  public void agregarLibro(Libro libro) {
    libros.add(libro);
  }

  public List<Libro> obtenerLibros() {
    return Collections.unmodifiableList(libros);
  }

  public List<String> listarTitulosConMasDe300Paginas() {
    return libros.stream()
        .filter(libro -> libro.getPaginas() > 300)
        .map(Libro::getTitulo)
        .sorted(String::compareToIgnoreCase)
        .collect(Collectors.toList());
  }

  public double calcularPromedioPaginas() {
    return libros.stream()
        .mapToInt(Libro::getPaginas)
        .average()
        .orElse(0.0);
  }

  public Map<String, Long> contarLibrosPorAutor() {
    return libros.stream()
        .collect(Collectors.groupingBy(
            Libro::getAutor,
            Collectors.counting()
        ));
  }

  public Optional<Libro> obtenerLibroMasCaro() {
    return libros.stream()
        .max(Comparator.comparingDouble(Libro::getPrecio));
  }

  public List<Libro> crearLibrosAleatorios() {

    List<String> AUTORES = List.of(
        "Julio M. Ortega", "Lucía Fernández", "Carlos Pérez",
        "Marina Gutiérrez", "Esteban Rivas", "Claudia Torres",
        "Gabriel Sánchez", "Isabel Méndez", "Tomás Quiroga", "Ana Beltrán"
    );

    List<String> TITULOS = List.of(
        "El código del destino", "Sombras del tiempo", "La ecuación del alma",
        "Caminos de fuego", "Los secretos del viento", "La paradoja del olvido",
        "Horizontes de plata", "El último algoritmo", "Crónicas del infinito",
        "El lenguaje de los sueños"
    );

    for (int i = 0; i < 16; i++) {
      String codigo = "LIB" + String.format("%04d", i + 1);
      String titulo = TITULOS.get(random.nextInt(TITULOS.size()));
      String autor = AUTORES.get(random.nextInt(AUTORES.size()));
      int paginas = random.nextInt(120, 800);
      double precio = Math.round((random.nextDouble(2000, 15000)) * 100.0) / 100.0;

      Libro libro = Libro.builder()
          .codigo(codigo)
          .titulo(titulo)
          .autor(autor)
          .paginas(paginas)
          .precio(precio)
          .build();

      libros.add(libro);
    }

    return libros;
  }
}
