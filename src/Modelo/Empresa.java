package Modelo;
import Utilidades.*;
import java.util.ArrayList;

public class Empresa {
    private Rut rut;
    private String nombre;
    private String url;
    private ArrayList<Bus> buses;
    private ArrayList<Conductor> conductores;
    private ArrayList<Auxiliar> auxiliares;
    public Empresa(Rut rut, String nombre, String url){
        this.rut = rut;
        this.nombre = nombre;
        this.url = url;

        buses = new ArrayList<>();
        conductores = new ArrayList<>();
        auxiliares = new ArrayList<>();

    }
    public Rut getRut() {
        return rut;
    }
    public String getNombre() {
        return nombre;
    }
    public String getUrl() {
        return url;
    }
    public void setRut(Rut rut) {
        this.rut = rut;
    }
    public void addBus(Bus bus) {

        buses.add(bus);
    }
    public Bus[] getBuses() {
        Bus[] arreglo = new Bus[buses.size()];

        for(int i = 0; i < buses.size(); i++){

            arreglo[i] = buses.get(i);
        }

        return arreglo;
    }

    public boolean addConductor (IdPersona id, Nombre nombre, Direccion dir){
        return false;
    }
    public boolean addAuxiliar (IdPersona id, Nombre nombre, Direccion dir){
        return false;
    }
    public Tripulante[] getTripulantes(){
        return null;
    }


    public Venta[] getVentas(){
        return null;
    }
}
