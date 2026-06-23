package Modelo;

public class PagoTarjeta extends Pago {

    private long nroTarjeta;

    public PagoTarjeta(int monto, long nroTarjeta) {
        super(monto);
        this.nroTarjeta = nroTarjeta;
    }

    @Override
    public int getMonto() {
        return super.getMonto();
    }

    public long getNroTarjeta() {
        return nroTarjeta;
    }
}
