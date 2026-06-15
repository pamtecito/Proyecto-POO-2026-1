package Vista;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;
import Controlador.*;
import Excepciones.SistemaVentaPasajesException;
import Modelo.TipoDocumento;
import Utilidades.*;

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
        cargarDatosPrueba();
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
            System.out.println("6) Crear viaje");
            System.out.println("7) Vender pasajes");
            System.out.println("8) Listar ventas");
            System.out.println("9) Listar viajes");
            System.out.println("10) Listar pasajeros de viaje");
            System.out.println("11) Listar empresas");
            System.out.println("12) Listar llegadas/salidas de terminal");
            System.out.println("13) Listar ventas de empresa");
            System.out.println("14) Salir");
            System.out.println("-------------------------------------------");
            System.out.print("..:: Ingrese un número de opción: ");
            opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> createEmpresa();
                case 2 -> contrataTripulante();
                case 3 -> createTerminal();
                case 4 -> createCliente();
                case 5 -> createBus();
                case 6 -> createViaje();
                case 7 -> vendePasajes();
                case 8 -> listVentas();
                case 9 -> listViajes();
                case 10 -> listPasajeroViaje();
                case 11 -> listEmpresas();
                case 12 -> listLLegadasSalidasTerminal();
                case 13 -> listVentasEmpresa();
                default -> System.out.println("La opcion debe estar en el rango valido: (1 a 14)");
            }
        }while (opcion != 14);
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
        } catch (SistemaVentaPasajesException e) {
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
        } catch (SistemaVentaPasajesException e) {
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
        } catch (SistemaVentaPasajesException e){
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
        }catch (SistemaVentaPasajesException e) {
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
        }catch (SistemaVentaPasajesException e){
            System.out.println(e.getMessage());
        }
    }

    private void createViaje() {
        System.out.println("\n ...::::: Creando un nuevo viaje :::::...");
        String fechaStr = leeFechaValida("\t Fecha viaje: ");
        LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String hraString = leeStringNoVacio("\t Hora[hh:mm] : ");
        LocalTime hora = LocalTime.parse(hraString, DateTimeFormatter.ofPattern("HH:mm"));
        int precio = leeIntNoNegativo("\t Precio: ");
        int duracion = leeIntNoNegativo("\t Duracion (minutos) : ");
        String patente = leeStringNoVacio("\t Patente Bus : ").toUpperCase();
        int nroConductores = 0;
        do {
            nroConductores = leeIntNoNegativo("\t Nro. de conductores : ");
            if (nroConductores < 1 || nroConductores > 2) {
                System.out.println("El número de conductores debe ser 1 o 2.");
            }
        } while (nroConductores < 1 || nroConductores > 2);
        IdPersona[] idTripulantes = new IdPersona[1 + nroConductores];
        System.out.println("\n\t :: Id Auxiliar:");
        int tipoIdAuxiliar;
        do {
            tipoIdAuxiliar = leeIntNoNegativo("\t Rut[1] o Pasaporte[2] : ");
            if (tipoIdAuxiliar < 1 || tipoIdAuxiliar > 2 ) System.out.println("Opción Invalida. Debe ser 1 o 2.");
        } while (tipoIdAuxiliar < 1 || tipoIdAuxiliar > 2);
        if (tipoIdAuxiliar == 1){
            System.out.println("\t FORMATO RUT: XX.XXX.XXX-X");
            String strRut = leeRutValido("\t R.U.T : ");
            idTripulantes[0] = Rut.of(strRut);
        } else {
            String numPasaporte = leeStringNoVacio("\t Numero Pasaporte:");
            while (numPasaporte.length() != 9){
                System.out.println("Cantidad de digitos incorrecta.");
                numPasaporte = leeStringNoVacio("\t Numero Pasaporte: ");
            }
            String nacionalidad = leeStringNoVacio("\t Nacionalidad:");
            idTripulantes[0] = Pasaporte.of(numPasaporte, nacionalidad);
        }

        for (int i = 1; i <= nroConductores; i++) {
            System.out.println("\n\t :: Id Conductor ::");
            int tipoIdConductor;
            do{
                tipoIdConductor = leeIntNoNegativo("\t Rut[1] o Pasaporte[2] : ");
                if (tipoIdConductor < 1 || tipoIdConductor > 2 ) System.out.println("Opción Invalida. Debe ser 1 o 2.");
            }while (tipoIdConductor < 1 || tipoIdConductor > 2 );
            if (tipoIdConductor == 1){
                System.out.println("\t FORMATO RUT: XX.XXX.XXX-X");
                String strRut = leeRutValido("\t R.U.T : ");
                idTripulantes[i] = Rut.of(strRut);
            } else {
                String numPasaporte = leeStringNoVacio("\t Numero Pasaporte:");
                while (numPasaporte.length() != 9){
                    System.out.println("Cantidad de digitos incorrecta.");
                    numPasaporte = leeStringNoVacio("\t Numero Pasaporte: ");
                }
                String nacionalidad = leeStringNoVacio("\t Nacionalidad:");
                idTripulantes[i] = Pasaporte.of(numPasaporte, nacionalidad);
            }
        }

        String comunaSalida = leeStringNoVacio("\t Nombre comuna salida: ");
        String comunaLlegada = leeStringNoVacio("\t Nombre comuna llegada: ");
        String[] comunas = {comunaSalida, comunaLlegada};

        try {
            SistemaVentaPasaje.getInstance().createViaje(fecha, hora, precio, duracion, patente, idTripulantes, comunas);
            System.out.println("\n ...::::: Viaje creado exitosamente :::::...");
        }catch (SistemaVentaPasajesException e){
            System.out.println(e.getMessage());
        }
    }

    private void vendePasajes() {
        TipoDocumento tipo, tipo2;
        boolean pasajero;
        int rutOpasaporte, rutOpasaporte2;
        String strRut = "", strRut2 = "";
        IdPersona id = null, id2 = null;
        System.out.println("\n ...:::: Venta de pasajes ::::...");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(":::: Datos de la Venta");
        String idDoc = leeStringNoVacio("\t ID Documento : ");
        int tipoDoc = leeIntNoNegativo("Tipo de documento: [1] Boleta [2] Factura : ");
        do {
            if (tipoDoc != 1 && tipoDoc != 2) {
                System.out.println("El tipo de documento debe ser 1 o 2");
                tipoDoc = leeIntNoNegativo("Tipo de documento: [1] Boleta [2] Factura : ");
            }
        } while (tipoDoc != 1 && tipoDoc != 2);
        if (tipoDoc == 1){
            tipo = TipoDocumento.BOLETA;
        } else {
            tipo = TipoDocumento.FACTURA;
        }
        String strFechaVenta = leeFechaValida("Fecha de venta[dd/mm/yyyy] : ");
        String comunaOrigen = leeStringNoVacio("\t Origen (comuna) : ");
        String comunaDestino = leeStringNoVacio("\t Destino (comuna) : ");
        LocalDate fVenta = LocalDate.parse(strFechaVenta, formato);
        System.out.println();
        System.out.println(":::: Datos del cliente");
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
        if (rutOpasaporte == 1){
            id = Rut.of(strRut);
        }
        System.out.println(":::: Pasajes a vender");
        int cantPasajes = leeIntNoNegativo("\t Cantidad de pasajes : ");

        try {
            SistemaVentaPasaje.getInstance().iniciaVenta(idDoc, tipo,fVenta, comunaOrigen, comunaDestino, id, cantPasajes);
            System.out.println("Venta iniciada.");
        } catch (SistemaVentaPasajesException e){
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("\n:::: Listado de horarios disponibles");
        String[][] horarios = SistemaVentaPasaje.getInstance().getHorariosDisponibles(fVenta, comunaOrigen, comunaDestino, cantPasajes);
        System.out.printf("\n %-5s | %-10s | %-10s | %-8s | %-10s%n", "NRO", "BUS", "SALIDA", "VALOR", "ASIENTOS");
        System.out.println(" " + "-".repeat(55));
        for (int i = 0; i < horarios.length; i++) {
            System.out.printf(" %-5s | %-10s | %-10s | %-8s | %-10s%n",
                    (i + 1), horarios[i][0], horarios[i][1], horarios[i][2], horarios[i][3]);
        }
        int opViaje = leeIntNoNegativo("\t Seleccione viaje [1.."+horarios.length+ "] : ");
        do {
            if (opViaje < 1 || opViaje > horarios.length) {
                System.out.println("Debe ingresar un viaje dentro del rango [1.."+horarios.length+"] : ");
                opViaje = leeIntNoNegativo("\t Seleccione viaje [1.."+horarios.length+ "] : ");
            }
        } while (opViaje < 1 || opViaje > horarios.length);
        String patBus = horarios[opViaje - 1][0];
        LocalTime hora = LocalTime.parse(horarios[opViaje - 1][1]);
        try {
            String[] asientos = SistemaVentaPasaje.getInstance().listAsientosDeViaje(fVenta, hora, patBus);
            System.out.println("\n :::: Asientos disponibles para el viaje seleccionado");
            System.out.println(" *---*---*---*---*---*");
            for (int i = 0; i < asientos.length; i += 4) {
                String a1, a2, a3, a4;

                if (i < asientos.length) {
                    a1 = asientos[i];
                } else {
                    a1 = " ";
                }

                if (i + 1 < asientos.length) {
                    a2 = asientos[i + 1];
                } else {
                    a2 = " ";
                }

                if (i + 2 < asientos.length) {
                    a3 = asientos[i + 2];
                } else {
                    a3 = " ";
                }

                if (i + 3 < asientos.length) {
                    a4 = asientos[i + 3];
                } else {
                    a4 = " ";
                }
                System.out.printf(" | %2s| %2s|   | %2s| %2s|%n", a1, a2, a4, a3);
                System.out.println(" |---+---+---+---+---|");
            }

            System.out.println(" *---*---*---*---*---*");
            System.out.print("\t Seleccione sus asientos [separe por ,] : ");
            String[] asientosSeleccionados = sc.next().split(",");
            int contPasajero = 1;
            for (String asientoStr : asientosSeleccionados) {
                int asiento = Integer.parseInt(asientoStr.trim());
                System.out.println("\n :::: Datos pasajeros " + contPasajero);
                rutOpasaporte2 = leeIntNoNegativo("\t Rut[1] o Pasaporte[2] : ");
                do {
                    if (rutOpasaporte2 != 1 && rutOpasaporte2 != 2) {
                        System.out.println("Opcion fuera de rango, debe ser entre 1 o 2.");
                        rutOpasaporte2 = leeIntNoNegativo("\t Rut[1] o Pasaporte[2] : ");
                    }
                }while (rutOpasaporte2 != 1 && rutOpasaporte2 != 2);
                if (rutOpasaporte2 == 1) {
                    System.out.println(" FORMATO RUT: XX.XXX.XXX-X");
                    strRut2 = leeRutValido("\t R.U.T :");
                } else {
                    System.out.println("\t Pasaporte :");
                    String num2 = leeStringNoVacio("\t Numero : ");
                    while (num2.length() != 9){
                        System.out.println("Cantidad de digitos incorrecta, ingrese nuevamente.");
                        num2 = leeStringNoVacio("\t Numero: ");
                    }
                    String nacionalidad2 = leeStringNoVacio("\t Nacionalidad : ");
                    id2 = Pasaporte.of(num2, nacionalidad2);
                }
                if (rutOpasaporte2 == 1){
                    id2 = Rut.of(strRut2);
                }
                Optional<String> nombrePasajero = SistemaVentaPasaje.getInstance().getNombrePasajero(id2);
                if (nombrePasajero.isEmpty()) {
                    System.out.println("\n>>> El pasajero no existe. Ingrese los datos para crearlo:");

                    Nombre nomPasajero = new Nombre();
                    int trat;
                    do {
                        trat = leeIntNoNegativo("\t Sr.[1] o Sra.[2] : ");
                    } while (trat != 1 && trat != 2);
                    nomPasajero.setTratamiento(trat == 1 ? Tratamiento.SR : Tratamiento.SRA);

                    nomPasajero.setNombres(leeStringNoVacio("\t Nombres : "));
                    nomPasajero.setApellidoPaterno(leeStringNoVacio("\t Apellido Paterno : "));
                    nomPasajero.setApellidoMaterno(leeStringNoVacio("\t Apellido Materno : "));

                    String fonoPasajero = leeStringNoVacio("\t Telefono movil : ");

                    System.out.println("\t --- Datos Contacto Emergencia ---");
                    Nombre nomContacto = new Nombre();
                    nomContacto.setNombres(leeStringNoVacio("\t Nombre del contacto : "));
                    String fonoContacto = leeStringNoVacio("\t Telefono del contacto : ");

                    SistemaVentaPasaje.getInstance().createPasajero(id2, nomPasajero, fonoPasajero, nomContacto, fonoContacto);
                }
                SistemaVentaPasaje.getInstance().vendePasaje(idDoc, tipo, fVenta, hora, patBus, asiento, id2);
                System.out.println("\n:::: Pasaje agregado exitosamente");
                contPasajero++;
            }
            Optional<Integer> montoTotal = SistemaVentaPasaje.getInstance().getMontoVenta(idDoc, tipo);
            if (montoTotal.isPresent()) {
                System.out.println("\n:::: Monto total de la venta: $" + montoTotal.get());
                pagaVentaPasajes();
            }
        } catch (SistemaVentaPasajesException e){
            System.out.println(e.getMessage());
        }

    }


    private void pagaVentaPasajes(){
        System.out.println("\n ...::::: Pago de venta :::::...");
        String idDoc = leeStringNoVacio("\t ID Documento: ");
        int tipoDoc;
        do {
            tipoDoc = leeIntNoNegativo("\t Tipo documento: Boleta[1] o Factura[2] : ");
            if (tipoDoc < 1 || tipoDoc > 2){
                System.out.println("Opción invalida. Debe ser 1 o 2.");
            }
        }while (tipoDoc < 1 || tipoDoc > 2);

        TipoDocumento tipo;
         if (tipoDoc == 1){
             tipo = TipoDocumento.BOLETA;
         } else {
             tipo = TipoDocumento.FACTURA;
         }

         int tipoPago;
         do{
             tipoPago = leeIntNoNegativo("\t Efectivo[1] o Tarjeta[2]: ");
             if (tipoPago < 1 || tipoPago > 2){
                 System.out.println("Opcion invalida. Debe ser 1 o 2");
             }
         }while (tipoPago < 1 || tipoPago > 2);

         try {
             if (tipoPago == 1){
                 SistemaVentaPasaje.getInstance().pagaVenta(idDoc, tipo);
             }else {
                 long nroTarjeta =  leeIntNoNegativo("\t Numero de tarjeta: ");
                 SistemaVentaPasaje.getInstance().pagaVenta(idDoc, tipo, nroTarjeta);
             }
             System.out.println("\n ...::::: Venta realizada exitosamente :::::...");
         }catch (SistemaVentaPasajesException e) {
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

        } catch (SistemaVentaPasajesException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listEmpresas(){
        System.out.println("\n ...::::: Listado de empresas :::::...");

        String[][] empresas = ControladorEmpresa.getInstance().listEmpresas();

        if (empresas.length == 0) {
            System.out.println("No existen empresas registradas.");
            return;
        }

        System.out.printf("\n %-15s | %-30s | %-30s | %-17s | %-10s | %-12s%n",
                "RUT EMPRESA", "NOMBRE", "URL", "NRO. TRIPULANTES", "NRO. BUSES", "NRO. VENTAS");
        System.out.println(" " + "-".repeat(120));

        for (String[] empresa : empresas) {
            System.out.printf(" %-15s | %-30s | %-30s | %-17s | %-10s | %-12s%n",
                    empresa[0], empresa[1], empresa[2], empresa[3], empresa[4], empresa[5]);
        }
    }

    private void listLLegadasSalidasTerminal(){
        System.out.println("\n ...::::: Listado de llegadas y salidas de un terminal :::::...");

        String nombre = leeStringNoVacio("\t Nombre terminal : ");
        String fechaStr = leeFechaValida("\t Fecha[dd/mm/yyyy] : ");
        LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        try {
            String[][] viajes = ControladorEmpresa.getInstance().listLlegadasSalidasTerminal(nombre, fecha);

            if (viajes.length == 0) {
                System.out.println("\n *** No existen llegadas ni salidas para ese terminal en la fecha dada ***");
                return;
            }

            System.out.printf("\n %-15s | %-8s | %-12s | %-30s | %-15s%n",
                    "LLEGADA/SALIDA", "HORA", "PATENTE BUS", "NOMBRE EMPRESA", "NRO. PASAJEROS");
            System.out.println(" " + "-".repeat(90));

            for (String[] viaje : viajes) {
                System.out.printf(" %-15s | %-8s | %-12s | %-30s | %-15s%n",
                        viaje[0], viaje[1], viaje[2], viaje[3], viaje[4]);
            }

        } catch (SistemaVentaPasajesException e) {
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

        } catch (SistemaVentaPasajesException e) {
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
            throw new SistemaVentaPasajesException("El rut ingresado no es correcto (no cumple con el formato)");
        }
        if (rut.length() == 11) {
            if (rut.charAt(1) != '.' || rut.charAt(5) != '.') {
                throw new SistemaVentaPasajesException("El rut ingresado no es correcto (no cumple con el formato)");
            }
            dv = rut.charAt(10);
            guion = rut.charAt(9);
            if (!((dv >= '0' && dv <= '9') || dv == 'k' || dv == 'K')) {
                throw new SistemaVentaPasajesException("El rut ingresado no es correcto (no cumple con el formato)");
            }
            if (guion != '-') {
                throw new SistemaVentaPasajesException("El rut ingresado no es correcto (no cumple con el formato)");
            }
        }

        if  (rut.length() == 12) {
            if (rut.charAt(2) != '.' || rut.charAt(6) != '.') {
                throw new SistemaVentaPasajesException("El rut ingresado no es correcto (no cumple con el formato)");
            }
            dv = rut.charAt(11);
            guion = rut.charAt(10);
            if (!((dv >= '0' && dv <= '9') || dv == 'k' || dv == 'K')) {
                throw new SistemaVentaPasajesException("El rut ingresado no es correcto (no cumple con el formato)");
            }
            if (guion != '-') {
                throw new SistemaVentaPasajesException("El rut ingresado no es correcto (no cumple con el formato)");
            }
        }
        for (int i = 0; i < rut.length() - 2; i++) {
            char caracter = rut.charAt(i);
            if (caracter != '.' && (caracter < '0' || caracter > '9')) {
                throw new SistemaVentaPasajesException("El rut ingresado no es correcto (no cumple con el formato)");
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
            } catch (SistemaVentaPasajesException e) {
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

    private void cargarDatosPrueba() {

        // ── EMPRESAS ─────────────────────────────────────────────────
        try {
            Rut rutBusesNuble = Rut.of("88.888.888-8");
            ControladorEmpresa.getInstance().createEmpresa(rutBusesNuble, "Buses Ñuble", "https://www.busesnuble.cl");

            Rut rutEfeBus = Rut.of("99.999.999-9");
            ControladorEmpresa.getInstance().createEmpresa(rutEfeBus, "Efe Bus", "https://www.efebus.cl");

            Rut rutTurbus = Rut.of("77.777.777-7");
            ControladorEmpresa.getInstance().createEmpresa(rutTurbus, "Turbus", "https://www.turbus.cl");
        } catch (SistemaVentaPasajesException e) {
            System.out.println("DatosPrueba empresa: " + e.getMessage());
        }

        // ── TERMINALES ────────────────────────────────────────────────
        try {
            ControladorEmpresa.getInstance().createTerminal("Fray Silvano",
                    new Direccion("Almirante Gaete", 1240, "Chillan"));

            ControladorEmpresa.getInstance().createTerminal("El Pinar",
                    new Direccion("Arturo Prat", 500, "Pinto"));

            ControladorEmpresa.getInstance().createTerminal("Terminal Medina",
                    new Direccion("O'Higgins", 320, "Medina"));

            ControladorEmpresa.getInstance().createTerminal("Terminal Santa Faz",
                    new Direccion("Los Carrera", 800, "Santa Faz"));

            ControladorEmpresa.getInstance().createTerminal("Terminal Bulnes",
                    new Direccion("Yungay", 150, "Bulnes"));
        } catch (SistemaVentaPasajesException e) {
            System.out.println("DatosPrueba terminal: " + e.getMessage());
        }

        // ── BUSES ─────────────────────────────────────────────────────
        try {
            Rut rutBN = Rut.of("88.888.888-8");
            ControladorEmpresa.getInstance().createBus("ABCD-12", "Mercedes", "Marcopolo", 45, rutBN);
            ControladorEmpresa.getInstance().createBus("EFGH-34", "Mercedes", "Paradiso",  52, rutBN);
            ControladorEmpresa.getInstance().createBus("IJKL-56", "Volvo",    "Novo",      40, rutBN);
            ControladorEmpresa.getInstance().createBus("MNOP-78", "Scania",   "Irizar",    38, rutBN);
            ControladorEmpresa.getInstance().createBus("QRST-90", "Volvo",    "9800",      44, rutBN);

            Rut rutEB = Rut.of("99.999.999-9");
            ControladorEmpresa.getInstance().createBus("AZYB-12", "Mercedes", "Novo", 50, rutEB);

            Rut rutTB = Rut.of("77.777.777-7");
            ControladorEmpresa.getInstance().createBus("TURB-01", "Scania", "Touring", 46, rutTB);
        } catch (SistemaVentaPasajesException e) {
            System.out.println("DatosPrueba bus: " + e.getMessage());
        }

        // ── TRIPULANTES ───────────────────────────────────────────────
        try {
            Rut rutEmp = Rut.of("88.888.888-8");

            // Auxiliar de Buses Ñuble
            Nombre nomAux = new Nombre();
            nomAux.setTratamiento(Tratamiento.SRA);
            nomAux.setNombres("Maria Jose");
            nomAux.setApellidoPaterno("Perez");
            nomAux.setApellidoMaterno("Soto");
            ControladorEmpresa.getInstance().hireAuxiliarForEmpresa(
                    rutEmp,
                    Rut.of("55.555.555-5"),
                    nomAux,
                    new Direccion("Almirante Gaete", 890, "Chillan"));

            // Conductor 1 de Buses Ñuble
            Nombre nomCond1 = new Nombre();
            nomCond1.setTratamiento(Tratamiento.SR);
            nomCond1.setNombres("Carlos Andres");
            nomCond1.setApellidoPaterno("Gonzalez");
            nomCond1.setApellidoMaterno("Muñoz");
            ControladorEmpresa.getInstance().hireConductorForEmpresa(
                    rutEmp,
                    Rut.of("77.777.777-7"),
                    nomCond1,
                    new Direccion("Santa Rosa", 123, "Bulnes"));

            // Conductor 2 de Buses Ñuble
            Nombre nomCond2 = new Nombre();
            nomCond2.setTratamiento(Tratamiento.SR);
            nomCond2.setNombres("Diego Antonio");
            nomCond2.setApellidoPaterno("Galindo");
            nomCond2.setApellidoMaterno("Torres");
            ControladorEmpresa.getInstance().hireConductorForEmpresa(
                    rutEmp,
                    Rut.of("66.666.666-6"),
                    nomCond2,
                    new Direccion("Los Robles", 456, "Chillan"));

            // Tripulantes Efe Bus
            Rut rutEB = Rut.of("99.999.999-9");

            Nombre nomAux2 = new Nombre();
            nomAux2.setTratamiento(Tratamiento.SRA);
            nomAux2.setNombres("Ana Paula");
            nomAux2.setApellidoPaterno("Fuentes");
            nomAux2.setApellidoMaterno("Castro");
            ControladorEmpresa.getInstance().hireAuxiliarForEmpresa(
                    rutEB,
                    Rut.of("33.333.333-3"),
                    nomAux2,
                    new Direccion("O'Higgins", 77, "Medina"));

            Nombre nomCond3 = new Nombre();
            nomCond3.setTratamiento(Tratamiento.SR);
            nomCond3.setNombres("Pedro Luis");
            nomCond3.setApellidoPaterno("Ramirez");
            nomCond3.setApellidoMaterno("Vega");
            ControladorEmpresa.getInstance().hireConductorForEmpresa(
                    rutEB,
                    Rut.of("44.444.444-4"),
                    nomCond3,
                    new Direccion("Arturo Prat", 210, "Pinto"));

        } catch (SistemaVentaPasajesException e) {
            System.out.println("DatosPrueba tripulante: " + e.getMessage());
        }

        // ── CLIENTES ──────────────────────────────────────────────────
        try {
            Nombre nomC1 = new Nombre();
            nomC1.setTratamiento(Tratamiento.SR);
            nomC1.setNombres("Juan Pablo");
            nomC1.setApellidoPaterno("Morales");
            nomC1.setApellidoMaterno("Diaz");
            SistemaVentaPasaje.getInstance().createCliente(
                    Rut.of("11.111.111-1"), nomC1, "+56912345678", "jpmorales@gmail.com");

            Nombre nomC2 = new Nombre();
            nomC2.setTratamiento(Tratamiento.SRA);
            nomC2.setNombres("Valentina");
            nomC2.setApellidoPaterno("Rojas");
            nomC2.setApellidoMaterno("Lopez");
            SistemaVentaPasaje.getInstance().createCliente(
                    Rut.of("22.222.222-2"), nomC2, "+56987654321", "vrojas@hotmail.com");

            Nombre nomC3 = new Nombre();
            nomC3.setTratamiento(Tratamiento.SR);
            nomC3.setNombres("Lucas");
            nomC3.setApellidoPaterno("Fernandez");
            nomC3.setApellidoMaterno("Gomez");
            SistemaVentaPasaje.getInstance().createCliente(
                    Pasaporte.of("P12345678", "ARG"), nomC3, "+54911111111", "lfernandez@mail.ar");

        } catch (SistemaVentaPasajesException e) {
            System.out.println("DatosPrueba cliente: " + e.getMessage());
        }

        // ── PASAJEROS ─────────────────────────────────────────────────
        try {
            Nombre nomP1 = new Nombre();
            nomP1.setTratamiento(Tratamiento.SR);
            nomP1.setNombres("Juan Pablo");
            nomP1.setApellidoPaterno("Morales");
            nomP1.setApellidoMaterno("Diaz");
            Nombre contactoP1 = new Nombre();
            contactoP1.setNombres("Rosa Diaz");
            SistemaVentaPasaje.getInstance().createPasajero(
                    Rut.of("11.111.111-1"), nomP1, "+56912345678", contactoP1, "+56912000001");

            Nombre nomP2 = new Nombre();
            nomP2.setTratamiento(Tratamiento.SRA);
            nomP2.setNombres("Valentina");
            nomP2.setApellidoPaterno("Rojas");
            nomP2.setApellidoMaterno("Lopez");
            Nombre contactoP2 = new Nombre();
            contactoP2.setNombres("Claudio Lopez");
            SistemaVentaPasaje.getInstance().createPasajero(
                    Rut.of("22.222.222-2"), nomP2, "+56987654321", contactoP2, "+56912000002");

            Nombre nomP3 = new Nombre();
            nomP3.setTratamiento(Tratamiento.SR);
            nomP3.setNombres("Sebastian");
            nomP3.setApellidoPaterno("Castro");
            nomP3.setApellidoMaterno("Pino");
            Nombre contactoP3 = new Nombre();
            contactoP3.setNombres("Lucia Pino");
            SistemaVentaPasaje.getInstance().createPasajero(
                    Rut.of("33.333.333-3"), nomP3, "+56955555555", contactoP3, "+56912000003");

            Nombre nomP4 = new Nombre();
            nomP4.setTratamiento(Tratamiento.SRA);
            nomP4.setNombres("Camila");
            nomP4.setApellidoPaterno("Vega");
            nomP4.setApellidoMaterno("Rios");
            Nombre contactoP4 = new Nombre();
            contactoP4.setNombres("Mario Rios");
            SistemaVentaPasaje.getInstance().createPasajero(
                    Rut.of("44.444.444-4"), nomP4, "+56944444444", contactoP4, "+56912000004");

        } catch (SistemaVentaPasajesException e) {
            System.out.println("DatosPrueba pasajero: " + e.getMessage());
        }

        // ── VIAJES ────────────────────────────────────────────────────
        // idTripulantes[]: [0]=auxiliar, [1]=conductor (,[2]=conductor2 opcional)
        // comunas[]      : [0]=salida, [1]=llegada
        try {
            LocalDate hoy = LocalDate.now();
            LocalDate manana = hoy.plusDays(1);

            SistemaVentaPasaje.getInstance().createViaje(
                    hoy, LocalTime.of(8, 0), 3000, 60, "ABCD-12",
                    new IdPersona[]{ Rut.of("55.555.555-5"), Rut.of("77.777.777-7") },
                    new String[]{ "Pinto", "Medina" });

            SistemaVentaPasaje.getInstance().createViaje(
                    hoy, LocalTime.of(9, 15), 3500, 75, "EFGH-34",
                    new IdPersona[]{ Rut.of("55.555.555-5"), Rut.of("77.777.777-7") },
                    new String[]{ "Pinto", "Santa Faz" });

            SistemaVentaPasaje.getInstance().createViaje(
                    hoy, LocalTime.of(10, 30), 4000, 90, "IJKL-56",
                    new IdPersona[]{ Rut.of("55.555.555-5"), Rut.of("66.666.666-6") },
                    new String[]{ "Pinto", "Medina" });

            SistemaVentaPasaje.getInstance().createViaje(
                    hoy, LocalTime.of(11, 45), 5500, 105, "MNOP-78",
                    new IdPersona[]{ Rut.of("55.555.555-5"), Rut.of("77.777.777-7") },
                    new String[]{ "Pinto", "Santa Faz" });

            SistemaVentaPasaje.getInstance().createViaje(
                    manana, LocalTime.of(13, 0), 6000, 120, "QRST-90",
                    new IdPersona[]{ Rut.of("55.555.555-5"), Rut.of("66.666.666-6") },
                    new String[]{ "Pinto", "Medina" });

            // Viaje día siguiente para probar filtros de fecha
            LocalDate fecha2 = LocalDate.of(2026, 5, 29);
            SistemaVentaPasaje.getInstance().createViaje(
                    fecha2, LocalTime.of(8, 0), 3000, 60, "ABCD-12",
                    new IdPersona[]{ Rut.of("55.555.555-5"), Rut.of("77.777.777-7") },
                    new String[]{ "Pinto", "Medina" });

        } catch (SistemaVentaPasajesException e) {
            System.out.println("DatosPrueba viaje: " + e.getMessage());
        }
        cargarVentas();
        System.out.println("...:::: Datos de prueba cargados exitosamente ::::....");
    }

    private void cargarVentas() {
        LocalDate hoy = LocalDate.now();
        System.out.println("DEBUG fecha: " + hoy);

        // ── Venta B001 ──
        try {
            SistemaVentaPasaje.getInstance().iniciaVenta(
                    "B001", TipoDocumento.BOLETA, hoy, "Pinto", "Medina",
                    Rut.of("11.111.111-1"), 2);
            System.out.println("DEBUG iniciaVenta B001 OK");

            SistemaVentaPasaje.getInstance().vendePasaje(
                    "B001", TipoDocumento.BOLETA, hoy, LocalTime.of(8, 0), "ABCD-12",
                    5, Rut.of("11.111.111-1"));
            System.out.println("DEBUG vendePasaje B001 asiento 5 OK");

            SistemaVentaPasaje.getInstance().vendePasaje(
                    "B001", TipoDocumento.BOLETA, hoy, LocalTime.of(8, 0), "ABCD-12",
                    6, Rut.of("22.222.222-2"));
            System.out.println("DEBUG vendePasaje B001 asiento 6 OK");

            SistemaVentaPasaje.getInstance().pagaVenta("B001", TipoDocumento.BOLETA);
            System.out.println("DEBUG pagaVenta B001 OK");
        } catch (SistemaVentaPasajesException e) {
            System.out.println("DEBUG ERROR B001: " + e.getMessage());
        }

        // ── Venta F001 ──
        try {
            SistemaVentaPasaje.getInstance().iniciaVenta(
                    "F001", TipoDocumento.FACTURA, hoy, "Pinto", "Medina",
                    Rut.of("22.222.222-2"), 2);
            System.out.println("DEBUG iniciaVenta F001 OK");

            SistemaVentaPasaje.getInstance().vendePasaje(
                    "F001", TipoDocumento.FACTURA, hoy, LocalTime.of(10, 30), "IJKL-56",
                    3, Rut.of("33.333.333-3"));
            System.out.println("DEBUG vendePasaje F001 asiento 3 OK");

            SistemaVentaPasaje.getInstance().vendePasaje(
                    "F001", TipoDocumento.FACTURA, hoy, LocalTime.of(10, 30), "IJKL-56",
                    4, Rut.of("44.444.444-4"));
            System.out.println("DEBUG vendePasaje F001 asiento 4 OK");

            SistemaVentaPasaje.getInstance().pagaVenta("F001", TipoDocumento.FACTURA, 1234567890123456L);
            System.out.println("DEBUG pagaVenta F001 OK");
        } catch (SistemaVentaPasajesException e) {
            System.out.println("DEBUG ERROR F001: " + e.getMessage());
        }

        // ── Venta B002 ──
        try {
            SistemaVentaPasaje.getInstance().iniciaVenta(
                    "B002", TipoDocumento.BOLETA, hoy, "Pinto", "Santa Faz",
                    Rut.of("11.111.111-1"), 1);
            System.out.println("DEBUG iniciaVenta B002 OK");

            SistemaVentaPasaje.getInstance().vendePasaje(
                    "B002", TipoDocumento.BOLETA, hoy, LocalTime.of(9, 15), "EFGH-34",
                    10, Rut.of("44.444.444-4"));
            System.out.println("DEBUG vendePasaje B002 asiento 10 OK");

            SistemaVentaPasaje.getInstance().pagaVenta("B002", TipoDocumento.BOLETA);
            System.out.println("DEBUG pagaVenta B002 OK");
        } catch (SistemaVentaPasajesException e) {
            System.out.println("DEBUG ERROR B002: " + e.getMessage());
        }
    }

}