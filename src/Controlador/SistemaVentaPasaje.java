package Controlador;

import Excepciones.SVPException;
import Excepciones.SVPException;
import Modelo.*;
import Utilidades.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

public class SistemaVentaPasaje implements Serializable {
    private static SistemaVentaPasaje instance;
    private ArrayList<Bus> misbuses;
    private ArrayList<Cliente> misClientes;
    private ArrayList<Pasajero> misPasajeros;
    private ArrayList<Viaje> misViajes;
    private ArrayList<Venta> misVentas;

    private SistemaVentaPasaje() {
        misbuses = new ArrayList<>();
        misClientes = new ArrayList<>();
        misPasajeros = new ArrayList<>();
        misViajes = new ArrayList<>();
        misVentas = new ArrayList<>();
    }

    public static SistemaVentaPasaje getInstance(){
        if (instance == null){
            return instance = new SistemaVentaPasaje();
        }
        return instance;
    }

    public void createCliente(IdPersona id, Nombre nom, String fono, String email) throws SVPException {
        if (findCliente(id).isPresent()) {
            throw new SVPException("Ya existe cliente con el id indicado.");
        }

        Cliente c = new Cliente(id,nom,email);
        c.setTelefono(fono);
        misClientes.add(c);
    }

    public void createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String fonoContacto) throws SVPException{
        if (findPasajero(id).isPresent()){
            throw new SVPException("Ya existe pasajero con el id indicado.");
        }
        Pasajero p = new Pasajero(id, nom);
        p.setFonoContacto(fono);
        p.setNomContacto(nomContacto);
        p.setFonoContacto(fonoContacto);
        misPasajeros.add(p);

    }

    public void createViaje(LocalDate fecha, LocalTime hora, int precio, int duracion, String patBus, IdPersona[] idPersonas, String[] nomComunas) throws SVPException {
        if (findViaje(fecha, hora,patBus).isPresent()){
            throw new SVPException("Ya existe viaje con fecha, hora y patente de bus indicados");
        }

        Optional<Bus> bus = ControladorEmpresa.getInstance().findBus(patBus);
        if (bus.isEmpty()) {
            throw new SVPException ("No existe un bus con la patente indicada.");
        }

        Rut rutEmpresa = bus.get().getEmpresa().getRut();

        Optional<Auxiliar> aux = ControladorEmpresa.getInstance().findAuxiliar(idPersonas[0], rutEmpresa);
        if (aux.isEmpty()) {
            throw new SVPException("No existe un auxiliar con el id indicado en la empresa con el rut indicado.");
        }

        Auxiliar auxiliar = aux.get();

        Optional<Conductor> cond = ControladorEmpresa.getInstance().findConductor(idPersonas[1], rutEmpresa);
        if (cond.isEmpty()) {
            throw new SVPException("No existe un conductor con el id indicado en la empresa con el rut indicado.");
        }

        Conductor conductor1 = cond.get();

        Conductor conductor2 = null;
        if (idPersonas.length == 3){
            Optional<Conductor> cond2 = ControladorEmpresa.getInstance().findConductor(idPersonas[2], rutEmpresa);
            if (cond2.isEmpty()){
                throw  new SVPException("No existe un conductor con el id indicado en la empresa con el rut indicado.");
            }
        }

        Optional<Terminal> salidaTerm = ControladorEmpresa.getInstance().findTerminalPorComuna(nomComunas[0]);
        if (salidaTerm.isEmpty()){
            throw new SVPException("No existe un terminal de salida en la comuna indicada.");
        }
        Terminal terminalSalida = salidaTerm.get();

        Optional<Terminal> llegadaTerm = ControladorEmpresa.getInstance().findTerminalPorComuna(nomComunas[1]);
        if (llegadaTerm.isEmpty()){
            throw new SVPException("No existe un terminal de llegada en la comuna indicada.");
        }
        Terminal terminalLlegada = llegadaTerm.get();

        Viaje viaje = new Viaje(fecha, hora, precio, duracion, bus.get(), auxiliar, conductor1, terminalSalida, terminalLlegada);

        if (conductor2 != null){
            viaje.addConductor(conductor2);
        }

        misViajes.add(viaje);
    }


    public void iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fechaViaje,String comSalida, String comLlegada, IdPersona idCliente, int nroPasaje) throws SVPException{
        if (findVenta(idDoc, tipo).isPresent()){
            throw new SVPException("Ya existe venta con el id y tipo de documento indicados.");
        }

        Optional<Cliente> cli = findCliente(idCliente);
        if (cli.isEmpty()){
            throw new SVPException("No existe cliente con id indicado.");
        }
        Cliente cliente =cli.get();

        String[][] horarios = getHorariosDisponibles(fechaViaje, comSalida, comLlegada, nroPasaje);
        if (horarios.length == 0){
            throw new SVPException("No existen viajes disponibles en la fecha y con terminales en las comunas de salida y llegada indicados.");
        }

        Venta venta = new Venta(idDoc, tipo, LocalDate.now(),cliente);
        misVentas.add(venta);
    }

    public String[][] getHorariosDisponibles(LocalDate fechaViaje, String comunaSalida, String comunaLlegada, int nroPasaje) {
        ArrayList<Viaje> viajesDisponibles = new ArrayList<>();

        for (Viaje v : misViajes){
            boolean fecha = v.getFecha().equals(fechaViaje);
            boolean salida = v.getTerminalSalida().getDireccion().getComuna().equals(comunaSalida);
            boolean llegada = v.getTerminalLlegada().getDireccion().getComuna().equals(comunaLlegada);
            boolean disponible = v.existeDisponibilidad(nroPasaje);

            if (fecha && salida && llegada && disponible){
                viajesDisponibles.add(v);
            }
        }

        String[][] horariosDisponibles = new String[viajesDisponibles.size()][4];

        for (int i = 0; i < horariosDisponibles.length; i++) {
            Viaje v = viajesDisponibles.get(i);
            horariosDisponibles[i][0] = v.getBus().getPatente();
            horariosDisponibles[i][1] = v.getHora().toString();
            horariosDisponibles[i][2] = String.valueOf(v.getPrecio());
            horariosDisponibles[i][3] = String.valueOf(v.getNroAsientosDisponibles());
        }
        return horariosDisponibles;
    }


    public String[] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) throws SVPException{
        Optional<Viaje> viaje = findViaje(fecha, hora, patBus);
        if (viaje.isEmpty()){
            throw new SVPException("No existe un viaje con esas caracteristas.");
        }

        return viaje.get().getAsientos();
    }




    public Optional<String> getNombrePasajero(IdPersona idPasajero){
        Optional<Pasajero> nomPasajero =findPasajero(idPasajero);
        if (nomPasajero.isEmpty()){
            return  Optional.empty();
        }
        return Optional.of(nomPasajero.get().getNombreCompleto().toString());
    }

    public Optional<Integer> getMontoVenta(String idDocumento, TipoDocumento tipo){
       Optional<Venta> monto = findVenta(idDocumento, tipo);
       if (monto.isEmpty()){
           return Optional.empty();
       }
       return Optional.of(monto.get().getMonto());
    }


    public void vendePasaje(String idDoc, TipoDocumento tipoDocumento, LocalDate fechaViaje, LocalTime hora, String patBus, int asiento, IdPersona idPasajero) throws SVPException{
        Optional<Venta> vent = findVenta(idDoc, tipoDocumento);
        if (vent.isEmpty()) {
            throw new SVPException("No existe venta con el id y tipo de documento indicados.");
        }
        Venta venta = vent.get();

        Optional<Pasajero> pas = findPasajero(idPasajero);
        if (pas.isEmpty()){
            throw new SVPException("No existe pasajero con el id indicado.");
        }
        Pasajero pasajero = pas.get();

        Optional<Viaje> via = findViaje(fechaViaje, hora, patBus);
        if (via.isEmpty()){
            throw new SVPException("No existe viaje con la fecha, hora y patente de bus indicados.");
        }
        Viaje viaje = via.get();

        venta.createPasaje(asiento, viaje, pasajero);
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo)throws SVPException{
        Optional<Venta> ven = findVenta(idDocumento, tipo);
        if (ven.isEmpty()){
            throw new SVPException("No existe venta con el id y tipo de documento indicados.");
        }
        Venta venta = ven.get();

        boolean pagado = venta.pagaMonto();
        if (!pagado){
            throw new SVPException("La venta ya fue pagada.");
        }
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipo, long nroTarjera){
        Optional<Venta> ven = findVenta(idDocumento, tipo);
        if (ven.isEmpty()){
            throw new SVPException("No existe una venta con el id o tipo de documento dado.");
        }
        Venta venta = ven.get();

        boolean pagado = venta.pagoMonto(nroTarjera);
        if (!pagado){
            throw new SVPException("La venta ya fue pagada.");
        }
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
        String[][] list = new String[misViajes.size()][8];
        for (int i = 0; i < misViajes.size(); i++) {
            Viaje v = misViajes.get(i);
            list[i][0] = v.getFecha().toString();
            list[i][1] = v.getHora().toString();
            list[i][2] = v.getFechaHoraTermino().toLocalTime().toString();
            list[i][3] = String.valueOf(v.getPrecio());
            list[i][4] = String.valueOf(v.getNroAsientosDisponibles());
            list[i][5] = v.getBus().getPatente();
            list[i][6] = v.getTerminalSalida().getDireccion().getComuna();
            list[i][7] = v.getTerminalLlegada().getDireccion().getComuna();
        }
        return list;
    }

    public String[][] listPasajerosViaje(LocalDate fecha, LocalTime hora, String patBus) throws SVPException{
        Optional<Viaje> via = findViaje(fecha, hora, patBus);
        if (via.isEmpty()){
            throw new SVPException("o existe viaje con la fecha, hora y patente de bus indicados.");
        }
        Viaje viaje = via.get();

        return viaje.getListaPasajeros();
    }

    public void generatePasajesVenta(String idDocumento, TipoDocumento tipo){
        Optional<Venta> v= findVenta(idDocumento, tipo);
        if(v.isEmpty()){
            throw new SVPException("");
        }

    }

   private Optional<Cliente> findCliente (IdPersona id) {
       for (Cliente c : misClientes) {
           if (c.getIdPersona().equals(id)) {
               return Optional.of(c);
           }
       }
       return Optional.empty();
   }

   private Optional<Venta> findVenta(String idDocumento, TipoDocumento tipoDocumento){
        for (Venta v: misVentas){
            if (v.getIdDocumento().equals(idDocumento) && v.getTipo().equals(tipoDocumento)){
                return Optional.of(v);
            }
        }
        return Optional.empty();
   }

   private Optional<Viaje> findViaje(LocalDate fecha, LocalTime hora, String patenteBus){
     for (Viaje v : misViajes){
         if (v.getFecha().equals(fecha) && v.getHora().equals(hora) && v.getBus().getPatente().equals(patenteBus)){
             return Optional.of(v);
         }
     }
     return Optional.empty();
   }

   private Optional<Pasajero> findPasajero(IdPersona idPersona){
        for (Pasajero p :misPasajeros){
            if (p.getIdPersona().equals(idPersona)){
                return Optional.of(p);
            }
        }
        return Optional.empty();
   }
}