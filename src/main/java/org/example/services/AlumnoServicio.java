package org.example.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import org.example.model.Alumno;
import org.example.model.enums.Curso;

public class AlumnoServicio {

  private final List<Alumno> alumnos = new ArrayList<>();
  private final Random random = new Random();

  public void agregarAlumno(Alumno alumno) {
    alumnos.add(alumno);
  }

  public List<Alumno> obtenerTodos() {
    return Collections.unmodifiableList(alumnos);
  }

  public List<Alumno> obtenerAlumnosConNotaMayorOIgualASiete() {
    return alumnos.stream()
        .filter(alumno -> alumno.getNota() >= 7)
        .map((alumno) -> Alumno.builder()
            .legajo(alumno.getLegajo())
            .curso(alumno.getCurso())
            .nota(alumno.getNota())
            .nombre(alumno.getNombre().toUpperCase())
            .build())
        .sorted(Comparator.comparing(Alumno::getNombre))
        .collect(Collectors.toList());
  }

  public Map<Curso, List<Alumno>> agruparPorCurso() {
    return alumnos.stream()
        .collect(Collectors.groupingBy(
            Alumno::getCurso)
        );
  }

  public Double calcularPromedioGeneral() {
    return alumnos.stream()
        .collect(Collectors.averagingInt(Alumno::getNota));
  }

  public List<Alumno> obtenerTresMejores() {
    return alumnos.stream()
        .sorted(Comparator.comparingInt(Alumno::getNota).reversed())
        .limit(3)
        .collect(Collectors.toList());
  }

  public void crearListaRandom() {
    alumnos.clear();

    String[] nombres = {
        "Lucía", "Matías", "Sofía", "Tomás", "Valentina",
        "Lautaro", "Camila", "Franco", "Agustina", "Julián",
        "Martina", "Nicolás", "Bianca", "Federico", "Emilia",
        "Bautista", "Renata", "Facundo", "Mía", "Santiago"
    };

    Curso[] cursos = Curso.values();

    for (int i = 0; i < 15; i++) {
      String nombre = nombres[random.nextInt(nombres.length)];
      int nota = random.nextInt(11);
      Curso curso = cursos[random.nextInt(cursos.length)];
      String legajo = generarLegajo();

      Alumno alumno = Alumno.builder()
          .legajo(legajo)
          .nombre(nombre)
          .nota(nota)
          .curso(curso)
          .build();
      alumnos.add(alumno);
    }
  }

  private String generarLegajo() {
    String id = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    return "A-" + id;
  }
}
