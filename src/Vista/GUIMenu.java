package Vista;

import javax.swing.*;
import java.awt.event.*;
import Controlador.*;
import Excepciones.*;

public class GUIMenu extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton crearUnViajeButton;
    private JButton ventaDePasajeButton;
    private JButton llegadasYSalidasDeButton;
    private JButton viajesDisponiblesButton;
    private JButton pasajerosDeUnViajeButton;
    private JButton leerDatosInicialesButton;
    private JButton guardarDatosDelSistemaButton;
    private JButton recuperarDatosDelSistemaButton;

    public GUIMenu() {
        setContentPane(contentPane);
        setModal(false);
        setTitle("Menú Sistema Venta Pasaje");
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        crearUnViajeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICreateViaje cv = new GUICreateViaje();
                cv.setLocationRelativeTo(null);
                cv.pack();
                cv.setVisible(true);
            }
        });

        ventaDePasajeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIVentaPasaje vp = new GUIVentaPasaje();
                vp.setLocationRelativeTo(GUIMenu.this);
                vp.pack();
                vp.setVisible(true);
            }
        });

        llegadasYSalidasDeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIListLlegadasYSalidas list = new GUIListLlegadasYSalidas();
                list.setLocationRelativeTo(null);
                list.pack();
                list.setVisible(true);
            }
        });

        viajesDisponiblesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIListViajeDisponibles list = new GUIListViajeDisponibles();
                list.setLocationRelativeTo(null);
                list.pack();
                list.setVisible(true);
            }
        });

        pasajerosDeUnViajeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIListEmpresas list = new GUIListEmpresas();
                list.setLocationRelativeTo(null);
                list.pack();
                list.setVisible(true);
            }
        });

        leerDatosInicialesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SistemaVentaPasaje.getInstance().readDatosIniciales();
                    JOptionPane.showMessageDialog(GUIMenu.this, "Datos iniciales cargados correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);

                } catch (SVPException ex) {
                    JOptionPane.showMessageDialog(GUIMenu.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        guardarDatosDelSistemaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SistemaVentaPasaje.getInstance().saveDatosSistema();
                    JOptionPane.showMessageDialog(GUIMenu.this, "Datos guardados correctamente en el sistema.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                } catch (SVPException ex) {
                    JOptionPane.showMessageDialog(GUIMenu.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        recuperarDatosDelSistemaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    SistemaVentaPasaje.getInstance().readDatosSistema();
                    JOptionPane.showMessageDialog(GUIMenu.this, "Datos del sistema recuperados correctamente.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                } catch (SVPException ex){
                    JOptionPane.showMessageDialog(GUIMenu.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        // add your code here if necessary
        System.exit(0);
        dispose();
    }

}
