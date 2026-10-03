package service;

import domain.Comerciante;
import exception.NegocioException;
import helpers.PasswordHelper;
import service.Inputports.CuentaComercianteInputPort;
import service.Outputports.ComercianteOutputPort;

public class ComercianteService implements CuentaComercianteInputPort {

    private final ComercianteOutputPort comercianteRepository;

    public ComercianteService(ComercianteOutputPort comercianteRepository) {
        this.comercianteRepository = comercianteRepository;
    }

    // US-15: usa su propio repositorio, así el acceso queda separado del de clientes
    @Override
    public Comerciante iniciarSesion(String correo, String contrasena) {
        String correoBuscado = correo == null ? "" : correo.trim();
        String hash = PasswordHelper.hash(contrasena == null ? "" : contrasena);

        return comercianteRepository.buscarPorCorreo(correoBuscado)
                .filter(c -> c.isActivo() && hash.equals(c.getContrasena()))
                .orElseThrow(() -> new NegocioException("Credenciales incorrectas."));
    }
}
