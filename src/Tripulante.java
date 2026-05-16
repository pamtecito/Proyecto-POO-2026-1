public abstract class Tripulante extends Persona {
    private Direccion direccion;

    public Tripulante(IdPersona idPersona, Nombre nombreCompleto, Direccion direccion) {
        super(idPersona, nombreCompleto);
        this.direccion = direccion;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public abstract void addViaje(Viaje viaje);

    public abstract int getNroViajes();

}
