import java.util.Objects;
public class Pasaporte implements IdPersona{
    private String numero;
    private String nacionalidad;

    private Pasaporte(String num, String nacionalidad){
        this.numero= num;
        this.nacionalidad= nacionalidad;
    }

    public String getNumero() {
        return numero;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public static Pasaporte of(String num, String nacionalidad){
        if(num.length() != 9 || num == null){
            return null;
        }
        if(nacionalidad == null){
            return null;
        }
        return new Pasaporte(num, nacionalidad);
    }

    @Override
    public String toString(){
        return numero + " " +  nacionalidad;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj  == null || this.getClass() != obj.getClass()) return false;
        Pasaporte p = (Pasaporte) obj;
        return  Objects.equals(this.numero, p.getNumero()) &&
                Objects.equals(this.nacionalidad, p.getNacionalidad());
    }
}

