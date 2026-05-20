package Controlador;

import Modelo.*;
import Utilidades.*;

import java.awt.desktop.OpenFilesEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

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
        Optional<Viaje> viaje = findViaje(String.valueOf(fecha), String.valueOf(hora), patBus);
        if (viaje.isPresent()){
            Viaje v = viaje.get();
            return v.getAsientos();
        }else{
            return new String[0][0];
        }
    }

    public int getMontoVenta(String idDocumento, TipoDocumento tipo){
       Optional<Venta> monto = findVenta(idDocumento, tipo);
       if (monto.isEmpty()){
           return 0;
       }else {
           Venta venta = monto.get();
           return venta.getMonto();
       }
    }

    public String getNombrePasajero(IdPersona idPasajero){
        Optional<Pasajero> nomPasajero =findPasajero(idPasajero);
        if (nomPasajero.isEmpty()){
            return  null;
        } else {
            Pasajero p = nomPasajero.get();
            return p.getNombreCompleto().toString();
        }
    }

    public boolean vendePasaje(String idDoc, LocalDate fecha, LocalTime hora, String patBus, int asiento, IdPersona idPasajero){
        Venta venta = null;
        for (int i = 0; i < misVentas.size() && venta == null; i++) {
            if (misVentas.get(i).getIdDocumento().equals(idDoc)) {
                venta = misVentas.get(i);
            }
        }

        if (venta == null) return false;
        Optional<Viaje> viaje = findViaje(String.valueOf(fecha), String.valueOf(hora), patBus);
        if (!viaje.isEmpty()) return false;
        Viaje v = viaje.get();

        Optional<Bus> bus = findBus(patBus);
        if (!bus.isEmpty()) return false;

        Optional<Cliente> cliente = findCliente(idPasajero);

        Optional<Pasajero> pasajero = findPasajero(idPasajero);
        if (!pasajero.isEmpty()) return false;
        Pasajero p = pasajero.get();

        venta.createPasaje(asiento, v, p);

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
        Optional<Viaje> v = findViaje(String.valueOf(fecha), String.valueOf(hora), patBus);
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

   private Optional<Bus> findBus(String patente){
        for (Bus b : misbuses){
            if (b.getPatente().equals(patente)){
                return Optional.of(b);
            }
        }
        return Optional.empty();
   }

   private Optional<Viaje> findViaje(String fecha, String hora, String patenteBus){
        LocalTime h = LocalTime.parse(hora);
        LocalDate fec = LocalDate.parse(fecha);
        Optional<Bus> bus = findBus(patenteBus);
        for (Viaje v : misViajes){
            if (v.getFecha().equals(fec) && v.getBus().getPatente().equals(patenteBus) && v.getHora().equals(h)){
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