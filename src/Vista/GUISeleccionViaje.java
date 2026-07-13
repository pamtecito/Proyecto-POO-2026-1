package Vista;

import Controlador.SistemaVentaPasaje;
import Excepciones.SVPException;
import Modelo.TipoDocumento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class GUISeleccionViaje extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tablaViajes;
    private String[][] horarios;
    private LocalDate fechaVenta;
    private int cantPasajes;
    private String idDocumento;
    private TipoDocumento tipo;
    public GUISeleccionViaje(String idDocumento, TipoDocumento tipo, String[][] horarios, LocalDate fechaVenta, int cantPasajes) {
        setContentPane(contentPane);
        setModal(true);
        this.horarios = horarios;
        this.fechaVenta = fechaVenta;
        this.cantPasajes = cantPasajes;
        this.idDocumento = idDocumento;
        this.tipo = tipo;
        String[] columnas = {"NÂ°", "Bus", "Salida", "Valor", "Asientos"};
        Object[][] datos = new Object[horarios.length][5];

        for (int i = 0; i < horarios.length; i++) {
            datos[i][0] = i + 1;
            datos[i][1] = horarios[i][0];
            datos[i][2] = horarios[i][1];
            datos[i][3] = horarios[i][2];
            datos[i][4] = horarios[i][3];
        }
        tablaViajes.setModel(new DefaultTableModel(datos, columnas));
        tablaViajes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaViajes.setModel(new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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
        int fila = tablaViajes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un viaje.");
            return;
        }

        try {
            String patenteBus = horarios[fila][0];
            LocalTime hora = LocalTime.parse(horarios[fila][1]);
            String[] asientos = SistemaVentaPasaje.getInstance().listAsientosDeViaje(fechaVenta, hora, patenteBus);
            GUISeleccionAsientos gui = new GUISeleccionAsientos(idDocumento, tipo, asientos, fechaVenta, hora, patenteBus, cantPasajes);

            gui.pack();
            gui.setLocationRelativeTo(this);
            gui.setVisible(true);

            dispose();
        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    private void onCancel() {
        // add your code here if necessary

        dispose();
    }

}