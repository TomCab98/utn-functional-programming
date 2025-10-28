package org.example.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import org.example.model.Empleado;

public class EmpleadoServicio {

  private final List<Empleado> empleados = new ArrayList<>();
  private final Random random = new Random();

  public void agregarEmpleado(Empleado alumno) {
    empleados.add(alumno);
  }

  public List<Empleado> obtenerTodos() {
    return Collections.unmodifiableList(empleados);
  }

  public List<Empleado> obtenerEmpleadosConSalarioMayorA2000() {
    return empleados.stream()
        .filter(e -> e.getSalario() > 2000)
        .sorted(Comparator.comparingDouble(Empleado::getSalario).reversed())
        .collect(Collectors.toList());
  }

  public double calcularSalarioPromedioGeneral() {
    return empleados.stream()
        .collect(Collectors.averagingDouble(Empleado::getSalario));
  }

  public Map<String, Double> agruparPorDepartamentoYSumarSalarios() {
    return empleados.stream()
        .collect(Collectors.groupingBy(
            Empleado::getDepartamento,
            Collectors.summingDouble(Empleado::getSalario)
        ));
  }

  public List<String> obtenerDosEmpleadosMasJovenes() {
    return empleados.stream()
        .sorted(Comparator.comparingInt(Empleado::getEdad))
        .limit(2)
        .map(Empleado::getNombre)
        .collect(Collectors.toList());
  }

  public void crearListaRandom() {
    String[] nombres = {
        "Lucía", "Matías", "Sofía", "Tomás", "Valentina",
        "Lautaro", "Camila", "Franco", "Agustina", "Julián",
        "Martina", "Nicolás", "Bianca", "Federico", "Emilia",
        "Bautista", "Renata", "Facundo", "Mía", "Santiago"
    };

    String[] departamentos = {
        "Recursos Humanos", "Desarrollo", "Ventas",
        "Marketing", "Administración", "Soporte Técnico"
    };

    for (int i = 0; i < 15; i++) {
      String legajo = generarLegajo();
      String nombre = nombres[random.nextInt(nombres.length)];
      String departamento = departamentos[random.nextInt(departamentos.length)];
      double salario = 1500 + (random.nextDouble() * 3500);
      int edad = 20 + random.nextInt(30);

      Empleado empleado = Empleado.builder()
          .legajo(legajo)
          .nombre(nombre)
          .departamento(departamento)
          .salario(Math.round(salario * 100.0) / 100.0)
          .edad(edad)
          .build();

      empleados.add(empleado);
    }
  }

  private String generarLegajo() {
    String id = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    return "E-" + id;
  }
}
