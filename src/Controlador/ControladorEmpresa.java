package Controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import Modelo.*;
import Utilidades.*;
import Excepciones.*;

public class ControladorEmpresa {
    private static ControladorEmpresa instance;
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

    public static ControladorEmpresa getInstance(){
        if (instance == null){
            return instance = new ControladorEmpresa();
        }
        return instance;
    }

    public void createEmpresa(Rut rut, String nombre, String url) throws SistemaVentaPasajesException{
        if (findEmpresa(rut).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe una empresa con ese rut.");
        }

        Empresa empresa = new Empresa(rut, nombre, url);
        misEmpresas.add(empresa);
    }

    public void createBus(String pat, String marca, String modelo, int nroAsientos, Rut rutEmp) throws SistemaVentaPasajesException {
        Optional<Empresa> empresa = findEmpresa(rutEmp);

        if (findEmpresa(rutEmp).isEmpty()){
            throw new SistemaVentaPasajesException("Empresa no encontrada.");
        }
        if (findBus(pat).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe un bus con esta patente.");
        }

        Bus bus = new Bus(pat, nroAsientos, empresa.get());
        bus.setMarca(marca);
        bus.setModelo(modelo);

        misBuses.add(bus);
    }

    public void createTerminal(String nombre, Direccion direccion) throws SistemaVentaPasajesException{
        if (findTerminal(nombre).isPresent()){
            throw new SistemaVentaPasajesException("Terminal ya existente.");
        }

        for (Terminal t : misTerminales){
            if (t.getDireccion().getComuna().equalsIgnoreCase(direccion.getComuna())) {
                throw new SistemaVentaPasajesException("Ya existe un terminal en esa comuna.");
            }
        }

        Terminal terminal = new Terminal(nombre, direccion);
        misTerminales.add(terminal);
    }

    public void hireConductorForEmpresa(Rut rutEmp, IdPersona id, Nombre nom, Direccion dir) throws SistemaVentaPasajesException{
        Optional<Empresa> empresa = findEmpresa(rutEmp);

        if (findEmpresa(rutEmp).isEmpty()){
            throw new SistemaVentaPasajesException("No existe una empresa con ese rut.");
        }

        boolean contratado = empresa.get().addConductor(id, nom, dir);
        if (!contratado){
            throw new SistemaVentaPasajesException("Ya está contratado este conductor.");
        }
    }

    public void hireAuxiliarForEmpresa(Rut rutEmp, IdPersona id, Nombre nom, Direccion dir) throws SistemaVentaPasajesException{
        Optional<Empresa> empresa = findEmpresa(rutEmp);

        if (findEmpresa(rutEmp).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe una empresa con ese rut.");
        }

        boolean contratado = empresa.get().addAuxiliar(id, nom, dir);

        if (!contratado) {
            throw new SistemaVentaPasajesException("Ya está contratado este auxiliar.");
        }

    }

    public String[][] listEmpresas(){
        //completar
        return null;
    }

    public String[][] listLlegadasSalidasTerminal(String nombre, LocalDate fecha){
        //completar
        return null;
    }

    public String[][] listVentasEmpresa(Rut rut){
        //completar
        return null;
    }

    protected Optional<Empresa> findEmpresa(Rut rut) throws SistemaVentaPasajesException {
        if (rut == null){
            throw new SistemaVentaPasajesException("Rut no puede ser null");
        }

        for (Empresa e : misEmpresas){
            if (e.getRut().equals(rut)){
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    protected Optional<Terminal> findTerminal(String nombre) throws SistemaVentaPasajesException{
        if (nombre == null){
            throw new SistemaVentaPasajesException("El nombre no puede ser null.");
        }

        for (Terminal t : misTerminales){
            if (t.getNombre().equals(nombre)) {
                return Optional.of(t);
            }
        }

        return Optional.empty();
    }

    protected Optional<Bus> findBus(String patente)throws SistemaVentaPasajesException {
        if (patente == null){
            throw new SistemaVentaPasajesException("La patente no puede ser null.");
        }

        for (Bus b : misBuses){
            if (b.getPatente().equalsIgnoreCase(patente)) {
                return Optional.of(b);
            }
        }

        return Optional.empty();
    }

    protected Optional<Conductor> findConductor(IdPersona id, Rut rutEmpresa) throws SistemaVentaPasajesException {
        Optional<Empresa> empresa = findEmpresa(rutEmpresa);

        if (empresa.isEmpty()) {
            throw new SistemaVentaPasajesException("Empresa no encontrada.");
        }

        for (Tripulante t : empresa.get().getTripulantes()){
            if (t instanceof Conductor c && c.getIdPersona().equals(id)){
                return Optional.of(c);
            }
        }

        return Optional.empty();
    }

    protected Optional<Auxiliar> findAuxiliar(IdPersona id, Rut rutEmpresa) throws SistemaVentaPasajesException{
        Optional<Empresa> empresa = findEmpresa(rutEmpresa);

        if (empresa.isEmpty()){
            throw new SistemaVentaPasajesException("Empresa no encontrada.");
        }

        for (Tripulante t : empresa.get().getTripulantes()){
            if (t instanceof Auxiliar a && a.getIdPersona().equals(id)) {
                return Optional.of(a);
            }
        }

        return Optional.empty();
    }


}
