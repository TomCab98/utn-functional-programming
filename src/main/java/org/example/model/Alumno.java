package org.example.model;

import lombok.Builder;
import lombok.Data;
import org.example.model.enums.Curso;

@Data
@Builder
public class Alumno {
  private final String legajo;
  private final String nombre;
  private final int nota;
  private final Curso curso;
}
