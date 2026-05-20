package Modelo;

import Utilidades.IdPersona;
import Utilidades.Nombre;

import java.util.ArrayList;

public class Auxiliar extends Tripulante {

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
