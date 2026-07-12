package Vista;

import Controlador.SistemaVentaPasaje;
import Excepciones.SVPException;
import Modelo.TipoDocumento;
import Utilidades.IdPersona;
import Utilidades.Pasaporte;
import Utilidades.Rut;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GUIVentaPasaje extends JDialog {
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private JPanel contentPane;
    private JTextField strIdDocumento;
    private JRadioButton boletaRadioButton;
    private JRadioButton facturaRadioButton;
    private JTextField strFecha;
    private JTextField strOrigen;
    private JTextField strDestino;
    private JComboBox tipoDocumento;
    private JTextField strCantPasajes;
    private JButton buscarViajesButton;
    private JTextField RUTOPASAPORTE;
    private JLabel RUToPASA;
    private JLabel nacionalidadlbl;
    private JTextField strNacionalidad;
    private JRadioButton RUTRadioButton;
    private JRadioButton pasaporteRadioButton;
    private JComboBox comboBox1;
    private JComboBox comboBox2;


    public GUIVentaPasaje() {
        setTitle("Venta de pasajes");
        setContentPane(contentPane);
        setModal(true);
        setLocationRelativeTo(null);

        nacionalidadlbl.setVisible(false);
        strNacionalidad.setVisible(false);



        // call onCancel() when cross is clicked

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        buscarViajesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarViajes();
            }
        });

        RUTRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarTipoDocumento();
            }
        });

        pasaporteRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarTipoDocumento();
            }
        });
    }

    private void buscarViajes() {
        String idDocumento = strIdDocumento.getText();
        String fecha = strFecha.getText();
        IdPersona idCliente = null;
        LocalDate fechaVenta;
        String opcion = (String) tipoDocumento.getSelectedItem();
        if (!verFormato(fecha)) {
            JOptionPane.showMessageDialog(this, "El formato de fecha es incorrecto (debe ser DD/MM/YYYY).");
            return;
        } else {
            fechaVenta = LocalDate.parse(fecha, formato);
        }
        try {
            if (opcion.equals("RUT")) {
                String rut = RUTOPASAPORTE.getText();
                verFormatoRut(rut);
                idCliente = Rut.of(rut);
            } else {
                String numero = RUTOPASAPORTE.getText();
                String nacionalidad = strNacionalidad.getText();
                if (numero.length() != 9) {
                    JOptionPane.showMessageDialog(this, "El pasaporte debe tener 9 caracteres.");
                    return;
                }
                if (nacionalidad.isBlank()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar la nacionalidad.");
                    return;
                }
                idCliente = Pasaporte.of(numero, nacionalidad);
            }
        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        }
        TipoDocumento tipo;
        if(boletaRadioButton.isSelected()){
            tipo = TipoDocumento.BOLETA;
        } else {
            tipo = TipoDocumento.FACTURA;
        }

        int cantPasajes;
        try {
            cantPasajes = Integer.parseInt(strCantPasajes.getText());
            if (cantPasajes <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que cero.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            return;
        }
        try {
            //SistemaVentaPasaje.getInstance().iniciaVenta(idDocumento,tipo,fechaVenta,idCliente,cantPasajes);
        } catch (SVPException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
            return;
        }

    }

    public static void main(String[] args) {
        GUIVentaPasaje dialog = new GUIVentaPasaje();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
    private void verFormatoRut(String rut) {
        //Formatos = 1.111.111-1 11.111.111-1

        char dv;
        char guion;
        if  (rut.length() < 11 || rut.length() > 12) {
            throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
        }
        if (rut.length() == 11) {
            if (rut.charAt(1) != '.' || rut.charAt(5) != '.') {
                throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
            }
            dv = rut.charAt(10);
            guion = rut.charAt(9);
            if (!((dv >= '0' && dv <= '9') || dv == 'k' || dv == 'K')) {
                throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
            }
            if (guion != '-') {
                throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
            }
        }

        if  (rut.length() == 12) {
            if (rut.charAt(2) != '.' || rut.charAt(6) != '.') {
                throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
            }
            dv = rut.charAt(11);
            guion = rut.charAt(10);
            if (!((dv >= '0' && dv <= '9') || dv == 'k' || dv == 'K')) {
                throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
            }
            if (guion != '-') {
                throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
            }
        }
        for (int i = 0; i < rut.length() - 2; i++) {
            char caracter = rut.charAt(i);
            if (caracter != '.' && (caracter < '0' || caracter > '9')) {
                throw new SVPException("El rut ingresado no es correcto (no cumple con el formato)");
            }
        }
    }
    private boolean verFormato(String fecha) {
        if (fecha.length() != 10) {
            return false;
        }
        if (fecha.charAt(2) != '/' || fecha.charAt(5) != '/') {
            return false;
        }
        for (int i = 0; i < fecha.length(); i++) {
            if (i != 2 && i != 5) {
                char esNum = fecha.charAt(i);
                if (esNum < '0' || esNum > '9') {
                    return false;
                }
            }
        }
        return true;
    }

    private void cambiarTipoDocumento() {
        if (RUTRadioButton.isSelected()){
            RUToPASA.setText("RUT:");
            nacionalidadlbl.setVisible(false);
            strNacionalidad.setVisible(false);
        }else {
            RUToPASA.setText("N° Pasaporte:");
            nacionalidadlbl.setVisible(true);
            strNacionalidad.setVisible(true);
        }
        pack();
    }

}
