package persistencia;
import Modelo.*;
import Utilidades.*;

import Excepciones.SistemaVentaPasajesException;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Optional;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalTime;

public class IOSVP {
    private static IOSVP instancia = null;

    private IOSVP() {
    }

    public static IOSVP getInstancia() {
        if(instancia == null){
            instancia = new IOSVP();
        }
        return instancia;
    }

    public Object[] readDatosIniciales() throws SistemaVentaPasajesException {
        ArrayList<Object> objetos = new ArrayList<>();

        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Pasajero> pasajeros = new ArrayList<>();
        ArrayList<Empresa> empresas = new ArrayList<>();
        ArrayList<Auxiliar> auxiliares = new ArrayList<>();
        ArrayList<Conductor> conductores = new ArrayList<>();
        ArrayList<Terminal> terminales = new ArrayList<>();
        ArrayList<Bus> buses = new ArrayList<>();
        ArrayList<Viaje> viajes = new ArrayList<>();

        try {
            File archivo = new File("SVPDatosIniciales.txt");

            Scanner escaner = new Scanner(new File("SVPDatosIniciales.txt"));

            int seccion = 0;

            while(escaner.hasNextLine()) {
                String linea = escaner.nextLine();

                if(linea.equals("+")) {
                    seccion++;
                    continue;
                }

                switch(seccion) {
                    case 0:
                        readClientesPasajeros(linea, objetos, clientes, pasajeros);
                        break;

                    case 1:
                        readEmpresas(linea, objetos, empresas);
                        break;

                    case 2:
                        readTripulantes(linea, objetos, auxiliares, conductores, empresas);
                        break;

                    case 3:
                        readTerminales(linea, objetos, terminales);
                        break;

                    case 4:
                        readBuses(linea, objetos, buses, empresas);
                        break;

                    case 5:
                        readViajes(linea, objetos, viajes, buses, terminales);
                        break;
                }
            }

            escaner.close();

        } catch(FileNotFoundException e) {
            throw new SistemaVentaPasajesException("No existe o no se puede abrir el archivo SVPDatosIniciales.txt");
        }

        return objetos.toArray();
    }

