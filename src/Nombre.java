//clase hecha por anastacia en un pc del labIci
public class Nombre {
    private Tratamiento tratamiento;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;

    public Tratamiento getTratamiento(){
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        tratamiento = tratamiento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        apellidoMaterno = apellidoMaterno;
    }

    @Override
    public String toString (){

        return tratamiento + nombres + apellidoPaterno + apellidoMaterno;
    }

    @Override
    public boolean equals (Object otro){
        if (this==otro)return true; //para comparar espacio de memoria
        if (this.getClass()!=otro.getClass()||otro==null)return false;
        return this.toString().equals(otro.toString());

    }
}