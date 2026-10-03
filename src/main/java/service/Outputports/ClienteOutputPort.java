package service.Outputports;

import domain.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteOutputPort {

    Cliente guardar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    Optional<Cliente> buscarPorCorreo(String correo);

    boolean existeCorreo(String correo);

    List<Cliente> listarTodos();
}
