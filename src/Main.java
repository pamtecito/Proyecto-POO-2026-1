import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class Main {
    private static Scanner tcld = new Scanner(System.in);
    private static SistemaVentaPasaje sistema = new SistemaVentaPasaje();
    private static DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public static void main(String[] args) {
        sistema.debug();
        menu();
    }

    private static void menu() {
        LocalDate f = null;
        int opcion;

        do {
            System.out.println("Menú principal");
            System.out.println("1. Crear cliente");
            System.out.println("2. Crear bus");
            System.out.println("3. Crear Viaje");
            System.out.println("4. Vender pasaje");
            System.out.println("5. Lista de pasajeros");
            System.out.println("6. Lista de ventas");
            System.out.println("7. Lista de viajes");
            System.out.println("8. Consulta viajes disponibles por fecha");
            System.out.println("9. Salir");
            System.out.println("..:: Ingrese numero de opción: ");
            opcion = tcld.nextInt();

            switch (opcion) {
                case 1:
                    crearCliente();
                    break;
                case 2:
                    crearBus();
                    break;
                case 3:
                    crearViaje();
                    break;
                case 4:
                    vendePasaje();
                    break;
                case 5:
                    listPasajeros();
                    break;
                case 6:
                    listVentas();
                    break;
                case 7:
                    listViajes();
                    break;
                case 8:
                    consultaHorario(f);
                    break;
                case 9:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Error, debe escribir una de las opciones puestas anteriormente");
            }


        } while (opcion != 9);
    }

    private static void crearCliente() {
        IdPersona id;
        int srOSra;


        System.out.println("Crear un nuevo cliente");
        System.out.println("Rut[1] o pasaporte[2]: ");
        int rutopasaporte = tcld.nextInt();
        if (rutopasaporte != 1 && rutopasaporte != 2) {
            System.out.println("Opción errónea");
        } else {
            if (rutopasaporte == 1) {
                System.out.println("R.U.T: ");
                id = Rut.of(tcld.next());
            } else {
                System.out.println("Pasaporte: ");
                System.out.println("Numero: ");
                String num = tcld.next();
                while(num.length() != 9){
                    System.out.println("Cantidad de digitos incorrecta, ingrese nuevamente: ");
                    num= tcld.next();
                }
                System.out.println("Nacionalidad:");
                String nacionalidad = tcld.next();
                id = Pasaporte.of(num, nacionalidad);
            }
            System.out.println("Sr[1] o Sra[2]");
            srOSra = tcld.nextInt();
            if (srOSra != 1 && srOSra != 2) {
                System.out.println("No se eligio Sr o Sra adecuadamente");
            } else {
                System.out.println("Nombres: ");
                tcld.nextLine();
                String nombres = tcld.nextLine();
                System.out.println("Apellido paterno: ");
                String apa = tcld.next();
                System.out.println("Apellido materno: ");
                String ama = tcld.next();
                Nombre nombre = new Nombre();
                if (srOSra == 1) {
                    String trato0 = "SR";
                    Tratamiento trato = Tratamiento.valueOf(trato0);
                    nombre.setTratamiento(trato);
                } else {
                    String trato0 = "SRA";
                    Tratamiento trato = Tratamiento.valueOf(trato0);
                    nombre.setTratamiento(trato);
                }
                nombre.setNombres(nombres);
                nombre.setApellidoPaterno(apa);
                nombre.setApellidoMaterno(ama);
                System.out.println("Telefono movil: (ejemplo: 9XXXXXXXX) ");
                String telefono = tcld.next();
                System.out.println("Email: (ejemplo: x@gmail.com)");
                String email = tcld.next();

                boolean creado = sistema.createCliente(id, nombre, telefono, email);


                if (creado) {
                    System.out.println("Cliente creado");
                } else {
                    System.out.println("No se ha podido crear. Ya existe un cliente con el identificador dado");
                }


            }

        }
    }

    private static void crearBus() {
        System.out.println("...::::Creacion de un nuevo BUS::::... ");
        System.out.println("Patente: (ejemplo: xx.xx-99)");
        String patente = tcld.next().toUpperCase();
        System.out.println("Marca: ");
        tcld.nextLine();
        String marca = tcld.nextLine();
        System.out.println("Modelo: ");
        String modelo = tcld.nextLine();
        tcld.nextLine();
        System.out.println("Numero de asientos: ");
        int nroAsientos = tcld.nextInt();

        boolean creado = sistema.createBus(patente, marca, modelo, nroAsientos);

        if (creado) {
            System.out.println("Bus creado exitosamente");
        } else {
            System.out.println("Bus no ha sido creado. Ya existe un bus con la patente dada");
        }
    }

    private static void crearViaje() {
        System.out.println("Creando un nuevo viaje: ");
        System.out.println("Fecha [dd/mm/yyyy]");
        String fechaStr = tcld.next();
        LocalDate fecha = LocalDate.parse(fechaStr, formato);
        System.out.println("Hora[hh:mm]: ");
        String horaStr = tcld.next();
        LocalTime hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
        System.out.println("Precio: ");
        int precio = tcld.nextInt();
        System.out.println("Patente Bus (xx.xx-00):");
        String patente = tcld.next().toUpperCase();


        if (sistema.createViaje(fecha, hora, precio, patente)) {
            System.out.println("Viaje creado exitosamente");
        } else {
            System.out.println("No se ha podido crear el viaje. O la patente no existe o el bus ya tiene un viaje programado");
        }

        //Verificar si la patente existe y si no hay un bus con la misma fecha agendada

    }


    private static void vendePasaje() {
        LocalDate f;

        System.out.println("...::::Venta de pasajes:::... ");

        System.out.println(":::: Datos de la venta");


        System.out.print("             ID Documento :");
        String idDocumento = tcld.next();
        System.out.print("Tipo documento: [1] Boleta [2] Factura: ");
        int tipoDocumento = tcld.nextInt();
        while (tipoDocumento != 1 && tipoDocumento != 2) {
            System.out.println("Opcion invalida ingrese nuevamente");
            tipoDocumento = tcld.nextInt();
        }

        System.out.println("Fecha de venta [dd/mm/yyyy]");
        f = LocalDate.parse(tcld.next(), formato);

        System.out.println("");
        System.out.println(":::: Datos del cliente");

        System.out.print("Rut [1] o Pasaporte [2]:");
        int tipoDocumentoCliente = tcld.nextInt();
        while (tipoDocumentoCliente != 1 && tipoDocumentoCliente != 2) {
            System.out.println("Opcion invalida ingrese nuevamente");
            tipoDocumentoCliente = tcld.nextInt();
        }
        IdPersona id;
        if (tipoDocumentoCliente == 1) {
            System.out.print("           RUT:");
            id = Rut.of(tcld.next());
        } else {
            System.out.print("Numero de pasaporte [9 numeros]: ");
            String idPasaporte = tcld.next();
            while (idPasaporte.length() != 9) {
                System.out.println("Numero de pasaporte invalido, ingreselo nuevamente:");
                idPasaporte = tcld.next();
            }
            System.out.print("Nacionalidad: ");
            id = Pasaporte.of(idPasaporte, tcld.next());

        }
        System.out.print("Ingrese nombre cliente: ");
        String nombreCliente = tcld.nextLine();
        tcld.nextLine();

        if (sistema.iniciaVenta(idDocumento, TipoDocumento.values()[tipoDocumento - 1], f, id)) {
            System.out.println("venta iniciada");
        } else {
            System.out.println("No se pudo iniciar la venta porque no se encontro el rut o pasaporte");
            return;
        }


        System.out.println("Pasajes a vender");
        System.out.println("cantidad de pasajes: ");
        //foreach
        int cantidad = tcld.nextInt();
        System.out.print("Fecha del viaje [dd/mm/yyyy]: ");
        f = LocalDate.parse(tcld.next(), formato);
        consultaHorario(f);

        String[][] datos = sistema.getHorariosDisponibles(f);
        System.out.println("Ingrese viaje entre 1-" + (datos.length) + ":");
        int viaje = tcld.nextInt();
        LocalTime hora = LocalTime.parse(datos[viaje - 1][1]);
        String patbus = datos[viaje - 1][0];


        System.out.println(":::: Asientos disponibles para el viaje seleccionado");
        System.out.println("*---*---*---*---*---*---*");
        String[][] asientosDisponibles = sistema.listAsientosDeViaje(f, hora, patbus);
        String a1, a2, a3, a4;
        for (int i = 0; i < asientosDisponibles.length; i += 4) {
            if (asientosDisponibles[i][1].equals("libre")) {
                a1 = asientosDisponibles[i][0];
            } else {
                a1 = "*";
            }
            if (i + 1 < asientosDisponibles.length && asientosDisponibles[i + 1][1].equals("libre")) {
                a2 = asientosDisponibles[i + 1][0];
            } else {
                a2 = "*";
            }
            if (i + 2 < asientosDisponibles.length && asientosDisponibles[i + 1][1].equals("libre")) {
                a3 = asientosDisponibles[i + 2][0];
            } else {
                a3 = "*";
            }
            if (i + 3 < asientosDisponibles.length && asientosDisponibles[i + 1][1].equals("libre")) {
                a4 = asientosDisponibles[i + 3][0];
            } else {
                a4 = "*";
            }

            System.out.printf("| %2s| %2s|   | %2s| %2s|%n", a1, a2, a4, a3);
            System.out.println("|---+---+---+---+---|");
        }

        System.out.println("Seleccione sus asientos (separe por ,): ");
        String[] asientos = tcld.next().split(",");

        for (String asiento : asientos) {
            int asientoi = Integer.parseInt(asiento);
            boolean pasajero;
            IdPersona r;

            if (asientosDisponibles[Integer.parseInt(asiento)][1].equals("Ocupado")) {
                System.out.println("El asiento " + asiento + "Se encuentra ocupado");

            } else {
                    System.out.println("Datos pasajero: ");
                    System.out.println("Rut[1] o pasaporte[2]");
                    int tipoDocumento2 = tcld.nextInt();
                    if (tipoDocumento2 == 1) {
                        System.out.println("Rut");
                        r = Rut.of(tcld.next());
                        pasajero = sistema.vendePasaje(idDocumento, f, hora, patbus, asientoi, r);
                    } else {
                        System.out.println("Pasaporte documento (9 numeros)");
                        String idPasaporte = tcld.next();
                        System.out.println("Nacionalidad");
                        r = Pasaporte.of(idPasaporte, tcld.next());
                        pasajero = sistema.vendePasaje(idDocumento, f, hora, patbus, asientoi, r);
                    }

                    String nombreCompleto = " ";
                    if (!pasajero) {
                        System.out.println(" ");
                        System.out.println("El pasajero no existe");
                        System.out.println("Rellene los datos para crear uno:");
                        System.out.print("Sr[1] Sra[2]");
                        int trat = tcld.nextInt();
                        System.out.print("Ingrese nombre pasajero: ");
                        Nombre nom = new Nombre();
                        nom.setNombres(tcld.next());
                        if (trat == 1) {
                            nom.setTratamiento(Tratamiento.SR);
                        } else {
                            nom.setTratamiento(Tratamiento.SRA);
                        }
                        System.out.print("Apelldo paterno: ");
                        nom.setApellidoPaterno(tcld.next());
                        System.out.print("Apellido materno: ");
                        nom.setApellidoMaterno(tcld.next());
                        System.out.print("Ingrese numero de telefono: ");
                        String fono = tcld.next();
                        Nombre nomContacto = new Nombre();
                        System.out.print("Nombre contacto: ");
                        nomContacto.setNombres(tcld.next());
                        nomContacto.setNombres(tcld.next());
                        System.out.print("Fono contacto: ");
                        String fonoContacto = tcld.next();

                        sistema.createPasajero(r, nom, fono, nomContacto, fonoContacto);
                        nombreCompleto = nom.getTratamiento() + " " + nom.getNombres() + " "
                                + nom.getApellidoPaterno() + " " + nom.getApellidoMaterno();
                    } else {
                        sistema.vendePasaje(idDocumento, f, hora, patbus, asientoi, r);

                    }

                    asientosVendidos[indice] = asientoi;
                    idsVendidos[indice]      = r.toString();
                    nombresVendidos[indice]  = nombreCompleto;
                    indice++;

                    System.out.println("Pasaje agregado exitosamente");
                }
                System.out.println(sistema.getMontoVenta(idDocumento, TipoDocumento.values()[tipoDocumento - 1]));

        }
        System.out.println(":::: Imprimiendo los pasajes");
        for (int i = 0; i < indice; i++) {
            System.out.println("------------------ PASAJE ------------------");
            System.out.println("NUMERO DE PASAJE : " + idDocumento);
            System.out.println("FECHA DE VIAJE   : " + f.format(formato));
            System.out.println("HORA DE VIAJE    : " + hora);
            System.out.println("ASIENTO          : " + asientosVendidos[i]);
            System.out.println("RUT / PASAPORTE  : " + idsVendidos[i]);
            System.out.println("NOMBRE PASAJERO  : " + nombresVendidos[i]);
        }
    }

    private static void consultaHorario(LocalDate f) {
        if (f == null){
            System.out.println("Fecha [dd/mm/yyyy]");
            f = LocalDate.parse(tcld.next(), formato);
        }

        String[][] horarios = sistema.getHorariosDisponibles(f);

        System.out.println(":::: Listado de horarios disponibles");
        System.out.printf("*----------*----------*----------*----------*%n");
        System.out.printf("| %-8s | %-6s | %-7s | %-8s |%n", "BUS", "SALIDA", "VALOR", "ASIENTOS");
        System.out.printf("|----------+----------+-----------+----------|%n");

        for (int i = 0; i < horarios.length; i++) {
            System.out.printf("%-3d| %-9s| %-7s| $%-8s| %-9s|%n",
                    (i + 1),
                    horarios[i][0],
                    horarios[i][1],
                    horarios[i][2],
                    horarios[i][3]
            );
            System.out.println("|----------+----------+-----------+----------|");
        }
    }

    private static void listPasajeros() {
        System.out.println("Listado de pasajeros de un viaje");
        System.out.println("Fecha del viaje: ");
        String fechaStr = tcld.next();
        LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.println("Hora[hh:mm]: ");
        String horaStr = tcld.next();
        LocalTime hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
        System.out.println("Patente bus: ");
        String patente = tcld.next();

        String[][] lista = sistema.listPasajeros(fecha, hora, patente);


        System.out.println("Listado de pasajeros");
        if (lista == null) {
            System.out.println("No hay pasajeros registrados");
        } else {
            System.out.printf("%-10s %-15s %-35s %-35s %-20s\n",
                    "N°Asiento", "RUT/Pasaporte", "Pasajero", "Nom Contacto", "Fono Contacto");

            System.out.println("----------------------------------------------------------------------------------------------------------------------------------");


            for (int i = 0; i < lista.length; i++) {
                System.out.printf("%-10s %-15s %-35s %-35s %-20s\n",
                        lista[i][0],
                        lista[i][1],
                        lista[i][2],
                        lista[i][3],
                        lista[i][4]
                );
            }
        }
    }

    private static void listVentas() {
        System.out.println("Listado de ventas");
        String[][] lista = sistema.listVentas();
        if (lista == null) {
            System.out.println("No hay ventas registrados");
        } else {
            System.out.printf("%-15s %-10s %-12s %-12s %-25s %-15s %-10s\n",
                    "ID Doc", "Tipo", "Fecha", "RUN/Pasaporte","Cliente", "Cantidad Boletos","Total venta");

            System.out.println("--------------------------------------------------------------------------");

            for (int i = 0; i < lista.length; i++) {
                System.out.printf("%-15s %-10s %-12s %-12s %-25s %-15s %-10s\n",
                        lista[i][0],
                        lista[i][1],
                        lista[i][2],
                        lista[i][3],
                        lista[i][4],
                        lista[i][5],
                        lista[i][6]
                );

            }
        }
    }

    private static void listViajes() {
        System.out.println("Listado de viajes");
        String[][] lista = sistema.listViajes();
        if (lista == null) {
            System.out.println("No hay viajes registrados");
        } else {
            System.out.printf("%-12s %-12s %-10s %-10s %-15s\n",
                    "Patente", "Fecha", "Hora", "Precio", "Disponibles");

            System.out.println("------------------------------------------------------------------");

            for (int i = 0; i < lista.length; i++) {
                System.out.printf("%-12s %-12s %-10s %-10s %-15s\n",
                        lista[i][0],
                        lista[i][1],
                        lista[i][2],
                        lista[i][3],
                        lista[i][4]
                );
            }
        }
    }
}

