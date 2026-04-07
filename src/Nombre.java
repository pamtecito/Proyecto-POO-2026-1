import java.util.Objects;

public class Nombre {
    private Tratamiento tratamiento;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    public Nombre(){
        //constructor
    }

    public void setTratamiento(Tratamiento tratamiento) {
        tratamiento = tratamiento;
    }

    public void setNombres(String nombres) {
        nombres = nombres;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        apellidoPaterno = apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        apellidoMaterno = apellidoMaterno;
    }

    public Tratamiento getTratamiento(){
        return tratamiento;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }
    @Override
    public String toString (){
        return null;
    }
    public boolean equals (Object otro){
        if (this==otro)return true; //para comparar espacio de memoria
        if (this.getClass()!=otro.getClass()||otro==null)return false;
        return Objects.equals(this.getApellidoMaterno(), this.apellidoPaterno);

    }
}