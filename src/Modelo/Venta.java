package Modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class Venta implements Serializable {
    private String idDocumento;
    private TipoDocumento tipo;//enum
    private LocalDate fecha;
    private Cliente cliente;
    private ArrayList<Pasaje> misPasajes;
    private Pago pago;

    public Venta(String idDocumento, TipoDocumento tipo, LocalDate fecha, Cliente cli) {
        this.idDocumento = idDocumento;
        this.fecha = fecha;
        this.tipo = tipo;
        this.cliente = cli;
        cli.addVenta(this);
        pago = null;
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
        viaje.addPasaje(p);
    }

    public Pasaje[] getPasajes() {
        Pasaje[]  pasajes = new Pasaje[misPasajes.size()];
        for (int i = 0; i < misPasajes.size(); i++) {
            pasajes[i] = misPasajes.get(i);
        }
        return pasajes;
    }

    public int getMonto() {

        if(misPasajes.isEmpty()){//esta vacia o no
            return 0;
        }

        return misPasajes.get(0).getViaje().getPrecio() * misPasajes.size();
    }

    public int getMontoPagado(){
        if (misPasajes.isEmpty()) return 0;
        return misPasajes.getFirst().getViaje().getPrecio() * misPasajes.size();// o esto?return pago.getMonto();
    }
    //este sin nada de trjeta
    public boolean pagaMonto(){

        if(pago != null){
            return false;
        }
        pago = new PagoEfectivo(getMonto());
        return true;

    }

    //este es con tarjeta
    public boolean pagoMonto(long nroTarjeta){
        if (pago != null){
            return false;
        }
        pago = new PagoTarjeta(getMonto(),nroTarjeta);

        return true;
    }
    public String getTipoPago() {

        if(pago == null){
            return null;
        }

        if(pago instanceof PagoEfectivo){
            return "Efectivo";
        }

        if(pago instanceof PagoTarjeta){
            return "Tarjeta";
        }

        return null;
    }
    @Override
    public boolean equals(Object otro) {

        if(otro instanceof Venta) {

            Venta v = (Venta) otro;

            return this.idDocumento.equals(v.idDocumento);//&& this.tipo.equals(v.tipo);no se si agregar eso
        }

        return false;
    }




}