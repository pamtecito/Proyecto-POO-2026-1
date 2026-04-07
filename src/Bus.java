public class Bus {
    private String patente;
    private String marca;
    private String modelo;
    private int nroAsientos;

    public Bus(String patente, int nroAsientos){
        this.patente=patente;
        this.nroAsientos= nroAsientos;


    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getNroAsientos() {
        return nroAsientos;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public void addViaje(String viaje){

    }
}
