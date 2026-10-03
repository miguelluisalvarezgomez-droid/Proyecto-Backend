package repository;

import domain.Cliente;
import service.Outputports.ClienteOutputPort;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Persistencia en memoria (suficiente para la clase)
public class ClienteRepository implements ClienteOutputPort {

    private final Map<Long, Cliente> clientes = new LinkedHashMap<>();
    private long siguienteId = 1;

    @Override
    public Cliente guardar(Cliente cliente) {
        if (cliente.getId() == null) {
            cliente.setId(siguienteId++);
        }
        clientes.put(cliente.getId(), cliente);
        return cliente;
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return Optional.ofNullable(clientes.get(id));
    }

    @Override
    public Optional<Cliente> buscarPorCorreo(String correo) {
        return clientes.values().stream()
                .filter(c -> c.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
    }

    @Override
    public boolean existeCorreo(String correo) {
        return buscarPorCorreo(correo).isPresent();
    }

    @Override
    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes.values());
    }
}
