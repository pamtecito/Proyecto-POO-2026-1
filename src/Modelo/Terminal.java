package Modelo;

import Utilidades.Direccion;

import java.util.ArrayList;

public class Terminal {
    private String nombre;
    private Direccion direccion;
    private ArrayList<Viaje> llegadas;
    private ArrayList<Viaje> salidas;

    public Terminal(String nombre, Direccion direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.llegadas = new ArrayList<>();
        this.salidas = new ArrayList<>();
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
        Viaje[] llegadasAr = new Viaje[this.llegadas.size()];
        for (int i = 0; i < this.llegadas.size(); i++) {
            llegadasAr[i] = llegadas.get(i);
        }
        return llegadasAr;
    }

    public Viaje[] getSalidas() {
        Viaje[] salidasAr = new Viaje[this.salidas.size()];
        for (int i = 0; i < salidas.size(); i++) {
            salidasAr[i] = salidas.get(i);
        }
        return salidasAr;
    }


}
