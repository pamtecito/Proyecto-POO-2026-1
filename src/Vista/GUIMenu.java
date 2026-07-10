package Vista;

import javax.swing.*;
import java.awt.event.*;

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
        setTitle("Menú Venta Pasaje");
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        crearUnViajeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUICreateViaje cv = new GUICreateViaje();
                cv.pack();
                cv.setVisible(true);
            }
        });

        ventaDePasajeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIVentaPasaje vp = new GUIVentaPasaje();
                vp.pack();
                vp.setVisible(true);
            }
        });

        llegadasYSalidasDeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIListLlegadasYSalidas list = new GUIListLlegadasYSalidas();
                list.pack();
                list.setVisible(true);
            }
        });

        viajesDisponiblesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIListViajeDisponibles list = new GUIListViajeDisponibles();
                list.pack();
                list.setVisible(true);
            }
        });

        pasajerosDeUnViajeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIListPasajerosViaje list = new GUIListPasajerosViaje();
                list.pack();
                list.setVisible(true);
            }
        });

        leerDatosInicialesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIReadDatosIniciales rdi = new GUIReadDatosIniciales();
                rdi.pack();;
                rdi.setVisible(true);
            }
        });

        guardarDatosDelSistemaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUISaveSystemData ssd = new GUISaveSystemData();
                ssd.pack();
                ssd.setVisible(true);
            }
        });

        recuperarDatosDelSistemaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIRecoverSystemData rsd = new GUIRecoverSystemData();
                rsd.pack();
                rsd.setVisible(true);
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
        dispose();
    }

}
