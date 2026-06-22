package Modelo;

import Utilidades.Direccion;
import Utilidades.IdPersona;
import Utilidades.Nombre;

import java.util.ArrayList;
import java.io.Serializable;

public class Auxiliar extends Tripulante implements Serializable {

    private ArrayList<Viaje> misViajes;

    public Auxiliar(IdPersona id, Nombre nom, Direccion dir){
        super(id, nom, dir);
        misViajes = new ArrayList<>();

    }

    @Override
    public void addViaje(Viaje viaje) {
        misViajes.add(viaje);

    }
    public int getNroViajes(){
        return  misViajes.size();
    }
}
