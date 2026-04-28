public class Rut implements IdPersona {
    private int numero;
    private char dv;

    private Rut(int numero, char dv) {
        this.numero = numero;
        this.dv = dv;
    }

    public int getNumero() {
        return numero;
    }

    public char getDv() {
        return dv;
    }

    public static Rut of(String rutConDv){
        char dv = rutConDv.charAt(rutConDv.length() - 1);

        rutConDv = rutConDv.replace(".","").replace("-","");
        rutConDv = rutConDv.substring(0, rutConDv.length() - 1);

        String  num =  (rutConDv);

        return new Rut(Integer.parseInt(num), dv);
    }

    @Override
    public String toString() {
        String rut = String.format("%,d",numero).replace(",",".");
        return rut + " - " + dv;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this == null || this.getClass() != getClass()) return false;
        if (!(obj instanceof Rut)) return false;
        Rut rut = (Rut) obj;
        return this.numero == rut.numero && this.dv == rut.dv;
    }
}
