import java.util.*;

class LibretaDeNotas {
    private HashMap<String, ArrayList<Double>> calificaciones;
    private double promedioCurso;

    public LibretaDeNotas() {
        calificaciones = new HashMap<>();
    }

    // Método para cargar datos de estudiantes y notas
    public void cargarDatos() {
        int cantidadAlumnos = obtenerEnteroPositivo("Ingrese la cantidad de alumnos: ");
        int cantidadNotas = obtenerEnteroPositivo("Ingrese la cantidad de notas por alumno: ");

        for (int i = 0; i < cantidadAlumnos; i++) {
            String nombre = obtenerNombre("Ingrese el nombre del alumno " + (i + 1) + ": ");
            ArrayList<Double> notas = new ArrayList<>();

            for (int j = 0; j < cantidadNotas; j++) {
                double nota = obtenerNotaValida("Ingrese la nota " + (j + 1) + " de " + nombre + ": ");
                notas.add(nota);
            }

            calificaciones.put(nombre, notas);
        }

        calcularPromedioCurso();
    }

    // Método para calcular el promedio del curso
    private void calcularPromedioCurso() {
        double sumaTotal = 0;
        int totalNotas = 0;

        for (ArrayList<Double> notas : calificaciones.values()) {
            sumaTotal += notas.stream().mapToDouble(Double::doubleValue).sum();
            totalNotas += notas.size();
        }

        promedioCurso = sumaTotal / totalNotas;
    }

    // Método para mostrar el menú y manejar las opciones
    public void mostrarMenu() {
        int opcion;
        boolean continuar = true;
        do {
            mostrarOpcionesMenu();
            try {
                opcion = obtenerOpcionValida("Seleccione una opción: ", 0, 4);

                switch (opcion) {
                    case 1:
                        mostrarPromedios();
                        break;
                    case 2:
                        mostrarNotasMaximasYMinimas();
                        break;
                    case 3:
                        verificarAprobacion();
                        break;
                    case 4:
                        verificarSobrePromedio();
                        break;
                    case 0:
                        System.out.println("Saliendo del menú...");
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            }catch ( InputMismatchException e ) {
                System.out.println("Opcion no valida.Ingresa un numero entero");
            }
        } while (continuar);
    }

    // Método para mostrar las opciones del menú
    private void mostrarOpcionesMenu() {
        System.out.println("""
        ##############Menú de Opciones###############
        1. Mostrar el Promedio de Notas por Estudiante.
        2. Mostrar la Nota Máxima y Mínima por Estudiante.
        3. Mostrar si la Nota es Aprobatoria o Reprobatoria por Estudiante.
        4. Mostrar si la Nota está por Sobre o por Debajo del Promedio del Curso por Estudiante.
        0. Salir del Menú.
        """);
    }

    // Método para mostrar los promedios de los estudiantes
    private void mostrarPromedios() {
        for (Map.Entry<String, ArrayList<Double>> entry : calificaciones.entrySet()) {
            String nombre = entry.getKey();
            ArrayList<Double> notas = entry.getValue();
            double promedio = calcularPromedio(notas);
            System.out.println("El promedio de " + nombre + " es: " + promedio);
        }
    }

    // Método para calcular el promedio de una lista de notas
    private double calcularPromedio(ArrayList<Double> notas) {
        return notas.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    // Método para mostrar la nota máxima y mínima de cada estudiante
    private void mostrarNotasMaximasYMinimas() {
        for (Map.Entry<String, ArrayList<Double>> entry : calificaciones.entrySet()) {
            String nombre = entry.getKey();
            ArrayList<Double> notas = entry.getValue();
            double notaMaxima = Collections.max(notas);
            double notaMinima = Collections.min(notas);
            System.out.println("Estudiante: " + nombre);
            System.out.println("  Nota máxima: " + notaMaxima);
            System.out.println("  Nota mínima: " + notaMinima);
        }
    }

    // Método para verificar si una nota es aprobatoria o reprobatoria
    private void verificarAprobacion() {
        String nombre = obtenerNombre("Ingrese el nombre del estudiante: ");

        if (calificaciones.containsKey(nombre)) {
            double nota = obtenerNotaValida("Ingrese la nota a verificar: ");
            if (nota >= 4) {
                System.out.println("La nota es Aprobatoria.");
            } else {
                System.out.println("La nota es Reprobatoria.");
            }
        } else {
            System.out.println("El estudiante no existe.");
        }
    }

    // Método para verificar si una nota está por encima o por debajo del promedio del curso
    private void verificarSobrePromedio() {
        String nombre = obtenerNombre("Ingrese el nombre del estudiante: ");

        if (calificaciones.containsKey(nombre)) {
            double nota = obtenerNotaValida("Ingrese la nota a verificar: ");
            if (nota > promedioCurso) {
                System.out.println("La nota está por sobre el promedio del curso.");
            } else if (nota < promedioCurso) {
                System.out.println("La nota está por debajo del promedio del curso.");
            } else {
                System.out.println("La nota es igual al promedio del curso.");
            }
        } else {
            System.out.println("El estudiante no existe.");
        }
    }

    // Métodos reutilizables para entrada de datos y validación

    // Método para obtener un entero positivo
    private int obtenerEnteroPositivo(String mensaje) {
        Scanner scanner = new Scanner(System.in);
        int valor;
        do {
            System.out.print(mensaje);
            valor = scanner.nextInt();
            if (valor <= 0) {
                System.out.println("El valor debe ser un número positivo. Intente nuevamente.");
            }
        } while (valor <= 0);
        return valor;
    }

    // Método para obtener una cadena de texto
    private String obtenerNombre(String mensaje) {
        Scanner scanner = new Scanner(System.in);
        String entrada;
        while(true){
            System.out.print(mensaje);
            entrada = scanner.nextLine();
            if(entrada.matches("[a-zA-z]+")){
                return entrada;
            } else {
                System.out.println("Entrada no valida. Solo se permiten letras.\nIntentalo nuevamente");
            }
        }
    }

    // Método para obtener una nota válida (entre 1 y 7)
    private double obtenerNotaValida(String mensaje) {
        Scanner scanner = new Scanner(System.in);
        double nota;

        while (true) {
            System.out.print(mensaje);
            try {
                String input = scanner.next().replace(",", "."); // Reemplazar coma con punto
                nota = Double.parseDouble(input);

                if (nota >= 1 && nota <= 7) {
                    break; // Salir del bucle si la nota es válida
                } else {
                    System.out.println("Error: La nota debe estar entre 1 y 7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
            }
        }

        return nota;
    }


    // Método para obtener una opción válida del menú
    private int obtenerOpcionValida(String mensaje, int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.print(mensaje);
            opcion = scanner.nextInt();
            if (opcion < min || opcion > max) {
                System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion < min || opcion > max);
        return opcion;
    }
}