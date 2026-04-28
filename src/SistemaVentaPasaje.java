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
        Pasajero p = new Pasajero(id, nom);
        p.setNomContacto(nomContacto);
        p.setFonoContacto(fono);
        p.setFonoContacto(fonoContacto);
        return misPasajeros.add(p);

    }

    public boolean createBus(String patente, String marca, String modelo, int nroAsientos){
            if(findBus(patente) != null) return false;
            Bus b = new Bus(patente, nroAsientos);
            b.setMarca(marca);
            b.setModelo(modelo);
            return misbuses.add(b);
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

    public String[][] getHorariosDisponibles(LocalDate fechaViaje){
        ArrayList<Viaje> viajesFecha = new ArrayList<>();
        for (Viaje v : misViajes){
            if(v.getFecha().equals(fechaViaje)){
                viajesFecha.add(v);
            }
        }
        String[][] horarios = new String[viajesFecha.size()][4];
        for (int i = 0; i < horarios.length; i++) {
            Viaje v = viajesFecha.get(i);
            horarios[i][0] = v.getBus().getPatente();
            horarios[i][1] = v.getHora().toString();
            horarios[i][2] = String.valueOf(v.getPrecio());
            horarios[i][3] = String.valueOf(v.getNroAsientosDisponibles());
        }
        return horarios;
    }

    public String[][] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) {
        Viaje viaje = findViaje(String.valueOf(fecha), String.valueOf(hora), patBus);
        if (viaje == null)return new String[0][0];

        return viaje.getAsientos();
    }

    public int getMontoVenta(String idDocumento, TipoDocumento tipo){
       if (findVenta(idDocumento, tipo) == null) return 0;
       Venta monto = findVenta(idDocumento, tipo);
       return monto.getMonto();
    }

    public String getNombrePasajero(IdPersona idPasajero){
        if (findPasajero(idPasajero) == null) return  null;
        Pasajero nom =findPasajero(idPasajero);
        return nom.getNombreCompleto().toString();
    }

    public boolean vendePasaje(String idDoc, LocalDate fecha, LocalTime hora, String patBus, int asiento, IdPersona idPasajero){
        Venta venta = null;
        for (int i = 0; i < misVentas.size() && venta == null; i++) {
            if (misVentas.get(i).getIdDocumento().equals(idDoc)) {
                venta = misVentas.get(i);
            }
        }

        if (venta == null) return false;
        Viaje viaje = findViaje(String.valueOf(fecha), String.valueOf(hora), patBus);
        if (viaje == null) return false;

        Bus bus = findBus(patBus);
        if (bus == null) return false;

        Cliente cliente = findCliente(idPasajero);

        Pasajero pasajero = findPasajero(idPasajero);
        if (pasajero == null) return false;

        venta.createPasaje(asiento, viaje, pasajero);
        return true;
    }

    public String[][] listVentas(){
        String[][] list = new String[misVentas.size()][7];
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
            list[i][0] = v.getBus().getPatente();
            list[i][1] = String.valueOf(v.getFecha());
            list[i][2] = String.valueOf(v.getHora());
            list[i][3] = String.valueOf(v.getPrecio());
            list[i][4] = String.valueOf(v.getNroAsientosDisponibles());
        }
        return list;
    }

    public String[][] listPasajeros(LocalDate fecha, LocalTime hora, String patBus){
        if (findBus(patBus) == null) return null;
        Viaje v = findViaje(String.valueOf(fecha), String.valueOf(hora), patBus);
        if (v== null) return new String[0][0];
        int contador = 0;
        for (Venta venta : misVentas) {
            for (Pasaje pasaje : venta.getPasajes()) {
                if (pasaje.getViaje().equals(v)) {
                    contador++;
                }
            }
        }
        String[][] list = new String[contador][6];
        int fila = 0;
        for (Venta venta : misVentas) {
            for (Pasaje pasaje : venta.getPasajes()) {
                if (pasaje.getViaje().equals(v)) {
                    Pasajero p = pasaje.getPasajero();
                    list[fila][0] = String.valueOf(pasaje.getAsiento());
                    list[fila][1] = p.getIdPersona().toString();
                    list[fila][2] = p.getNombreCompleto().toString();
                    list[fila][3] = p.getNomContacto().toString();
                    list[fila][4] = p.getFonoContacto();
                    fila++;
                }
            }
        }


        System.out.println("Control" + list.length);
        return list;
    }

   private Cliente findCliente(IdPersona id) {
       for (Cliente c : misClientes) {
           if (c.getIdPersona().equals(id)) {
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
            if (b.getPatente().equals(patente)){
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
            if (v.getFecha().equals(fec) && v.getBus().getPatente().equals(patenteBus) && v.getHora().equals(h)){
                return v;
            }
        }
        return null;
   }

   private Pasajero findPasajero(IdPersona idPersona){
        for (Pasajero p :misPasajeros){
            if (p.getIdPersona().equals(idPersona)){
                return p;
            }
        }
        return null;
   }


    public void debug() {

        // ── Crear Clientes ────────────────────────────────────────
        System.out.println("=== CREAR CLIENTES ===");

        Nombre nomCliente1 = new Nombre();
        nomCliente1.setTratamiento(Tratamiento.SR);
        nomCliente1.setNombres("Juan Carlos");
        nomCliente1.setApellidoPaterno("Pérez");
        nomCliente1.setApellidoMaterno("González");

        Nombre nomCliente2 = new Nombre();
        nomCliente2.setTratamiento(Tratamiento.SRA);
        nomCliente2.setNombres("María José");
        nomCliente2.setApellidoPaterno("López");
        nomCliente2.setApellidoMaterno("Soto");

}