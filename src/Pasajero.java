    private Nombre nomContacto;
    private String fonoContacto;

    /*public Pasajero(IdPersona id, Nombre nombreCompleto, String telefono) {
        super(id,nombreCompleto, telefono);
    }*/


    public Nombre getNomContacto() {
        return nomContacto;
    }

    public void setNomContacto(Nombre nom) {
        this.nomContacto = nom;
    }

    public String getFonoContacto(){
        return fonoContacto;
    }

    public void setFonoContacto(String fono) {
        this.fonoContacto = fono;
    }
}