package Modelo;
import Controlador.*;
import excepciones.SistemaVentaPasajesException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Viaje {
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
        conductors.add(cond);
        this.salida= sale;
        this.llegada= llega;
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

    public LocalDateTime getFechaHoraTermino(){
        //return fecha y hora de llegada de un viaje mas los minutos de duracion del viaje y hora de salida

    }



    public String[][] getAsientos(){
        String[][] asientos = new String[this.getBus().getNroAsientos()][2];
        for (int i = 0; i < asientos.length; i++) {
            asientos[i][0] = String.valueOf(i+1);
            asientos[i][1] = "libre";
        }
        for (Pasaje p : pasajes){
            asientos[p.getAsiento()][1] = "Ocupado";
        }
        return asientos;
    }

    public void addPasaje(Pasaje pasaje) {
        pasajes.add(pasaje);
    }

    public String[][] getListaPasajeros(){
        String[][] h= new String[pasajes.size()][4];
        for(int i=0; i<h.length; i++){
            h[i][0]= String.valueOf(pasajes.get(i).getPasajero().getIdPersona());
            h[i][1]= String.valueOf(pasajes.get(i).getPasajero().getNomContacto());
            h[i][2]= String.valueOf(pasajes.get(i).getPasajero().getNombreCompleto());
            h[i][3]= pasajes.get(i).getPasajero().getFonoContacto();
        }
        return h;
    }

    public boolean existeDisponibilidad(){
        return pasajes.size() < bus.getNroAsientos();
    }

    public int getNroAsientosDisponibles() {
        return bus.getNroAsientos() - pasajes.size();
    }

    public void getVentas(){

    }
    public void addConductor(Conductor conductor) throws SistemaVentaPasajesException {
        if(conductors.size()>= 2) {
            throw new SistemaVentaPasajesException("Maximo 2 conductores");
        }
        conductors.add(conductor);
    }
    public Tripulante[] getTripulantes(){
        return
    }
    public Terminal getTerminalLlegada(){
        return llegada;
    }
    public Terminal getTerminalSalida(){
        return salida;
    }

}
