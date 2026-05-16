package Modelo;

import java.util.ArrayList;

public class Terminal {
    private String nombre;
    private Direccion direccion;
    private ArrayList<Viaje> viajes;

    public Terminal(String nombre, Direccion direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.viajes = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }
    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void addLlegada(Viaje viaje) {
    }

    public void addSalida(Viaje viaje) {
    }

    public Viaje[] getLlegadas() {
        return null;
    }

    public Viaje[] getSalidas() {
        return null;
    }


}
