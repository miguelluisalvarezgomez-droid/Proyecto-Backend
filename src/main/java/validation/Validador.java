package validation;

import exception.NegocioException;

import java.util.regex.Pattern;

public final class Validador {

    private static final Pattern CORREO = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern TELEFONO = Pattern.compile("^\\d{7,15}$");

    private Validador() {
    }

    public static String requerido(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new NegocioException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    public static String correo(String valor) {
        String correo = requerido(valor, "correo");
        if (!CORREO.matcher(correo).matches()) {
            throw new NegocioException("El formato del correo no es válido.");
        }
        return correo;
    }

    public static String telefono(String valor) {
        String telefono = requerido(valor, "teléfono");
        if (!TELEFONO.matcher(telefono).matches()) {
            throw new NegocioException("El teléfono debe tener entre 7 y 15 dígitos.");
        }
        return telefono;
    }

    // Mínimo 8 caracteres, con mayúscula, minúscula y número
    public static String contrasenaSegura(String valor) {
        if (valor == null || valor.length() < 8
                || !valor.chars().anyMatch(Character::isUpperCase)
                || !valor.chars().anyMatch(Character::isLowerCase)
                || !valor.chars().anyMatch(Character::isDigit)) {
            throw new NegocioException(
                    "La contraseña debe tener mínimo 8 caracteres, con mayúscula, minúscula y número.");
        }
        return valor;
    }
}
