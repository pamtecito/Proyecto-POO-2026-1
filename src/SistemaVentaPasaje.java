import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class SistemaVentaPasaje {
    private ArrayList<Bus> misbuses;
    private ArrayList<Cliente> misClientes;
    private ArrayList<Pasajero> misPasajeros;
    private ArrayList<Viaje> misViajes;
    private ArrayList<Venta> misVentas;

    public SistemaVentaPasaje() {
        misbuses = new ArrayList<>();
        misClientes = new ArrayList<>();
        misPasajeros = new ArrayList<>();
        misViajes = new ArrayList<>();
        misVentas = new ArrayList<>();
    }

    public boolean createCliente(IdPersona id, Nombre nom, String fono, String email) {
        if (findCliente(id) != null) return false;
        Cliente c = new Cliente(id,nom,email);
        c.setTelefono(fono);
        return misClientes.add(c);


    }

    public boolean createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String fonoContacto) {
        if (findPasajero(id) != null) return false;
        Pasajero p = new Pasajero(IdPersona id, Nombre nom);
        p.setNomContacto(nomContacto);
        p.setFonoContacto(fono);
        return misPasajeros.add(p);

    }

    public boolean createViaje(LocalDate fecha, LocalTime hora, int precio, String patBus) {
        if (findViaje(String.valueOf(fecha),String.valueOf(hora),patBus) != null) return false;
        if (findBus(patBus) == null) return false;
        return misViajes.add(new Viaje(fecha,hora,precio,findBus(patBus)));

    }

    public boolean iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fechaVenta, IdPersona idCliente){
        if (findVenta(idDoc, tipo) != null) return false;
        if (findCliente(idCliente) == null) return false;

        return misVentas.add(new Venta(idDoc, tipo, fechaVenta, findCliente(idCliente)));
    }

    public String[][] getHorario(LocalDate fechaViaje){
        return null;
    }

    public String[][] listAsientosDeViaje(LocalDate fecha, LocalTime hora, int precio, String patBus) {
        return null;
    }

    public int getMontoVenta(String idDocumento, TipoDocumento tipo){
        return 0;
    }

    public String getNombrePasajero(IdPersona idPasajero){
        return null;
    }

    public boolean vendePasaje(String idDoc, LocalDate fecha, LocalTime hora, String patBus, int asiento, IdPersona idPasajero){
        return false;
    }

    public String[][] listVentas(){
        String[][] list = new String[misViajes.size()][7];
       for (int i = 0; i < misVentas.size(); i++) {
            Venta v = misVentas.get(i);
            list[i][0] = v.getIdDocumento();
            list[i][1] = v.getTipo().toString();
            list[i][2] = v.getFecha().toString();
            list[i][3] = v.getCliente().getIdPersona().toString();
            list[i][4] = v.getCliente().getNombreCompleto().toString();
            list[i][5] = String.valueOf(v.getPasajes().length);
            list[i][6] = String.valueOf(v.getMonto());

       }
       return list;
    }

    public String[][] listViajes(){
        String[][] list = new String[misViajes.size()][5];
        for (int i = 0; i < misViajes.size(); i++) {
            Viaje v = misViajes.get(i);
            list[i][0] = String.valueOf(v.getFecha());
            list[i][1] = String.valueOf(v.getHora());
            list[i][2] = String.valueOf(v.getPrecio());
            list[i][3] = String.valueOf(v.getNroAsientosDisponibles());
            list[i][4] = v.getBus().getPatente();
        }
        return list;
    }

    public String[][] listPasajeros(LocalDate fecha, LocalTime hora, String patBus){
        String[][] list = new String[misPasajeros.size()][6];
        if (findViaje(String.valueOf(fecha),String.valueOf(hora),patBus) == null) return null;
        if (findBus(patBus) == null) return null;

        for (int i = 0; i < misVentas.size(); i++) {
            Pasaje[] pasajes = misVentas.get(i).getPasajes();
            for (Pasaje pasaje : pasajes) {

                Viaje v = findViaje(String.valueOf(fecha), String.valueOf(hora), patBus);

                if (pasaje.getViaje().equals(v)) {
                    Pasajero p = pasaje.getPasajero();
                    list[i][0] = String.valueOf(pasaje.getNumero());
                    list[i][1] = p.getIdPersona().toString();
                    list[i][2] = p.getNombreCompleto;
                    list[i][3] = p.getNomContacto();
                    list[i][4] = p.getFonoContacto();
                }
            }
        }
        return list;
    }

   private Cliente findCliente(IdPersona id){
        for (Cliente c : misClientes){
            if(c.getIdPersona().equals(id)){
                return c;
            }
        }
        return null;
   }
   private Venta findVenta(String idDocumento, TipoDocumento tipoDocumento){
        for (Venta v: misVentas){
            if (v.getIdDocumento().equals(idDocumento) && v.getTipo().equals(tipoDocumento)){
                return v;
            }
        }
        return null;
   }

   private Bus findBus(String patente){
        for (Bus b : misbuses){
            if (b.getPatente() == patente){
                return b;
            }
        }
        return null;
   }

   private Viaje findViaje(String fecha, String hora, String patenteBus){
        LocalTime h = LocalTime.parse(hora);
        LocalDate fec = LocalDate.parse(fecha);
        Bus bus = findBus(patenteBus);
        for (Viaje v : misViajes){
            if (v.getFecha().equals(fec) && v.getBus().equals(bus) && v.getHora().equals(h)){
                return v;
            }
        }
        return null;
   }

   private Pasajero findPasajero(IdPersona idPersona){
        for (misPasajeros p : Pasajero){
            if (p.getIdPersona().equals(idPersona)){
                return p;
            }
        }
        return null;
   }
}
