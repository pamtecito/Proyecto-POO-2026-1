import java.security.Permission;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Cliente extends Persona {
    private String email;
    private ArrayList<Venta> ventas;

    public Cliente(IdPersona idPersona, Nombre nom, String email) {
        super(idPersona, nom);
        this.email = email;
        ventas = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addVenta(Venta venta){
        ventas.add(venta);
    }

    public Venta[] getVentas(){
        return ventas.toArray(new Venta[0]);
    }

}







