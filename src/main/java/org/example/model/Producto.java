package org.example.model;

import lombok.Builder;
import lombok.Data;
import org.example.model.enums.CategoriaProducto;

@Data
@Builder
public class Producto {
  private final String codigo;
  private String nombre;
  private CategoriaProducto categoria;
  private double precio;
  private int stock;
}
