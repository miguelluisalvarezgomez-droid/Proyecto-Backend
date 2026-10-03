package view;

import exception.NegocioException;

import java.util.Scanner;

// Un solo Scanner compartido por todas las vistas
public class Consola {

    private final Scanner scanner = new Scanner(System.in);

    public String capturar(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return scanner.nextLine();
    }

    public int leerEntero(String etiqueta) {
        try {
            return Integer.parseInt(capturar(etiqueta).trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public double leerDecimal(String etiqueta) {
        try {
            return Double.parseDouble(capturar(etiqueta).trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new NegocioException("El valor numérico no es válido.");
        }
    }

    public void mostrar(String mensaje) {
        System.out.println(mensaje);
    }
}
