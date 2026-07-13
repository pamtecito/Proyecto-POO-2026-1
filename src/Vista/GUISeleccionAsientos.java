package Vista;

import Controlador.SistemaVentaPasaje;
import Modelo.TipoDocumento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

public class GUISeleccionAsientos extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel panelAsientos;

    private ArrayList<JCheckBox> checks = new ArrayList<>();
    private ArrayList<Integer> asientosSeleccionados = new ArrayList<>();

    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fechaVenta;
    private LocalTime hora;
    private String patenteBus;
    private int cantPasajes;

    public GUISeleccionAsientos(String idDocumento, TipoDocumento tipo, String[] asientos, LocalDate fechaVenta, LocalTime hora, String patenteBus, int cantPasajes) {
        this.idDocumento = idDocumento;
        this.tipo = tipo;
        this.fechaVenta = fechaVenta;
        this.hora = hora;
        this.patenteBus = patenteBus;
        this.cantPasajes = cantPasajes;

        setContentPane(contentPane);
        setModal(true);
        setTitle("Seleccion de asientos");

        panelAsientos.setLayout(new GridLayout(0, 5, 10, 5));

        for (int i = 0; i < asientos.length; i += 4) {

            agregarAsiento(asientos[i]);
            agregarAsiento(asientos[i + 1]);

            JLabel pasillo = new JLabel("|       |", SwingConstants.CENTER);
            panelAsientos.add(pasillo);

            agregarAsiento(asientos[i + 3]);
            agregarAsiento(asientos[i + 2]);
        }
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {onOK();
            }});

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {onCancel();
            }});

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
        asientosSeleccionados.clear();
        for (JCheckBox chk : checks) {
            if (chk.isSelected()) {
                asientosSeleccionados.add(Integer.parseInt(chk.getText()));
            }
        }

        if (asientosSeleccionados.size() != cantPasajes) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar exactamente " + cantPasajes + " asientos.");
            return;
        }

        for (Integer asiento : asientosSeleccionados) {
            GUISeleccionPasajero guiPasajero = new GUISeleccionPasajero(idDocumento, tipo, fechaVenta, hora, patenteBus, asiento);
            guiPasajero.pack();
            guiPasajero.setLocationRelativeTo(this);
            guiPasajero.setVisible(true);
        }
        Optional<Integer> montoTotal = SistemaVentaPasaje.getInstance().getMontoVenta(idDocumento, tipo);

        if (montoTotal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No fue posible obtener el monto de la venta.");
            return;
        }

        GUIPagoVenta guiPago = new GUIPagoVenta(idDocumento, tipo, montoTotal.get());
        guiPago.pack();
        guiPago.setLocationRelativeTo(this);
        guiPago.setVisible(true);
        dispose();
    }

    private void onCancel() {
        asientosSeleccionados.clear();
        dispose();
    }
    private void agregarAsiento(String asiento) {
        JCheckBox chk = new JCheckBox();

        if (asiento.equals("*")) {
            chk.setText("Ocupado");
            chk.setEnabled(false);
        } else {
            chk.setText(asiento);
        }

        checks.add(chk);
        panelAsientos.add(chk);
    }
}