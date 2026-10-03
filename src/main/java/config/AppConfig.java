package config;

import domain.Administrador;
import domain.enums.RolUsuario;
import helpers.PasswordHelper;
import repository.AdministradorRepository;
import repository.CalificacionRepository;
import repository.ClienteRepository;
import repository.ComercianteRepository;
import repository.FacturaRepository;
import repository.PagoRepository;
import repository.PedidoRepository;
import repository.ProductoRepository;
import repository.RestauranteRepository;
import service.AdministradorService;
import service.CalificacionService;
import service.CarritoService;
import service.ClienteService;
import service.ComercianteService;
import service.DescubrimientoService;
import service.FacturaService;
import service.GestionPlataformaService;
import service.Inputports.AdministradorInputPort;
import service.Inputports.CalificacionInputPort;
import service.Inputports.CarritoInputPort;
import service.Inputports.CuentaClienteInputPort;
import service.Inputports.CuentaComercianteInputPort;
import service.Inputports.DescubrimientoInputPort;
import service.Inputports.FacturaInputPort;
import service.Inputports.GestionPlataformaInputPort;
import service.Inputports.NegocioInputPort;
import service.Inputports.PagoInputPort;
import service.Inputports.SeguimientoInputPort;
import service.NegocioService;
import service.Outputports.AdministradorOutputPort;
import service.Outputports.CalificacionOutputPort;
import service.Outputports.ClienteOutputPort;
import service.Outputports.ComercianteOutputPort;
import service.Outputports.FacturaOutputPort;
import service.Outputports.PagoOutputPort;
import service.Outputports.PedidoOutputPort;
import service.Outputports.ProductoOutputPort;
import service.Outputports.RestauranteOutputPort;
import service.PagoService;
import service.SeguimientoService;
import view.AdministradorView;
import view.CarritoView;
import view.ClienteView;
import view.ComercianteView;
import view.Consola;
import view.DescubrimientoView;
import view.FacturaView;
import view.MenuPrincipalView;
import view.NegocioView;
import view.PagoView;
import view.PerfilView;
import view.PlataformaView;
import view.SeguimientoView;

import java.time.LocalDateTime;

// Aquí se conectan todas las piezas (inyección de dependencias manual)
public class AppConfig {

    // Cuenta inicial del sistema (cámbiala por la que quieras)
    private static final String ADMIN_CORREO = "admin@plataforma.com";
    private static final String ADMIN_CLAVE = "Admin1234";

    private final MenuPrincipalView menuPrincipalView;

    public AppConfig() {
        Consola consola = new Consola();

        // Repositories (Outputports)
        ClienteOutputPort clienteRepository = new ClienteRepository();
        ComercianteOutputPort comercianteRepository = new ComercianteRepository();
        AdministradorOutputPort administradorRepository = new AdministradorRepository();
        RestauranteOutputPort restauranteRepository = new RestauranteRepository();
        ProductoOutputPort productoRepository = new ProductoRepository();
        PedidoOutputPort pedidoRepository = new PedidoRepository();
        PagoOutputPort pagoRepository = new PagoRepository();
        FacturaOutputPort facturaRepository = new FacturaRepository();
        CalificacionOutputPort calificacionRepository = new CalificacionRepository();

        crearAdministradorInicial(administradorRepository);

        // Services (Inputports) - cliente
        CuentaClienteInputPort cuentaService = new ClienteService(clienteRepository);
        DescubrimientoInputPort descubrimientoService =
                new DescubrimientoService(restauranteRepository, productoRepository);
        CarritoInputPort carritoService =
                new CarritoService(pedidoRepository, productoRepository, restauranteRepository);
        FacturaInputPort facturaService =
                new FacturaService(facturaRepository, pagoRepository, pedidoRepository);
        PagoInputPort pagoService =
                new PagoService(pagoRepository, pedidoRepository, facturaService);
        SeguimientoInputPort seguimientoService = new SeguimientoService(pedidoRepository);
        CalificacionInputPort calificacionService =
                new CalificacionService(calificacionRepository, pedidoRepository, restauranteRepository);

        // Services (Inputports) - comerciante
        CuentaComercianteInputPort cuentaComercianteService = new ComercianteService(comercianteRepository);
        NegocioInputPort negocioService = new NegocioService(comercianteRepository, restauranteRepository);

        // Services (Inputports) - administrador
        AdministradorInputPort cuentaAdminService = new AdministradorService(administradorRepository);
        GestionPlataformaInputPort plataformaService = new GestionPlataformaService(
                administradorRepository, comercianteRepository, clienteRepository, restauranteRepository);

        // Views - cliente
        FacturaView facturaView = new FacturaView(consola);
        PagoView pagoView = new PagoView(pagoService, facturaService, facturaView, consola);
        CarritoView carritoView = new CarritoView(carritoService, pagoView, consola);
        SeguimientoView seguimientoView =
                new SeguimientoView(seguimientoService, pagoService, facturaService,
                        calificacionService, facturaView, consola);
        PerfilView perfilView = new PerfilView(cuentaService, consola);
        DescubrimientoView descubrimientoView =
                new DescubrimientoView(descubrimientoService, carritoView, seguimientoView, perfilView, consola);
        ClienteView clienteView = new ClienteView(cuentaService, descubrimientoView, consola);

        // Views - comerciante
        NegocioView negocioView = new NegocioView(negocioService, consola);
        ComercianteView comercianteView = new ComercianteView(cuentaComercianteService, negocioView, consola);

        // Views - administrador
        PlataformaView plataformaView = new PlataformaView(plataformaService, consola);
        AdministradorView administradorView = new AdministradorView(cuentaAdminService, plataformaView, consola);

        this.menuPrincipalView = new MenuPrincipalView(clienteView, comercianteView, administradorView, consola);
    }

    public MenuPrincipalView menuPrincipalView() {
        return menuPrincipalView;
    }

    // El primer administrador tiene que existir para poder crear comercios (US-28)
    private void crearAdministradorInicial(AdministradorOutputPort repositorio) {
        Administrador admin = new Administrador();
        admin.setNombre("Administrador");
        admin.setCorreo(ADMIN_CORREO);
        admin.setTelefono("0000000");
        admin.setContrasena(PasswordHelper.hash(ADMIN_CLAVE));
        admin.setRol(RolUsuario.ADMINISTRADOR);
        admin.setActivo(true);
        admin.setFechaCambioEstado(LocalDateTime.now());
        repositorio.guardar(admin);
    }
}
