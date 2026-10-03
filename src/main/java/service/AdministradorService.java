package service;

import domain.Administrador;
import exception.NegocioException;
import helpers.PasswordHelper;
import service.Inputports.AdministradorInputPort;
import service.Outputports.AdministradorOutputPort;

public class AdministradorService implements AdministradorInputPort {

    private final AdministradorOutputPort administradorRepository;

    public AdministradorService(AdministradorOutputPort administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    // US-26: acceso separado del de clientes y comercios (repositorio propio)
    @Override
    public Administrador iniciarSesion(String correo, String contrasena) {
        String correoBuscado = correo == null ? "" : correo.trim();
        String hash = PasswordHelper.hash(contrasena == null ? "" : contrasena);

        return administradorRepository.buscarPorCorreo(correoBuscado)
                .filter(a -> a.isActivo() && hash.equals(a.getContrasena()))
                .orElseThrow(() -> new NegocioException("Credenciales incorrectas."));
    }
}
