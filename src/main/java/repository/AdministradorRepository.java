package repository;

import domain.Administrador;
import service.Outputports.AdministradorOutputPort;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class AdministradorRepository implements AdministradorOutputPort {

    private final Map<Long, Administrador> administradores = new LinkedHashMap<>();
    private long siguienteId = 1;

    @Override
    public Administrador guardar(Administrador administrador) {
        if (administrador.getId() == null) {
            administrador.setId(siguienteId++);
        }
        administradores.put(administrador.getId(), administrador);
        return administrador;
    }

    @Override
    public Optional<Administrador> buscarPorId(Long id) {
        return Optional.ofNullable(administradores.get(id));
    }

    @Override
    public Optional<Administrador> buscarPorCorreo(String correo) {
        return administradores.values().stream()
                .filter(a -> a.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
    }
}
