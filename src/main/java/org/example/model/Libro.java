package org.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Libro {
  private final String codigo;
  private String titulo;
  private String autor;
  private int paginas;
  private double precio;
}
