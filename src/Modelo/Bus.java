package Modelo;

import java.util.ArrayList;

public class Bus {
    private String patente;
    private String marca;
    private String modelo;
    private int nroAsientos;
    private ArrayList<Viaje> viajes;
    private Empresa empresa;

    public Bus(String patente, int nroAsientos, Empresa empresa){
        this.patente=patente;
        this.nroAsientos= nroAsientos;
        this.empresa = empresa;
        viajes = new ArrayList<Viaje>();
    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getNroAsientos() {
        return nroAsientos;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void addViaje(Viaje viaje){
        viajes.add(viaje);
    }
    public Viaje[] getViajes(){

        Viaje[] arreglo = new Viaje[viajes.size()];

        for(int i = 0; i < viajes.size(); i++){

            arreglo[i] = viajes.get(i);
        }

        return arreglo;
    }
}
