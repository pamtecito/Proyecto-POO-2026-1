package Modelo;

import Utilidades.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Conductor extends Tripulante implements Serializable {

    private ArrayList<Viaje> misViajes;

    public Conductor (IdPersona id, Nombre nom, Direccion dir ){
        super(id,nom,dir);
        misViajes = new ArrayList<>();
    }
    //creo arraylist tipoo vieje
    public void addViaje (Viaje viaje){
        misViajes.add(viaje);
    }
    //retorno mi tamaño
    public int getNroViajes(){
        return misViajes.size();
    }
}
