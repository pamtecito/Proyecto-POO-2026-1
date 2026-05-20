package Controlador;

import java.util.ArrayList;
import Modelo.*;

public class ControladorEmpresa {
    private ControladorEmpresa instance;
    private ArrayList<Empresa> misEmpresas;
    private ArrayList<Terminal> misTerminales;
    private ArrayList<Bus> misBuses;
    private ArrayList<Conductor> misConductores;
    private ArrayList<Auxiliar> misAxiliares;
    private ArrayList<Venta> misVentas;

    private ControladorEmpresa() {
        misEmpresas = new ArrayList<>();
        misTerminales = new ArrayList<>();
        misBuses = new ArrayList<>();
        misConductores = new ArrayList<>();
        misAxiliares = new ArrayList<>();
        misVentas = new ArrayList<>();

    }



}
