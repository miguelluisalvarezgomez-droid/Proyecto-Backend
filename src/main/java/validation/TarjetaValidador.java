package validation;

import exception.NegocioException;

import java.time.YearMonth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Validaciones de tarjeta (simulan la pasarela de pagos)
public final class TarjetaValidador {

    private static final Pattern VENCIMIENTO = Pattern.compile("^(0[1-9]|1[0-2])/(\\d{2})$");
    private static final Pattern CVV = Pattern.compile("^\\d{3,4}$");

    private TarjetaValidador() {
    }

    // Devuelve el número solo con dígitos
    public static String numero(String valor) {
        String limpio = Validador.requerido(valor, "número de tarjeta").replaceAll("[\\s-]", "");
        if (!limpio.matches("\\d{13,19}") || !cumpleLuhn(limpio)) {
            throw new NegocioException("El número de tarjeta no es válido.");
        }
        return limpio;
    }

    // Formato MM/AA
    public static void vencimiento(String valor) {
        Matcher m = VENCIMIENTO.matcher(Validador.requerido(valor, "fecha de vencimiento"));
        if (!m.matches()) {
            throw new NegocioException("La fecha de vencimiento debe tener el formato MM/AA.");
        }
        YearMonth vence = YearMonth.of(2000 + Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)));
        if (vence.isBefore(YearMonth.now())) {
            throw new NegocioException("La tarjeta está vencida.");
        }
    }

    public static void cvv(String valor) {
        if (valor == null || !CVV.matcher(valor.trim()).matches()) {
            throw new NegocioException("El CVV debe tener 3 o 4 dígitos.");
        }
    }

    public static String ultimosDigitos(String numeroLimpio) {
        return numeroLimpio.substring(numeroLimpio.length() - 4);
    }

    // Algoritmo de Luhn
    private static boolean cumpleLuhn(String digitos) {
        int suma = 0;
        boolean duplicar = false;
        for (int i = digitos.length() - 1; i >= 0; i--) {
            int d = digitos.charAt(i) - '0';
            if (duplicar) {
                d *= 2;
                if (d > 9) {
                    d -= 9;
                }
            }
            suma += d;
            duplicar = !duplicar;
        }
        return suma % 10 == 0;
    }
}
