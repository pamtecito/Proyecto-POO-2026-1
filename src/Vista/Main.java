package Vista;

import Controlador.*;

public class Main {
    public static void main(String[] args) {
        SistemaVentaPasaje.getInstance().readDatosSistema();
        UISVP.getInstance().menu();

    }
}

