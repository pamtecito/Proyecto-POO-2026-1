import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Viaje {
    private LocalDate fecha;
    private LocalTime hora;
    private int precio;
    private Bus bus;
    private ArrayList<Pasaje> pasajes;

    public Viaje(LocalDate fecha, LocalTime hora, int precio, Bus bus) {
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.bus = bus;
        pasajes= new ArrayList<Pasaje>();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public int getPrecio() {
        return precio;
    }

    public Bus getBus() {
        return bus;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void addPasaje(Pasaje pasaje){
        pasajes.add(pasaje);
    }

    public String[][] getListaPasajeros(){
        String[][] h= new String[pasajes.size()][4];
        for(int i=0; i<h.length; i++){
            h[i][0]= pasajes.getPasajero().getIdPersona();
            h[i][1]= pasajes.getPasajero().getNombreCompleto();
            h[i][2]= pasajes.getPasajero().getNomContacto();
            h[i][3]= pasajes.getPasajero().getFonoContacto();
        }
        return h;
    }

    public boolean existeDisponibilidad(){
        return pasajes.size() < bus.getNroAsientos();
    }

    public int getNroAsientosDisponibles() {
        return bus.getNroAsientos() - pasajes.size();
    }
}
