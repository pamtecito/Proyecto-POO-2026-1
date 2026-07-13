package Vista;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;
import Controlador.*;
import Excepciones.SVPException;
import Modelo.TipoDocumento;
import Utilidades.*;
import Excepciones.*;

public class UISVP {
    private static UISVP instance;
    private Scanner sc = new Scanner(System.in);
    private static DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private UISVP(){}

    public static UISVP getInstance(){
        if (instance == null){
            return instance =  new UISVP();
        }
        return instance;
    }

    public void menu(){
        int opcion = 0;
        sc.useDelimiter("[\\t\\r\\n]+");
        do {
            System.out.println("\n==================================");
            System.out.println("...::: Menú principal :::...");
            System.out.println();

            System.out.println("1) Crear empresa");
            System.out.println("2) Contratar tripulante");
            System.out.println("3) Crear terminal");
            System.out.println("4) Crear cliente");
            System.out.println("5) Crear bus");
            System.out.println("6) Listar ventas");
            System.out.println("7) Listar viajes");
            System.out.println("8) Listar pasajeros de viaje");
            System.out.println("9) Listar ventas de empresa");
            System.out.println("10) Generar pasajes venta");
            System.out.println("-------------------------------------------");
            System.out.print("..:: Ingrese un número de opción: ");
            opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> createEmpresa();
                case 2 -> contrataTripulante();
                case 3 -> createTerminal();
                case 4 -> createCliente();
                case 5 -> createBus();
                case 6 -> listVentas();
                case 7 -> listViajes();
                case 8 -> listPasajeroViaje();
                case 9 -> listVentasEmpresa();
                case 10 -> generatePasajesVenta();
                default -> System.out.println("La opcion debe estar en el rango valido: (1 a 18)");
            }
        } while (opcion != 18);
    }

    private void createEmpresa(){
        System.out.println("\n...::::: Creando una nueva empresa :::::...");
        System.out.println("\n\t FORMATO RUT: XX.XXX.XXX-X");
        String strRut = leeRutValido("\t R.U.T : ");
        String nombre = leeStringNoVacio("\t Nombre : ");
        String url = leeStringNoVacio("\t url : ");
        try{
            Rut rut = Rut.of(strRut);
            ControladorEmpresa.getInstance().createEmpresa(rut, nombre, url);
            System.out.println();
            System.out.println("...::: Empresa guardada exitosamente :::...");
        } catch (SVPException e) {
            System.out.println(e.getMessage());
        }

    }

    private void contrataTripulante(){
        System.out.println("...::::: Contratando un nuevo Tripulante :::::...");
        System.out.println("\n :::: Dato de la Empresa");
        System.out.println("\t FORMATO RUT: XX.XXX.XXX-X");
        String strRutEmp = leeRutValido("\t R.U.T : ");
        Rut rutEmp = Rut.of(strRutEmp);

        System.out.println("\n :::: Datos tripulante");
        int tipo;
        do {
            tipo = leeIntNoNegativo("\t Auxiliar[1] o Conductor[2]: ");
            if (tipo < 1 || tipo > 2) System.out.println("Opción inválida, debe ser 1 o 2.");
        } while (tipo < 1 || tipo > 2);

        int tipoId;
        do {
            tipoId = leeIntNoNegativo("\t Rut[1] o Pasaporte[2]: ");
            if (tipoId < 1 || tipoId > 2) System.out.println("Opción inválida, debe ser 1 o 2.");
        } while (tipoId < 1 || tipoId > 2);

        IdPersona id;
        if (tipoId == 1) {
            System.out.println("\t FORMATO RUT: XX.XXX.XXX-X");
            String strRutTri = leeRutValido("\t R.U.T : ");
            id = Rut.of(strRutTri);
        } else {
            String numPasaporte = leeStringNoVacio("\t Numero Pasaporte: ");
            String nacionalidad = leeStringNoVacio("\t Nacionalidad: ");
            id = Pasaporte.of(numPasaporte, nacionalidad);
        }

        int tratamiento;
        do {
            tratamiento = leeIntNoNegativo("\t Sr.[1] o Sra.[2]: ");
            if (tratamiento < 1 || tratamiento > 2) System.out.println("Opción inválida, debe ser 1 o 2.");
        } while (tratamiento < 1 || tratamiento > 2);

        Tratamiento tra;
        if (tratamiento == 1){
            tra = Tratamiento.SR;
        } else {
            tra = Tratamiento.SRA;
        }
        String nombres = leeStringNoVacio("\t Nombres: ");
        String apellidoP = leeStringNoVacio("\t Apellido Paterno: ");
        String apellidoM = leeStringNoVacio("\t Apellido Materno: ");
        String calle = leeStringNoVacio("\t Calle: ");
        int numero = leeIntNoNegativo("\t Numero: ");
        String comuna = leeStringNoVacio("\t Comuna:");

        Nombre nom = new Nombre();
        nom.setTratamiento(tra);
        nom.setNombres(nombres);
        nom.setApellidoPaterno(apellidoP);
        nom.setApellidoMaterno(apellidoM);
        Direccion direccion = new Direccion(calle, numero, comuna);

        try {
            if (tipo == 1){
                ControladorEmpresa.getInstance().hireAuxiliarForEmpresa(rutEmp,id, nom, direccion );
                System.out.println("\n ...::::: Auxiliar contratado exitosamente :::::...");
            } else if ( tipo == 2){
                ControladorEmpresa.getInstance().hireConductorForEmpresa(rutEmp, id, nom, direccion);
                System.out.println("\n ...::::: Conductor contratado exitosamente :::::...");
            } else {
                System.out.println("Opcion Invalida :/");
            }
        } catch (SVPException e){
            System.out.println(e.getMessage());
        }
    }

    private void createTerminal(){
        System.out.println("\n ...::::: Creando un nuevo Terminal :::::...");
        String nombre = leeStringNoVacio("\t Nombre: ");
        String calle = leeStringNoVacio("\t Calle: ");
        int numero = leeIntNoNegativo("\t Numero: ");
        String comuna = leeStringNoVacio("\t Comuna: ");
        try {
            Direccion direccion = new Direccion(calle, numero, comuna);
            ControladorEmpresa.getInstance().createTerminal(nombre, direccion);
            System.out.println("\n ...::::: Terminal guardado exitosamente :::::...");
        } catch (SVPException e) {
            System.out.println(e.getMessage());
        }
    }


    private void createCliente(){
        int rutOpasaporte;
        IdPersona id = null;
        String strRut = "";
        System.out.println("\n...:::: Crear nuevo cliente ::::...");
        System.out.println();
        rutOpasaporte = leeIntNoNegativo("\t Rut[1] o Pasaporte[2] : ");
        do {
            if (rutOpasaporte != 1 && rutOpasaporte != 2) {
                System.out.println("Opcion fuera de rango, debe ser entre 1 o 2.");
                rutOpasaporte = leeIntNoNegativo("\t Rut[1] o Pasaporte[2] : ");
            }
        }while (rutOpasaporte != 1 && rutOpasaporte != 2);
        if (rutOpasaporte == 1) {
            System.out.println(" FORMATO RUT: XX.XXX.XXX-X");
            strRut = leeRutValido("\t R.U.T :");
        } else {
            System.out.println("\t Pasaporte :");
            String num = leeStringNoVacio("\t Numero : ");
            while (num.length() != 9){
                System.out.println("Cantidad de digitos incorrecta, ingrese nuevamente.");
                num = leeStringNoVacio("\t Numero: ");
            }
            String nacionalidad = leeStringNoVacio("\t Nacionalidad : ");
            id = Pasaporte.of(num, nacionalidad);
        }
        Nombre nombre = new Nombre();
        int srOSra;
        do {
            srOSra = leeIntNoNegativo("\t Sr.[1] o Sra.[2] : ");
            if (srOSra < 1 || srOSra > 2) System.out.println("Opción inválida, debe ser 1 o 2.");
        } while (srOSra < 1 || srOSra > 2);
        if (srOSra == 1){
            Tratamiento tratamiento = Tratamiento.SR;
            nombre.setTratamiento(tratamiento);
        } else {
            Tratamiento tratamiento = Tratamiento.SRA;
            nombre.setTratamiento(tratamiento);
        }

        String nombres = leeStringNoVacio("\t Nombres :");
        String apellidoPaterno = leeStringNoVacio("\t Apellido Paterno : ");
        String apellidoMaterno = leeStringNoVacio("\t Apellido Materno : ");
        nombre.setNombres(nombres);
        nombre.setApellidoPaterno(apellidoPaterno);
        nombre.setApellidoMaterno(apellidoMaterno);

        String telefono = leeStringNoVacio("\t Telefono movil : (ejemplo: 9XXXXXXXX)");
        String email = leeStringNoVacio("\t Email : ");
        try {
            if (rutOpasaporte == 1) {
                id = Rut.of(strRut);
            }
            SistemaVentaPasaje.getInstance().createCliente(id, nombre, telefono, email);
            System.out.println();
            System.out.println("...:::: Cliente guardado exitosamente ::::...");
        }catch (SVPException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createBus(){
        System.out.println("\n ...::::: Creando un nuevo Bus :::::...");
        String patente = leeStringNoVacio("\t Patente: ");
        String marca = leeStringNoVacio("\t Marca:");
        String modelo = leeStringNoVacio("\t Modelo: ");
        int nroAsientos = leeIntNoNegativo("\t Número de asientos: ");
        System.out.println("\n :::: Dato de la empresa");
        System.out.println("\t FORMATO R.U.T: XX.XXX.XXX-X");
        String strRut = leeRutValido("\t R.U.T: ");
        try {
            Rut rut = Rut.of(strRut);
            ControladorEmpresa.getInstance().createBus(patente, marca, modelo, nroAsientos, rut);
            System.out.println("\n ...::::: Bus creado existosamente :::::...");
        }catch (SVPException e){
            System.out.println(e.getMessage());
        }
    }

    private void listVentas() {
        System.out.println("\n ...::::: Listado de ventas :::::...");
        String[][] ventasTotales = SistemaVentaPasaje.getInstance().listVentas();
        if (ventasTotales.length == 0){
            System.out.println("No existen ventas registradas.");
            return;
        }

        System.out.printf("\n %-15s | %-10s | %-12s | %-20s | %-20s | %-10s | %-10s%n",
                "ID DOCUMENTO", "TIPO", "FECHA", "ID CLIENTE", "NOMBRE CLIENTE", "NRO PASAJES", "MONTO");
        System.out.println(" " + "-".repeat(105));

        for (String[] venta : ventasTotales) {
            System.out.printf(" %-15s | %-10s | %-12s | %-20s | %-20s | %-10s | %-10s%n",
                    venta[0], venta[1], venta[2], venta[3], venta[4], venta[5], venta[6]);
        }
    }

    private void listViajes(){
        System.out.println("\n ...::::: Listado de viajes :::::...");

        String[][] viajes = SistemaVentaPasaje.getInstance().listViajes();

        if (viajes.length == 0) {
            System.out.println("No existen viajes registrados.");
            return;
        }

        System.out.printf("\n %-12s | %-10s | %-10s | %-8s | %-15s | %-10s | %-12s | %-12s%n",
                "FECHA", "HORA SALE", "HORA LLEGA", "PRECIO", "ASIENTOS DISP.", "PATENTE", "ORIGEN", "DESTINO");
        System.out.println(" " + "-".repeat(105));

        for (String[] viaje : viajes) {
            System.out.printf(" %-12s | %-10s | %-10s | %-8s | %-15s | %-10s | %-12s | %-12s%n",
                    viaje[0], viaje[1], viaje[2], viaje[3], viaje[4], viaje[5], viaje[6], viaje[7]);
        }
    }

    private void listPasajeroViaje(){
        System.out.println("\n ...::::: Listado de pasajeros de un viaje :::::...");

        String fechaStr = leeFechaValida("\t Fecha[dd/mm/yyyy] : ");
        LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String horaStr = leeStringNoVacio("\t Hora[hh:mm] : ");
        LocalTime hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));

        String patente = leeStringNoVacio("\t Patente Bus : ").toUpperCase();

        try {
            String[][] pasajeros = SistemaVentaPasaje.getInstance().listPasajerosViaje(fecha, hora, patente);

            if (pasajeros.length == 0) {
                System.out.println("\n *** No existen pasajeros registrados para este viaje ***");
                return;
            }

            System.out.printf("\n %-15s | %-25s | %-25s | %-15s%n",
                    "ID", "NOMBRE", "NOMBRE CONTACTO", "FONO CONTACTO");
            System.out.println(" " + "-".repeat(85));

            for (String[] pasajero : pasajeros) {
                System.out.printf(" %-15s | %-25s | %-25s | %-15s%n",
                        pasajero[0], pasajero[1], pasajero[2], pasajero[3]);
            }

        } catch (SVPException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listVentasEmpresa(){
        System.out.println("\n ...::::: Listado de ventas de una empresa :::::...");

        System.out.println("\t FORMATO RUT: XX.XXX.XXX-X");
        String strRut = leeRutValido("\t R.U.T : ");
        Rut rut = Rut.of(strRut);

        try {
            String[][] ventas = ControladorEmpresa.getInstance().listVentasEmpresa(rut);

            if (ventas.length == 0) {
                System.out.println("\n *** No existen ventas registradas para esta empresa ***");
                return;
            }

            System.out.printf("\n %-12s | %-10s | %-15s | %-15s%n",
                    "FECHA", "TIPO", "MONTO PAGADO", "TIPO PAGO");
            System.out.println(" " + "-".repeat(60));

            for (String[] venta : ventas) {
                System.out.printf(" %-12s | %-10s | %-15s | %-15s%n",
                        venta[0], venta[1], venta[2], venta[3]);
            }

        } catch (SVPException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean verFormato(String fecha) {
        if (fecha.length() != 10) {
            return false;
        }
        if (fecha.charAt(2) != '/' || fecha.charAt(5) != '/') {
            return false;
        }
        for (int i = 0; i < fecha.length(); i++) {
            if (i != 2 && i != 5) {
                char esNum = fecha.charAt(i);
                if (esNum < '0' || esNum > '9') {
                    return false;
                }
            }
        }
        return true;
    }

    private void verFormatoRut(String rut) {
        //Formatos = 1.111.111-1 11.111.111-1
        char dv;
        char guion;
        if  (rut.length() < 11 || rut.length() > 12) {
            throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
        }
        if (rut.length() == 11) {
            if (rut.charAt(1) != '.' || rut.charAt(5) != '.') {
                throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
            }
            dv = rut.charAt(10);
            guion = rut.charAt(9);
            if (!((dv >= '0' && dv <= '9') || dv == 'k' || dv == 'K')) {
                throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
            }
            if (guion != '-') {
                throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
            }
        }

        if  (rut.length() == 12) {
            if (rut.charAt(2) != '.' || rut.charAt(6) != '.') {
                throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
            }
            dv = rut.charAt(11);
            guion = rut.charAt(10);
            if (!((dv >= '0' && dv <= '9') || dv == 'k' || dv == 'K')) {
                throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
            }
            if (guion != '-') {
                throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
            }
        }
        for (int i = 0; i < rut.length() - 2; i++) {
            char caracter = rut.charAt(i);
            if (caracter != '.' && (caracter < '0' || caracter > '9')) {
                throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
            }
        }
    }
    private String leeStringNoVacio(String mensaje) {
        String dato;
        do {
            System.out.print(mensaje);
            dato = sc.next();
            if (dato.isBlank()) {
                System.out.println("El "+ mensaje.toLowerCase().replace(":", "").trim()+ " no puede estar vacio.");
            }
        } while (dato.isBlank());
        return dato;
    }
    private float leeFloatNoNegativo(String mensaje) {

        float nroNoNegativo = 0f;
        boolean datoValido = false;
        do {
            try {
                System.out.print(mensaje);
                nroNoNegativo = sc.nextFloat();
                if (nroNoNegativo >= 0) {
                    datoValido = true;
                } else {
                    System.out.println("El "+ mensaje.toLowerCase().replace(":", "").trim() + " numérico no puede ser negativo.");
                }
            } catch (InputMismatchException e) {
                sc.next();
                System.out.println("El " + mensaje.toLowerCase().replace(":", "").trim() + " debe ser un dígito numérico.");
            }
        } while (!datoValido);
        return nroNoNegativo;
    }

    private int leeIntNoNegativo(String mensaje) {
        int nroNoNegativo = 0;
        boolean datoValido = false;
        do {
            try {
                System.out.print(mensaje);
                nroNoNegativo = sc.nextInt();
                if (nroNoNegativo >= 0) {
                    datoValido = true;
                } else {
                    System.out.println("El "+ mensaje.toLowerCase().replace(":", "").trim() + " numérico no puede ser negativo.");
                }
            } catch (InputMismatchException e) {
                sc.next();
                System.out.println("El "+mensaje.toLowerCase().replace(":", "").trim()+" debe ser un dígito numérico.");
            }
        } while (!datoValido);
        return nroNoNegativo;
    }

    private String leeRutValido(String mensaje) {
        String rut = "";
        boolean datoValido = false;
        do {
            System.out.print(mensaje);
            rut = sc.next();
            try {
                verFormatoRut(rut);
                datoValido = true;
            } catch (SVPException e) {
                System.out.println(e.getMessage());
            }
        } while (!datoValido);
        return rut;
    }

    private String leeFechaValida(String mensaje) {
        String fecha = "";
        boolean datoValido = false;
        do {
            System.out.print(mensaje);
            fecha = sc.next();

            if (verFormato(fecha)) {
                datoValido = true;
            } else {
                System.out.println("El formato de fecha es incorrecto (debe ser DD/MM/YYYY).");
            }
        } while (!datoValido);
        return fecha;
    }

    private long leeLongNoNegativo(String mensaje) {
        long nroNoNegativo = 0L;
        boolean datoValido = false;
        do {
            try {
                System.out.print(mensaje);
                nroNoNegativo = sc.nextLong();
                if (nroNoNegativo >= 0) {
                    datoValido = true;
                } else {
                    System.out.println("El número ingresado no puede ser negativo.");
                }
            } catch (InputMismatchException e) {
                sc.next();
                System.out.println("Error: Debe ingresar solo dígitos numéricos.");
            }
        } while (!datoValido);
        return nroNoNegativo;
    }

    private void generatePasajesVenta() {
        System.out.println("\n ...::::: Generando Pasajes de Venta :::::...");
        String idDoc = leeStringNoVacio("\t ID Documento: ");
        int tipoDoc;
        do {
            tipoDoc = leeIntNoNegativo("\t Tipo de documento Boleta[1] o Factura[2]: ");
            if (tipoDoc < 1 || tipoDoc > 2) System.out.println("\tDebe ser 1 o 2.");
        } while (tipoDoc < 1 || tipoDoc > 2);
        TipoDocumento tipo = (tipoDoc == 1) ? TipoDocumento.BOLETA : TipoDocumento.FACTURA;
        try {
            SistemaVentaPasaje.getInstance().generatePasajesVenta(idDoc, tipo);
            System.out.println("\n ...::::: Pasajes generados correctamente :::::...");
        } catch (SVPException e) {
            System.out.println(e.getMessage());
        }
    }

}