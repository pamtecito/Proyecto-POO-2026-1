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
            throw new SistemaVentaPasajesException("Ya existe empresa con el rut indicado.");
        }

        Empresa empresa = new Empresa(rut, nombre, url);
        misEmpresas.add(empresa);
    }

    public void createBus(String pat, String marca, String modelo, int nroAsientos, Rut rutEmp) throws SistemaVentaPasajesException {
        Optional<Empresa> empresa = findEmpresa(rutEmp);

        if (findEmpresa(rutEmp).isEmpty()){
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado.");
        }
        if (findBus(pat).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe bus con la patente indicada.");
        }

        Bus bus = new Bus(pat, nroAsientos, empresa.get());
        bus.setMarca(marca);
        bus.setModelo(modelo);

        misBuses.add(bus);
    }

    public void createTerminal(String nombre, Direccion direccion) throws SistemaVentaPasajesException{
        if (findTerminal(nombre).isPresent()){
            throw new SistemaVentaPasajesException("Ya existe terminal con el nombre indicado");
        }

        for (Terminal t : misTerminales){
            if (t.getDireccion().getComuna().equalsIgnoreCase(direccion.getComuna())) {
                throw new SistemaVentaPasajesException("Ya existe terminal en la comuna indicada");
            }
        }

        Terminal terminal = new Terminal(nombre, direccion);
        misTerminales.add(terminal);
    }

    public void hireConductorForEmpresa(Rut rutEmp, IdPersona id, Nombre nom, Direccion dir) throws SistemaVentaPasajesException{
        Optional<Empresa> empresa = findEmpresa(rutEmp);

        if (findEmpresa(rutEmp).isEmpty()){
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }

        boolean contratado = empresa.get().addConductor(id, nom, dir);
        if (!contratado){
            throw new SistemaVentaPasajesException("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada");
        }
    }

    public void hireAuxiliarForEmpresa(Rut rutEmp, IdPersona id, Nombre nom, Direccion dir) throws SistemaVentaPasajesException{
        Optional<Empresa> empresa = findEmpresa(rutEmp);

        if (findEmpresa(rutEmp).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado.");
        }

        boolean contratado = empresa.get().addAuxiliar(id, nom, dir);

        if (!contratado) {
            throw new SistemaVentaPasajesException("Ya está contratado auxiliar/conductor con el id dado en la empresa señalada.");
        }

    }

    public String[][] listEmpresas(){
        if (misEmpresas.isEmpty()){
            return new String[0][0];
        }

        String[][] listEmp = new String[misEmpresas.size()][6];

        for (int i = 0; i < misEmpresas.size(); i++) {
            Empresa e = misEmpresas.get(i);
            listEmp[i][0] = e.getRut().toString();
            listEmp[i][1] = e.getNombre();
            listEmp[i][2] = e.getUrl();
            listEmp[i][3] = String.valueOf(e.getTripulantes().length);
            listEmp[i][4] = String.valueOf(e.getBuses().length);
            listEmp[i][5] = String.valueOf(e.getVentas().length);
        }

        return listEmp;
    }

    public String[][] listLlegadasSalidasTerminal(String nombre, LocalDate fecha) throws SistemaVentaPasajesException{
        Optional<Terminal> terminal = findTerminal(nombre);
        if (findTerminal(nombre).isEmpty()) {
            throw new SistemaVentaPasajesException("No existe un terminal con el nombre dado.No existe terminal con el nombre indicado");
        }

        ArrayList<String[]> salidasYLlegadas = new ArrayList<>();

        for (Bus b : misBuses){
            for (Viaje v : b.getViajes()){
                boolean salida = v.getTerminalSalida().equals(terminal.get()) && v.getFecha().equals(fecha);
                boolean llegada = v.getTerminalLlegada().equals(terminal.get()) && v.getFechaHoraTermino().toLocalDate().equals(fecha);

                if (salida) {
                    String[] salidaOficial = new String[5];
                    salidaOficial[0] = "Salida";
                    salidaOficial[1] = v.getHora().toString();
                    salidaOficial[2] = b.getPatente();
                    salidaOficial[3] = b.getEmpresa().getNombre();
                    salidaOficial[4] = String.valueOf(v.getVentas().length);
                    salidasYLlegadas.add(salidaOficial);
                } else if (llegada){
                    String[] llegadaOficial = new String[5];
                    llegadaOficial[0] = "Llegada";
                    llegadaOficial[1] = v.getFechaHoraTermino().toLocalTime().toString();
                    llegadaOficial[2] = b.getPatente();
                    llegadaOficial[3] = b.getEmpresa().getNombre();
                    llegadaOficial[4] = String.valueOf(v.getVentas().length);
                    salidasYLlegadas.add(llegadaOficial);
                }

            }
        }

        String[][] listaSalYLleg =  new String[salidasYLlegadas.size()][5];
        for (int i = 0; i < salidasYLlegadas.size(); i++) {
            listaSalYLleg[i] = salidasYLlegadas.get(i);
        }

        return listaSalYLleg;

    }

    public String[][] listVentasEmpresa(Rut rut) throws SistemaVentaPasajesException {
        Optional<Empresa> empresa = findEmpresa(rut);

        if(findEmpresa(rut).isEmpty()){
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado.");
        }

        Venta[] ventas = empresa.get().getVentas();

        if (ventas.length == 0) {
            return new String[0][0];
        }

        String[][] ventasTotales = new String[ventas.length][4];

        for (int i = 0; i < ventas.length; i++) {
            Venta v =  ventas[i];
            ventasTotales[i][0] = v.getFecha().toString();
            ventasTotales[i][1] = v.getTipo().toString();
            ventasTotales[i][2] = String.valueOf(v.getMontoPagado());
            ventasTotales[i][3] = v.getTipoPago();
        }

        return ventasTotales;
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

    protected Optional<Terminal> findTerminalPorComuna(String comuna){
        for (Terminal t : misTerminales) {
            if (t.getDireccion().getComuna().equals(comuna)) {
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