    public void saveControladores(Object[] controladores) throws SistemaVentaPasajesException {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream("SVPObjetos.obj"))) {

            salida.writeObject(controladores);

        } catch (IOException e) {
            throw new SistemaVentaPasajesException("No se puede grabar en el archivo SVPObjetos.obj");
        }
    }

    public Object[] readControladores() throws SistemaVentaPasajesException {
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream("SVPObjetos.obj"))) {
            return (Object[]) entrada.readObject();

        } catch (IOException e) {
            throw new SistemaVentaPasajesException("No existe o no se puede abrir el archivo SVPObjetos.obj");
        } catch (ClassNotFoundException e) {
            throw new SistemaVentaPasajesException("No se puede leer el archivo SVPObjetos.obj");
        }
    }



    private void readClientesPasajeros(String linea, ArrayList<Object> objetos, ArrayList<Cliente> clientes, ArrayList<Pasajero> pasajeros) throws SistemaVentaPasajesException {
        String[] datos = linea.split(";");

        String tipo = datos[0];

        IdPersona id = Rut.of(datos[1]);
        Nombre nombre = crearNombre(datos[2], datos[3], datos[4], datos[5]);

        if(tipo.equals("C") || tipo.equals("CP")) {
            Cliente cliente = new Cliente(id, nombre, datos[7]);
            cliente.setTelefono(datos[6]);

            clientes.add(cliente);
            objetos.add(cliente);
        }

        if(tipo.equals("P") || tipo.equals("CP")) {

            Pasajero pasajero = new Pasajero(id, nombre);
            pasajero.setTelefono(datos[6]);

            int inicioContacto;

            if (tipo.equals("CP")) {
                inicioContacto = 8;
            } else {
                inicioContacto = 7;
            }
            Nombre contacto = crearNombre(datos[inicioContacto], datos[inicioContacto + 1], datos[inicioContacto + 2], datos[inicioContacto + 3]);

            pasajero.setNomContacto(contacto);
            pasajero.setFonoContacto(datos[inicioContacto + 4]);

            pasajeros.add(pasajero);
            objetos.add(pasajero);
        }


    }

    private void readEmpresas(String linea, ArrayList<Object> objetos, ArrayList<Empresa> empresas) throws SistemaVentaPasajesException {
        String[] datos = linea.split(";");
        Empresa empresa = new Empresa(Rut.of(datos[0]), datos[1], datos[2]);

        empresas.add(empresa);
        objetos.add(empresa);
    }

    private void readTripulantes(String linea, ArrayList<Object> objetos, ArrayList<Auxiliar> auxiliares, ArrayList<Conductor> conductores, ArrayList<Empresa> empresas) throws SistemaVentaPasajesException {
        String[] datos = linea.split(";");

        IdPersona id = Rut.of(datos[1]);
        Nombre nombre = crearNombre(datos[2], datos[3], datos[4], datos[5]);
        Direccion direccion = new Direccion(datos[6], Integer.parseInt(datos[7]), datos[8]);
        Empresa empresa = findEmpresa(empresas, Rut.of(datos[9])).orElseThrow(() -> new SistemaVentaPasajesException("Empresa no encontrada"));

        if(datos[0].equals("A")) {

            Auxiliar auxiliar = new Auxiliar(id, nombre, direccion);

            empresa.addAuxiliar(id, nombre, direccion);
            auxiliares.add(auxiliar);
            objetos.add(auxiliar);
        } else if(datos[0].equals("C")) {

            Conductor conductor = new Conductor(id, nombre, direccion);

            empresa.addConductor(id, nombre, direccion);
            conductores.add(conductor);
            objetos.add(conductor);
        }
    }

    private void readTerminales(String linea, ArrayList<Object> objetos, ArrayList<Terminal> terminales) throws SistemaVentaPasajesException {
        String[] datos = linea.split(";");

        Direccion direccion =
                new Direccion(datos[1], Integer.parseInt(datos[2]), datos[3]);

        Terminal terminal =
                new Terminal(datos[0], direccion);

        terminales.add(terminal);
        objetos.add(terminal);
    }

    private void readBuses(String linea, ArrayList<Object> objetos, ArrayList<Bus> buses, ArrayList<Empresa> empresas) throws SistemaVentaPasajesException {
        String[] datos = linea.split(";");
        Rut rutEmpresa = Rut.of(datos[4]);

        Empresa empresa = findEmpresa(empresas, Rut.of(datos[4])).orElseThrow(() -> new SistemaVentaPasajesException("No existe empresa con rut " + datos[4]));
        Bus bus = new Bus(datos[0], Integer.parseInt(datos[3]), empresa);

        bus.setMarca(datos[1]);
        bus.setModelo(datos[2]);
        empresa.addBus(bus);

        buses.add(bus);
        objetos.add(bus);
    }

    private void readViajes(String linea, ArrayList<Object> objetos, ArrayList<Viaje> viajes, ArrayList<Bus> buses, ArrayList<Terminal> terminales) throws SistemaVentaPasajesException {
        String[] datos = linea.split(";");

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate fecha = LocalDate.parse(datos[0], formato);

        LocalTime hora = LocalTime.parse(datos[1]);

        int precio = Integer.parseInt(datos[2]);
        int duracion = Integer.parseInt(datos[3]);

        Bus bus = findBus(buses, datos[4]).orElseThrow(() -> new SistemaVentaPasajesException("Bus no encontrado"));

        Empresa empresa = bus.getEmpresa();

        Tripulante auxiliar = findTripulante(empresa, Rut.of(datos[5])).orElseThrow(() -> new SistemaVentaPasajesException("Auxiliar no encontrado"));

        Tripulante conductor = findTripulante(empresa, Rut.of(datos[6])).orElseThrow(() -> new SistemaVentaPasajesException("Conductor no encontrado"));

        if (!(auxiliar instanceof Auxiliar)) {
            throw new SistemaVentaPasajesException("El tripulante indicado no es auxiliar");
        }

        if (!(conductor instanceof Conductor)) {
            throw new SistemaVentaPasajesException("El tripulante indicado no es conductor");
        }

        Auxiliar auxilio = (Auxiliar) auxiliar;
        Conductor conducto = (Conductor) conductor;

        Terminal salida = findTerminal(terminales, datos[7]).orElseThrow(() -> new SistemaVentaPasajesException("Terminal de salida no encontrado"));

        Terminal llegada = findTerminal(terminales, datos[8]).orElseThrow(() -> new SistemaVentaPasajesException("Terminal de llegada no encontrado"));

        Viaje viaje = new Viaje(fecha, hora, precio, duracion, bus, auxilio, conducto, salida, llegada);

        auxilio.addViaje(viaje);
        conducto.addViaje(viaje);

        viajes.add(viaje);
        objetos.add(viaje);
    }

    private Optional<Empresa> findEmpresa(ArrayList<Empresa> empresas, Rut rut) {
        return empresas.stream()
                .filter(e -> e.getRut().equals(rut))
                .findFirst();
    }

    private Optional<Tripulante> findTripulante(Empresa empresa, IdPersona id) {

        return Arrays.stream(empresa.getTripulantes())
                .filter(t -> t.getIdPersona().equals(id))
                .findFirst();
    }

    private Optional<Bus> findBus(ArrayList<Bus> buses, String patente) {
        return buses.stream()
                .filter(b -> b.getPatente().equalsIgnoreCase(patente))
                .findFirst();
    }

    private Optional<Terminal> findTerminal(ArrayList<Terminal> terminales, String nombre) {
        return terminales.stream()
                .filter(t -> t.getNombre().equals(nombre))
                .findFirst();
    }

    private Nombre crearNombre(String tratamiento, String nombres, String paterno, String materno) {

        Nombre nombre = new Nombre();

        nombre.setTratamiento(Tratamiento.valueOf(tratamiento));

        nombre.setNombres(nombres);
        nombre.setApellidoPaterno(paterno);
        nombre.setApellidoMaterno(materno);

        return nombre;
    }


}
