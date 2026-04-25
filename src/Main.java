import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    SistemaVentaPasaje svp= new SistemaVentaPasaje();
    public static void main(String[] args) {
        Scanner tcld = new Scanner(System.in);
        menu(tcld);
    }

    private static void menu(Scanner tcld) {
        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Bus> buses = new ArrayList<>();
        ArrayList<Viaje> viajes = new ArrayList<>();
        int opcion;

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
            case 1: crearCliente(tcld);
            break;
            case 2: crearBus(tcld);
            break;


        }


    }
    private static void crearCliente(Scanner tcld) {
        String rut;
        String pasaporte;
        String nombres;
        String apa;
        String ama;
        String nroTelefono;
        String email;
        int srOSra;


        System.out.println("Crear un nuevo cliente");
        System.out.println("Rut[1] o pasaporte[2]: ");
        int rutopasaporte = tcld.nextInt();
        if(rutopasaporte != 1 || rutopasaporte != 2){
            System.out.println("Opción errónea");
        } else {
            if (rutopasaporte == 1) {
                System.out.println("R.U.T: ");
                rut =  tcld.next();
                System.out.println("Sr[1] o sra[2]: ");
                srOSra = tcld.nextInt();
                System.out.println("Nombres: ");
                tcld.nextLine();
                nombres = tcld.nextLine();
                System.out.println("Apellido paterno: ");
                apa = tcld.next();
                System.out.println("Apellido materno: ");
                ama = tcld.next();
                System.out.println("Telefono movil: ");
                nroTelefono = tcld.next();
                System.out.println("Email: ");
                email = tcld.next();

            } else {
                System.out.println("Pasaporte: ");
                pasaporte =  tcld.next();
            }

        }
    }

    private static void crearBus(Scanner tcld){
        System.out.println("Creacion de un bus: ");
        System.out.println("Patente:");
        String pat= tcld.next();
        System.out.println("Marca:");
        String marc= tcld.next();
        System.out.println("Modelo:");
        String mod= tcld.next();
        System.out.println("Numero de asientos: ");
        int numTotDeAsientos= tcld.nextInt();
        if(svp.createBus(pat, marc,mod,numTotDeAsientos)){
            System.out.println("Bus guardado exitosamente");
        } else {
            System.out.println("Bus no creado");
        }
    }
}