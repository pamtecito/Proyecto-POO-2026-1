
import java.time.LocalDate;
import java.util.ArrayList;

public class Venta {
    private String idDocumento;
    private TipoDocumento tipo;//enum
    private LocalDate fecha;
    private Cliente cliente;
    private ArrayList<Pasaje> misPasajes;

    public Venta(String idDocumento, TipoDocumento tipo,  LocalDate fecha, Cliente cli) {
        this.idDocumento = idDocumento;
        this.fecha = fecha;
        this.tipo = tipo;
        this.cliente = cli;
        misPasajes = new ArrayList<>();
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public TipoDocumento getTipo() {
        return tipo;
    }
    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void createPasaje(int asiento, Viaje viaje, Pasajero pasajero) {
        Pasaje p = new Pasaje(asiento, viaje, pasajero, this);
        misPasajes.add(p);

    }

    public Pasaje[] getPasajes() {
        Pasaje[]  pasajes = new Pasaje[misPasajes.size()];
        for (int i = 0; i < misPasajes.size(); i++) {
            pasajes[i] = misPasajes.get(i);
        }
        return pasajes;
        //return misPasajes.toArray(new Pasaje[0]);
    }

    public int getMonto() {
        if (misPasajes.isEmpty()) return 0;
        return misPasajes.getFirst().getViaje().getPrecio() * misPasajes.size();
    }


}
