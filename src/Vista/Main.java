package Vista;

import Controlador.*;

public class Main {
    public static void main(String[] args) {
        GUIMenu dialog = new GUIMenu();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        UISVP.getInstance().menu();

    }
}

