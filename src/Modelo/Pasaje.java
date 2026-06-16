package Modelo;

import java.io.Serializable;

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
        return "------------------------------------------------------------\n" +
                "                    PASAJE ELECTRÓNICO\n" +
                "------------------------------------------------------------\n" +
                "Nombre Empresa          Número de pasaje\n" +
                viaje.getBus().getEmpresa().getNombre() + "          " +
                numero + "\n\n" +

                "Nombre Pasajero                    RUT/Pasaporte\n" +
                pasajero.getNomContacto() + "          " +
                pasajero.getIdPersona() + "\n\n" +

                "Patente bus     Asiento     Valor Pagado\n" +
                viaje.getBus().getPatente() + "          " +
                asiento + "          " +
                viaje.getPrecio() + "\n\n" +

                "Terminal origen     Terminal destino     Fecha     Hora\n" +
                viaje.getTerminalSalida().getNombre() + "     " +
                viaje.getTerminalLlegada().getNombre() + "     " +
                viaje.getFecha() + "     " +
                viaje.getHora() + "\n" +

                "------------------------------------------------------------";
    }


}