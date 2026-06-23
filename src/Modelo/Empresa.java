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
    public void SetUrl(String url){
        this.url = url;
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
        if (conductores.contains(new Conductor(id,nombre,dir))){//obejeto temporal contains metodo que ve si dentro del arreglo el existe
            return false;
        }
        conductores.add(new  Conductor(id,nombre,dir));
        return true;
    }
    public boolean addAuxiliar (IdPersona id, Nombre nombre, Direccion dir){
        if (auxiliares.contains(new Auxiliar(id,nombre,dir))){
            return false;
        }
        auxiliares.add(new  Auxiliar(id,nombre,dir));
        return true;
    }
    public Tripulante[] getTripulantes(){

        Tripulante[] arreglo = new Tripulante[conductores.size() + auxiliares.size()];

        int idx = 0;
        for (int i = 0; i < conductores.size(); i++) {
            arreglo[idx++] = conductores.get(i);
        }
        for (int i = 0; i < auxiliares.size(); i++) {
            arreglo[idx++] = auxiliares.get(i);
        }

        return arreglo;
    }

//metodo faltante
    public Venta[] getVentas(){
        ArrayList<Venta>ventas =new ArrayList<>();
        for(Bus b : buses){
            Viaje[]viajes = b.getViajes();

            for(Viaje v : viajes){
                Venta[] ventas2 =v.getVentas();

                for(Venta vent : ventas2){

                    if (!ventas.contains(vent)){
                        ventas.add(vent);
                    }

                }
            }
        }
        Venta[] arreglo = new Venta[ventas.size()];
        for(int i = 0; i < arreglo.length ; i++){
            arreglo[i] = ventas.get(i);
        }
        return arreglo;
    }


}
