import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Viaje {
    private LocalDate fecha;
    private LocalTime hora;
    private int precio;
    private Bus bus;
    private ArrayList<Pasaje> pasajes;

    public Viaje(LocalDate fecha, LocalTime hora, int precio, Bus bus){
        this.fecha= fecha;
        this.hora= hora;
        this.precio= precio;
        this.bus=bus;
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

    public String[][] getAsientos(){
        String[][] asientos = new String[this.getBus().getNroAsientos()][2];
        for (int i = 0; i < asientos.length; i++) {
            asientos[i][0] = String.valueOf(i+1);
            asientos[i][1] = "libre";
        }
        for (Pasaje p : pasajes){
            asientos[p.getAsiento()][1] = "Ocupado";
        }
        return asientos;
    }

    public void addPasaje(Pasaje pasaje) {
        pasajes.add(pasaje);
    }

    public String[][] getListaPasajeros(){
        String[][] h= new String[0][0];
        return h;
    }

    public boolean existeDisponibilidad(){
        return false;
    }

    public int getNroAsientosDisponibles(){
        return 0;
    }
}
