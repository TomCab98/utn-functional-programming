# Sistema de gestión escolar

## Ejecutar

### 1. Requisitos previos:

- Java 21

### 2. Ejecutar:

> [!IMPORTANT]
> Para ejecutar el proyecto es necesario hacerlo por consola, para que Gradle pueda cargar los plugins de JavaFX

Ejecutamos el siguiente comando:

``` bash
    ./gradlew run
```

Una vez hecho esto se mostrará la ventana de JavaFX con la aplicación. Dividida en ventanas para cada **Caso práctico**

## Caso 1: Alumno

Dentro de la clase [AlumnoServicio](./src/main/java/org/example/services/AlumnoServicio.java) se encontrarán los métodos que realizan las operaciones sobre **Alumno**

Dentro de esta clase se podrán encontrar los métodos:

- [obtenerAlumnosConNotaMayorOIgualASiete()](org.example.services.AlumnoServicio.obtenerAlumnosConNotaMayorOIgualASiete)
- [agruparPorCurso()](org.example.services.AlumnoServicio.agruparPorCurso)
- [calcularPromedioGeneral()](org.example.services.AlumnoServicio.calcularPromedioGeneral)
- [obtenerTresMejores()](org.example.services.AlumnoServicio.obtenerTresMejores)

Cada uno respectivamente cumple las indicaciones del **Caso Práctico 1**

> [!NOTE]
> En la pestaña **ALUMNOS** dentro de la aplicación se podrán ver diferentes formas de interactuar y ver la información
> Entre ellas:
>  - Agregar nuevo alumno
>  - Generar alumnos aleatorios
>  - Ejecutar cualquiera de las funcioanlidades solicitadas
