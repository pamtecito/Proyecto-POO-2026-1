public class Direccion {
    private String calle;
    private int numero;
    private String comuna;

    public Direccion(String calle, int numero, String comuna) {
        this.calle = calle;
        this.numero = numero;
        this.comuna = comuna;
    }

    public String getCalle() {
        return calle;
    }

    public int getNumero() {
        return numero;
    }

    public String getComuna() {
        return comuna;
    }

    @Override
    public String toString() {
        return calle +  numero + comuna;
    }

    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (otro == null || this.getClass() != otro.getClass()) return false;
        Direccion direccion = (Direccion) otro;
        return this.numero == direccion.numero && this.comuna.equals(direccion.comuna);
    }
}
