package service.Inputports;

import domain.Restaurante;

public interface NegocioInputPort {

    // US-16: el negocio asociado al comerciante
    Restaurante verPerfil(Long comercianteId);

    // US-16: los cambios se reflejan de inmediato en lo que ve el cliente
    Restaurante editarPerfil(Long comercianteId, String nombre, String direccion,
                             double latitud, double longitud, String horario, String logoUrl);
}
