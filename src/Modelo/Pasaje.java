package Modelo;
import Utilidades.*;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

public class Pasaje implements Serializable {
    private long numero;
    private int asiento;
    private Venta venta;
    private Pasajero pasajero;
    private Viaje viaje;
    public Pasaje(int asiento, Viaje viaje, Pasajero pasajero, Venta venta ) {
        this.asiento = asiento;
        this.viaje = viaje;
        this.pasajero = pasajero;
        this.venta = venta;

    }
    public int getNumero() {
        return (int)numero;
    }
    public int getAsiento() {
        return asiento;
    }
    public Viaje getViaje(){
        return viaje;
    }
    public Pasajero getPasajero(){
        return pasajero;
    }
    public Venta getVenta(){
        return venta;
        //no se si arreglarlo mas aca??
    }
    @Override
    public String toString() {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        return "------------------------------------------------------------\n" +
                "                    PASAJE ELECTRÓNICO\n" +
                "------------------------------------------------------------\n" +
                "Nombre Empresa          Número de pasaje\n" +
                viaje.getBus().getEmpresa().getNombre().toUpperCase() + "          " +
                numero + "\n\n" +

                "Nombre Pasajero                    RUT/Pasaporte\n" +
                pasajero.getNombreCompleto().toString().toUpperCase() + "       " +
                pasajero.getIdPersona() + "\n\n" +

                "Patente bus     Asiento     Valor Pagado\n" +
                viaje.getBus().getPatente() + "          " +
                asiento + "          " +
                viaje.getPrecio() + "\n\n" +

                "Terminal origen     Terminal destino     Fecha     Hora\n" +
                viaje.getTerminalSalida().getNombre().toUpperCase() + "     " +
                viaje.getTerminalLlegada().getNombre().toUpperCase() + "     " +
                viaje.getFecha().format(formatoFecha) + "    " +
                viaje.getHora().format(formatoHora) + "\n" +

                "------------------------------------------------------------";
    }


}