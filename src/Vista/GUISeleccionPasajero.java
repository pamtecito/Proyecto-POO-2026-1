package Vista;

import Controlador.SistemaVentaPasaje;
import Excepciones.SVPException;
import Modelo.TipoDocumento;
import Utilidades.IdPersona;
import Utilidades.Nombre;
import Utilidades.Pasaporte;
import Utilidades.Rut;
import Utilidades.Tratamiento;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class GUISeleccionPasajero extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JRadioButton RUTRadioButton;
    private JRadioButton pasaporteRadioButton;
    private JTextField RUToPASAPORTE;
    private JTextField strNacionalidad;

    private JPanel panelDocumento;
    private JPanel panelCrearPasajero;

    private JRadioButton srRadioButton;
    private JRadioButton sraRadioButton;

    private JTextField pasajeroNoExisteNombre;
    private JTextField noExisteApPaterno;
    private JTextField noExisteApMaterno;
    private JTextField noExisteTelefono;
    private JTextField nomContactoEmergencia;
    private JTextField fonoContactoEmergencia;
    private JLabel lblNacionalidad;
    private JLabel lblRUTOPASAPORTE;

    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    private LocalTime hora;
    private String patente;
    private int asiento;

    private IdPersona idPasajero;
    private boolean creandoPasajero = false;

    public GUISeleccionPasajero(String idDocumento, TipoDocumento tipo, LocalDate fecha, LocalTime hora, String patente, int asiento) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Pasajero del asiento " + asiento);

        this.idDocumento = idDocumento;
        this.tipo = tipo;
        this.fecha = fecha;
        this.hora = hora;
        this.patente = patente;
        this.asiento = asiento;

        panelCrearPasajero.setVisible(false);
        lblNacionalidad.setVisible(false);
        strNacionalidad.setVisible(false);

        lblRUTOPASAPORTE.setText("RUT");
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {onOK();
            }});

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {onCancel();
            }});

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

        // call onCancel() when cross is clicked

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if (!creandoPasajero) {
            try {
                idPasajero = crearIdPasajero();
            } catch (SVPException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos inválidos", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Optional<String> nombrePasajero = SistemaVentaPasaje.getInstance().getNombrePasajero(idPasajero);
            if (nombrePasajero.isPresent()) {
                JOptionPane.showMessageDialog(this, "Pasajero encontrado: " + nombrePasajero.get() + "\nSe asignará al asiento " + asiento + ".");
                venderPasaje();
                return;
            }
            JOptionPane.showMessageDialog(this, "El pasajero no existe.\nComplete sus datos para crearlo.");
            creandoPasajero = true;
            panelCrearPasajero.setVisible(true);
            buttonOK.setText("Crear y agregar");
            pack();
            return;
        }
        crearPasajeroNuevo();
    }

    private IdPersona crearIdPasajero() {
        String documento = RUToPASAPORTE.getText().trim();
        if (documento.isBlank()) {
            throw new SVPException("Debe ingresar la identificación del pasajero.");
        }
        if (RUTRadioButton.isSelected()) {
            return Rut.of(documento);
        }
        String nacionalidad = strNacionalidad.getText().trim();

        if (documento.length() != 9) {
            throw new SVPException("El pasaporte debe tener 9 caracteres.");
        }
        if (nacionalidad.isBlank()) {
            throw new SVPException("Debe ingresar la nacionalidad.");
        }
        return Pasaporte.of(documento, nacionalidad);
    }

    private void crearPasajeroNuevo() {
        if (!srRadioButton.isSelected() && !sraRadioButton.isSelected()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar Sr. o Sra.");
            return;
        }
        String nombres = pasajeroNoExisteNombre.getText().trim();
        String apellidoPaterno = noExisteApPaterno.getText().trim();
        String apellidoMaterno = noExisteApMaterno.getText().trim();
        String telefono = noExisteTelefono.getText().trim();
        String nombreContacto = nomContactoEmergencia.getText().trim();
        String telefonoContacto = fonoContactoEmergencia.getText().trim();
        if (nombres.isBlank() || apellidoPaterno.isBlank() || apellidoMaterno.isBlank() || telefono.isBlank() || nombreContacto.isBlank() || telefonoContacto.isBlank()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los datos del pasajero.");
            return;
        }
        Nombre nombrePasajero = new Nombre();
        if (srRadioButton.isSelected()) {
            nombrePasajero.setTratamiento(Tratamiento.SR);
        } else {
            nombrePasajero.setTratamiento(Tratamiento.SRA);
        }
        nombrePasajero.setNombres(nombres);
        nombrePasajero.setApellidoPaterno(apellidoPaterno);
        nombrePasajero.setApellidoMaterno(apellidoMaterno);
        Nombre contactoEmergencia = new Nombre();
        contactoEmergencia.setNombres(nombreContacto);
        try {
            SistemaVentaPasaje.getInstance().createPasajero(idPasajero, nombrePasajero, telefono, contactoEmergencia, telefonoContacto);
            venderPasaje();
        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void venderPasaje() {
        try {
            SistemaVentaPasaje.getInstance().vendePasaje(idDocumento, tipo, fecha, hora, patente, asiento, idPasajero);
            JOptionPane.showMessageDialog(this, "Pasaje agregado correctamente." + " Asiento: " + asiento);
            dispose();

        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al vender el pasaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarTipoDocumento() {
        if (RUTRadioButton.isSelected()) {
            lblRUTOPASAPORTE.setText("RUT");
            lblNacionalidad.setVisible(false);
            strNacionalidad.setVisible(false);
            strNacionalidad.setText("");
        } else {
            lblRUTOPASAPORTE.setText("N° Pasaporte");
            lblNacionalidad.setVisible(true);
            strNacionalidad.setVisible(true);
        }
        pack();
    }

    private void onCancel() {
        JOptionPane.showMessageDialog(this, "Debe ingresar el pasajero correspondiente al asiento " + asiento + ".");
    }
}