import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class Main {
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
                    crearCliente(tcld, sistema);
                    break;
                case 2:
                    crearBus(tcld, sistema);
                    break;
                case 3:
                    crearViaje(tcld, sistema);
                    break;
                case 4:
                    vendePasaje(tcld, sistema);
                    break;
                case 5:
                    listPasajeros(tcld, sistema);
                    break;
                case 6:
                    listVentas(tcld, sistema);
                    break;
                case 7:
                    listViajes(tcld, sistema);
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
                System.out.println("Telefono movil: ");
                String telefono = tcld.next();
                System.out.println("Email: ");
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

    private static void crearBus(Scanner tcld, SistemaVentaPasaje sistema){
        System.out.println("Crear un nuevo bus: ");
        System.out.println("Patente: ");
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

    private static void crearViaje(Scanner tcld, SistemaVentaPasaje sistema){
        System.out.println("Creando un nuevo viaje: ");
        System.out.println("Fecha [dd/mm/yyyy]");
        String fechaStr = tcld.next();
        LocalDate fecha = LocalDate.parse(fechaStr, formato);
        System.out.println("Hora[hh:mm]: ");
        String horaStr = tcld.next();
        LocalTime hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
        System.out.println("Precio: ");
        int precio = tcld.nextInt();
        System.out.println("Patente Bus: ");
        String patente = tcld.next().toUpperCase();

        Boolean creado = sistema.createViaje(fecha, hora, precio, patente);

        if (creado) {
            System.out.println("Viaje creado exitosamente");
        } else {
            System.out.println("No se ha podido crear el viaje. O la patente no existe o el bus ya tiene un viaje programado");
        }

        //Verificar si la patente existe y si no hay un bus con la misma fecha agendada

    }


    private static void vendePasaje(Scanner tcld, SistemaVentaPasaje sistema){
        System.out.println("...::::Venta de pasajes:::... ");

        boolean continuar = true;

        System.out.println("Datos de la venta: ");

        System.out.println("ID Documento: ");
        String idDoc = tcld.next();

        System.out.println("Tipo de documento: [1]Boleta [2]Factura");
        int tipo = tcld.nextInt();

        if (tipo != 1 && tipo != 2) {
            System.out.println("No se pudo completar. Tipo de documento invalido.");
            continuar = false;
        }

        TipoDocumento tipoDoc = null;
        if (continuar) {
            if (tipo == 1) {
                tipoDoc = TipoDocumento.BOLETA;
            } else {
                tipoDoc = TipoDocumento.FACTURA;
            }
        }


        LocalDate fecha = null;
        if (continuar) {
            System.out.println("Fecha de la venta [dd/MM/yyyy]:");
            String fechaStr = tcld.next();
            fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        IdPersona id = null;

        if (continuar) {
            System.out.println("Datos del cliente:");
            System.out.println("Rut[1] o Pasaporte[2]");
            int opcion = tcld.nextInt();

            if (opcion == 1) {
                System.out.println("Rut:");
                id = Rut.of(tcld.next());
            } else if (opcion == 2) {
                System.out.println("Numero:");
                String numero = tcld.next();
                System.out.println("Nacionalidad:");
                String nacionalidad = tcld.next();
                id = Pasaporte.of(numero, nacionalidad);
            } else {
                System.out.println("Opcion invalida.");
                continuar = false;
            }

            if (id == null) {
                System.out.println("Identificador invalido.");
                continuar = false;
            }
        }

        boolean creado;

        if (continuar) {
            creado = sistema.iniciaVenta(idDoc, tipoDoc, fecha, id);

            if (!creado) {
                System.out.println("No se pudo iniciar la venta.");
                continuar = false;
            }
        }

        int cantPasajes = 0;
        LocalDate fechaViaje = null;
        String[][] horariosDisponibles = null;

        if (continuar) {
            System.out.println("Cantidad de pasajes:");
            cantPasajes = tcld.nextInt();

            System.out.println("Fecha de viaje [dd/MM/yyyy]:");
            String fechaStr = tcld.next();
            fechaViaje = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            horariosDisponibles = sistema.getHorariosDisponibles(fechaViaje);

            if (horariosDisponibles.length == 0) {
                System.out.println("No hay viajes disponibles.");
                continuar = false;
            }
        }

        int seleccion = 0;
        String patente = null;
        LocalTime horaViaje = null;

        if (continuar) {
            System.out.printf("| %-3s | %-8s | %-8s | %-6s | %-9s |%n",
                    "N°", "BUS", "SALIDA", "VALOR", "ASIENTOS");

            for (int i = 0; i < horariosDisponibles.length; i++) {
                System.out.printf("| %-3d | %-8s | %-8s | %-6s | %-9s |%n",
                        (i + 1), horariosDisponibles[i][0], horariosDisponibles[i][1], horariosDisponibles[i][2], horariosDisponibles[i][3]);
            }

            System.out.println("Seleccione viaje (desde [1] hasta [" + horariosDisponibles.length + "]):");
            seleccion = tcld.nextInt();

            patente = horariosDisponibles[seleccion - 1][0];
            horaViaje = LocalTime.parse(horariosDisponibles[seleccion - 1][1]);
        }

        String[][] asientos = null;

        if (continuar) {
            asientos = sistema.listAsientosDeViaje(fechaViaje, horaViaje, patente);

            if (asientos.length == 0) {
                System.out.println("No existe el viaje.");
                continuar = false;
            }
        }

        if (continuar) {
            for (int i = 0; i < asientos.length; i++) {
                System.out.print((i + 1) + ":" + asientos[i][1] + "  ");
            }
            System.out.println();
        }

        if (continuar) {
            int vendidos = 0;

            while (vendidos < cantPasajes) {

                System.out.println("Seleccione asiento:");
                int asiento = tcld.nextInt();

                if (asientos[asiento - 1][1].equals("*")) {
                    System.out.println("Asiento ocupado.");
                } else {

                    System.out.println("Tipo ID pasajero [1 Rut / 2 Pasaporte]: ");
                    int tipoPas = tcld.nextInt();

                    IdPersona idPasajero = null;

                    if (tipoPas == 1) {
                        idPasajero = Rut.of(tcld.next());
                    } else if (tipoPas == 2) {
                        String num = tcld.next();
                        String nac = tcld.next();
                        idPasajero = Pasaporte.of(num, nac);
                    }

                    if (idPasajero != null) {

                        boolean vendido = sistema.vendePasaje(
                                idDoc,
                                fechaViaje,
                                horaViaje,
                                patente,
                                asiento,
                                idPasajero
                        );

                        if (vendido) {
                            vendidos++;

                            asientos[asiento - 1][1] = "*";

                        } else {
                            System.out.println("No se pudo vender el pasaje.");
                        }

                    } else {
                        System.out.println("Pasajero invalido.");
                    }
                }
            }
        }

        if (continuar) {
            double monto = sistema.getMontoVenta(idDoc, tipoDoc);
            System.out.println("Venta completada.");
            System.out.println("Monto total: " + monto);
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

