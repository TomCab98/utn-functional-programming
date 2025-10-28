package org.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Empleado {
  private final String legajo;
  private String nombre;
  private String departamento;
  private double salario;
  private int edad;
}
