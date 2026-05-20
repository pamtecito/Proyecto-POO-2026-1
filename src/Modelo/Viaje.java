package Modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

public class Viaje {
    private LocalDate fecha;
    private LocalTime hora;
    private int precio;
    private Bus bus;
    private ArrayList<Pasaje> pasajes;

    public Viaje(LocalDate fecha, LocalTime hora, int precio, Optional<Bus> bus) {
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.bus = bus;
        pasajes= new ArrayList<Pasaje>();
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
    }//lo agrege para probar
    public Venta[] getVentas(){

        ArrayList<Venta> ventas = new ArrayList<>();

        for(Pasaje p : pasajes){

            Venta venta = p.getVenta();

            if(!ventas.contains(venta)){

                ventas.add(venta);
            }
        }

        Venta[] arreglo = new Venta[ventas.size()];

        for(int i = 0; i < ventas.size(); i++){

            arreglo[i] = ventas.get(i);
        }

        return arreglo;
    }
}
