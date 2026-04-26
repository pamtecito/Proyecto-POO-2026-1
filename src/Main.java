import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        Scanner tcld = new Scanner(System.in);
        menu(tcld);
    }

    private static void menu(Scanner tcld) {
        SistemaVentaPasaje sistema = new SistemaVentaPasaje();
        int opcion;

        do {
            System.out.println("Menú principal");
            System.out.println("1. Crear cliente");
            System.out.println("2. Crear bus");
            System.out.println("3. Crear Viaje");
            System.out.println("4. Vender pasaje");
            System.out.println("5. Lista de pasajeros");
            System.out.println("6. Lista de buses");
            System.out.println("7. Lista de viajes");
            System.out.println("8. Consulta viajes disponibles por fecha");
            System.out.println("9. Salir");
            System.out.println("Opcion: ");
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
                    venderPasaje(tcld, sistema);
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
                case 9:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Error, debe escribir una de las opciones puestas anteriormente");
            }


        } while (opcion != 9);
    }
    private static void crearCliente (Scanner tcld, SistemaVentaPasaje sistema){
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

                boolean creado;

                if (rutopasaporte == 1) {
                    creado = sistema.createCliente(id, nombre, telefono, email);
                } else {
                    creado = sistema.createCliente(id, nombre, telefono, email);
                }

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
        String patente = tcld.next();
        System.out.println("Marca: ");
        tcld.nextLine();
        String marca = tcld.nextLine();
        System.out.println("Modelo: ");
        tcld.nextLine();
        String modelo = tcld.nextLine();
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
        String fechaStr = tcld.next();
        LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.println("Hora[hh:mm]: ");
        String horaStr = tcld.next();
        LocalTime hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
        System.out.println("Precio: ");
        int precio = tcld.nextInt();
        System.out.println("Patente Bus: ");
        String patente = tcld.next();

        Boolean creado = sistema.createViaje(fecha, hora, precio, patente);

        if (creado) {
            System.out.println("Viaje creado exitosamente");
        } else {
            System.out.println("No se ha podido crear el viaje. O la patente no existe o el bus ya tiene un viaje programado");
        }

        //Verificar si la patente existe y si no hay un bus con la misma fecha agendada

    }

    private static void vendePasajes(Scanner tcld, SistemaVentaPasaje sistema) {
        System.out.println("...::::Venta de pasajes:::... ");



        System.out.println("Datos de la venta: ");
        System.out.println("ID Documento: ");
        String idDoc = tcld.next();
        System.out.println("Tipo de documento: [1]Boleta [2]Factura");
        int tipo = tcld.nextInt();

        if (tipo != 1 && tipo != 2) {
            System.out.println("No se pudo copletar. No se eligio ni boleta ni factura");
        } else {
            System.out.println("Fecha de la venta:");
            String fech = tcld.next();
            LocalDate fecha = LocalDate.parse(fech, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            System.out.println("Datos del cliente:");
            System.out.println("Rut[1] o Pasaporte[2]");
            int opcion = tcld.nextInt();
            IdPersona id = null;
            if (opcion == 1) {
                System.out.println("Rut: ");
                id = Rut.of(tcld.next());
            }
            if (opcion == 2) {
                System.out.println("Pasaporte: ");
                System.out.println("Numero: ");
                String numero = tcld.next();
                System.out.println("Nacionalidad: ");
                String nacionalidad = tcld.next();
                id = Pasaporte.of(numero, nacionalidad);
            }
            if (opcion != 1 && opcion != 2) {
                System.out.println("No se pudo completar. No se eligio ni Rut ni Pasaporte");
            } else {
                boolean creado;
                if (tipo == 1) {
                    String bolofac = "BOLETA";
                    TipoDocumento boletaofactura = TipoDocumento.valueOf(bolofac);
                    creado = sistema.iniciaVenta(idDoc, boletaofactura, fecha, id);
                }
            }
        }


    }

    private static void venderPasaje(Scanner tcld, SistemaVentaPasaje sistema){
        System.out.println("Datos de la venta");
        System.out.println("ID Documento: ");
        String id = tcld.next();
        System.out.println("Tipo documento [1] Boleta [2] Factura: ");
        int boletaofactura = tcld.nextInt();
        if  (boletaofactura != 1 && boletaofactura != 2) {
            System.out.println("No se eligio una opcion valida ([1] [2])");
        } else {
            System.out.println("Datos del cliente");
            System.out.println("Rut[1] o pasaporte[2]: ");
            int rutopasaporte = tcld.nextInt();
            if (rutopasaporte != 1 && rutopasaporte != 2) {
                System.out.println("No se eligio una opcion valida ([1] [2])");
            } else {

            }

        }
    }

    private static void listPasajeros(Scanner tcld, SistemaVentaPasaje sistema) {
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
            System.out.printf("%-10s %-10s %-10s %-10s %-10s\n",
                    "Patente", "Fecha", "Hora", "Precio", "Disponibles");

            System.out.println("------------------------------------------------------");


            for (int i = 0; i < lista.length; i++) {
                System.out.printf("%-10s %-10s %-10s %-10s %-10s\n",
                        lista[i][0],
                        lista[i][1],
                        lista[i][2],
                        lista[i][3],
                        lista[i][4]
                );
            }
        }
    }

    private static void listVentas(Scanner tcld, SistemaVentaPasaje sistema) {
        System.out.println("Listado de ventas");
        String[][] lista = sistema.listVentas();
        if (lista == null) {
            System.out.println("No hay ventas registrados");
        } else {
            System.out.printf("%-15s %-10s %-12s %-25s %-10s\n",
                    "ID Doc", "Tipo", "Fecha", "Cliente", "Monto");

            System.out.println("--------------------------------------------------------------------------");

            for (int i = 0; i < lista.length; i++) {
                System.out.printf("%-15s %-10s %-12s %-25s %-10s\n",
                        lista[i][0],
                        lista[i][1],
                        lista[i][2],
                        lista[i][3],
                        lista[i][4]
                );

            }
        }
    }

    private static void listViajes(Scanner tcld, SistemaVentaPasaje sistema) {
        System.out.println("Listado de viajes");
        String[][] lista = sistema.listViajes();
        if (lista == null) {
            System.out.println("No hay viajes registrados");
        }  else {
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