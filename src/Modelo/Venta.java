package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class Venta {
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

    }

    public Pasaje[] getPasajes() {
        Pasaje[]  pasajes = new Pasaje[misPasajes.size()];
        for (int i = 0; i < misPasajes.size(); i++) {
            pasajes[i] = misPasajes.get(i);
        }
        return pasajes;
        //return misPasajes.toArray(new Modelo.Pasaje[0]);
    }

    public int getMonto() {
        return 0;
    }
    public int getMontoPagado(){


        if (misPasajes.isEmpty()) return 0;
        return misPasajes.getFirst().getViaje().getPrecio() * misPasajes.size();
    }
    public boolean pagaMonto(long nroTarjeta){
        if (nroTarjeta >0){
            return true;
        }
        return false;
    }
    public boolean pagoMonto(){
        if (pago != null){
            return false;
        } else {
            PagoEfectivo p = new PagoEfectivo(getMonto());

        }
    }
    public String getTipoPago() {
        return null;
    }
    @Override
    public boolean equals(Object otro) {

        if(otro instanceof Venta) {

            Venta v = (Venta) otro;

            return this.idDocumento.equals(v.idDocumento);
        }

        return false;
    }




}
