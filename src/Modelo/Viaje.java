package Modelo;
import Excepciones.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Viaje implements Serializable {
    private LocalDate fecha;
    private LocalTime hora;
    private int precio;
    private int duracion;
    private Bus bus;
    private ArrayList<Pasaje> pasajes;
    private Terminal llegada;
    private Terminal salida;
    private Auxiliar aux;
    private ArrayList<Conductor> conductors;

    public Viaje(LocalDate fecha, LocalTime hora, int precio, int duracion ,Bus bus, Auxiliar aux, Conductor cond, Terminal sale, Terminal llega) {
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.duracion= duracion;
        this.bus = bus;
        pasajes= new ArrayList<>();
        this.aux= aux;
        this.conductors= new ArrayList<>();
        conductors.add(cond);
        this.salida= sale;
        this.llegada= llega;
        bus.addViaje(this);
        sale.addSalida(this);
        llega.addLlegada(this);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public int getPrecio() {
        return precio;
    }

    public Bus getBus() {
        return bus;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setDuracion(int duracion){
        this.duracion= duracion;
    }

    public LocalDateTime getFechaHoraTermino() {
        LocalDateTime fechita= LocalDateTime.of(fecha, hora);
        return fechita.plusMinutes(duracion);
    }

    public String[] getAsientos() {
        String[] asientos = new String[this.getBus().getNroAsientos()];
        for (int i = 0; i < asientos.length; i++) {
            asientos[i] = String.valueOf(i + 1);
        }
        for (Pasaje p : pasajes) {
            asientos[p.getAsiento() - 1] = "*";
        }
        return asientos;
    }

    public void addPasaje(Pasaje pasaje) {
        pasajes.add(pasaje);
    }

    public String[][] getListaPasajeros(){
        String[][] h= new String[pasajes.size()][4];
        for(int i=0; i<h.length; i++){
            h[i][0]= String.valueOf(pasajes.get(i).getPasajero().getIdPersona().toString());
            h[i][1]= String.valueOf(pasajes.get(i).getPasajero().getNombreCompleto().toString());
            h[i][2]= String.valueOf(pasajes.get(i).getPasajero().getNomContacto().toString());
            h[i][3]= pasajes.get(i).getPasajero().getFonoContacto();
        }
        return h;
    }

    public boolean existeDisponibilidad(int nroAsientos){
        int asientosLibres = bus.getNroAsientos() - pasajes.size();
        return asientosLibres >= nroAsientos;
    }

    public int getNroAsientosDisponibles() {
        return bus.getNroAsientos() - pasajes.size();
    }//lo agrege para probar


    public Venta[] getVentas(){
        ArrayList<Venta> ventas= new ArrayList<>();
        for(Pasaje p: pasajes){
            Venta v= p.getVenta();
            if(!ventas.contains(v)){
                ventas.add(v);
            }
        }
        return ventas.toArray(new Venta[0]);
    }

    public void addConductor(Conductor conductor) throws SistemaVentaPasajesException {
        if(conductors.size()>= 2) {
            throw new SistemaVentaPasajesException("Maximo 2 conductores");
        }
        conductors.add(conductor);
    }

    public Tripulante[] getTripulantes(){
       ArrayList<Tripulante> totalTrip = new ArrayList<>();

       totalTrip.add((Tripulante) aux);

       for (Conductor c : conductors){
           totalTrip.add(c);
       }

       return totalTrip.toArray(new Tripulante[0]);
    }

    public Terminal getTerminalLlegada(){
        return llegada;
    }

    public Terminal getTerminalSalida(){
        return salida;
    }

}
