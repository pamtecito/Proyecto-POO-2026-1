//Alonso Gutiérrez Pino

public class Persona {
    private IdPersona idPersona;
    private Nombre nombreCompleto;
    private String telefono;

    public Persona(IdPersona idPersona, Nombre nombreCompleto) {
        this.idPersona = idPersona;
        this.nombreCompleto = nombreCompleto;
    }

    public IdPersona getIdPersona() {
        return idPersona;
    }

    public Nombre getNombreCompleto() {
        return nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setNombreCompleto(Nombre nombreCompleto)  {
        this.nombreCompleto = nombreCompleto;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String toString() {
        return "Id Persona: " + idPersona +
                "\nNombre: " + nombreCompleto +
                "\nTelefono: " + telefono;
    }

    @Override
    public boolean equals(Object otro){
        if (this == otro) return true;
        if (otro == null || getClass() != otro.getClass()) return false;
        Persona perso = (Persona)otro;
        return this.idPersona == perso.idPersona;
    }
}
