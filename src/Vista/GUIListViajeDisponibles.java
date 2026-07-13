package Vista;

import Controlador.SistemaVentaPasaje;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GUIListViajeDisponibles extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buscarButton;
    private JTable tablaViajes;
    private JTextField strFecha;
    private JComboBox<String> comboOrigen;
    private JComboBox<String> comboDestino;
    private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public GUIListViajeDisponibles() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Viajes disponibles");
        inicializarTabla();
        cargarComunas();
        actualizarDestinos();

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarViajes();
            }});
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
        comboOrigen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarDestinos();
            }});
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // call onCancel() when cross is clicked
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
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void inicializarTabla() {
        String[] columnas = {"Bus", "Salida", "Valor", "Asientos disponibles"};
        tablaViajes.setModel(new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;}});
    }

    private void cargarComunas() {
        comboOrigen.removeAllItems();
        comboDestino.removeAllItems();
        String[][] viajes = SistemaVentaPasaje.getInstance().listViajes();
        for (int i = 0; i < viajes.length; i++) {
            String origen = viajes[i][6];
            String destino = viajes[i][7];
            agregarSiNoExiste(comboOrigen, origen);
            agregarSiNoExiste(comboOrigen, destino);
            agregarSiNoExiste(comboDestino, origen);
            agregarSiNoExiste(comboDestino, destino);
        }
    }

    private void actualizarDestinos() {
        String origen = (String) comboOrigen.getSelectedItem();
        String destinoSeleccionado = (String) comboDestino.getSelectedItem();

        comboDestino.removeAllItems();
        String[][] viajes = SistemaVentaPasaje.getInstance().listViajes();

        for (int i = 0; i < viajes.length; i++) {
            String comunaSalida = viajes[i][6];
            String comunaLlegada = viajes[i][7];

            if (!comunaSalida.equals(origen)) {
                agregarSiNoExiste(comboDestino, comunaSalida);
            }

            if (!comunaLlegada.equals(origen)) {
                agregarSiNoExiste(comboDestino, comunaLlegada);
            }
        }

        if (destinoSeleccionado != null) {
            for (int i = 0; i < comboDestino.getItemCount(); i++) {
                if (comboDestino.getItemAt(i).equals(destinoSeleccionado)) {
                    comboDestino.setSelectedItem(destinoSeleccionado);
                }
            }
        }
    }

    private void agregarSiNoExiste(JComboBox<String> combo, String comuna) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).equals(comuna)) {
                return;
            }
        }
        combo.addItem(comuna);
    }

    private void cargarViajes() {
        String fechaTexto = strFecha.getText().trim();
        if (fechaTexto.isBlank()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la fecha del viaje.");
            return;
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaTexto, formato);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "La fecha debe tener el formato dd/MM/yyyy.");
            return;
        }

        String origen = (String) comboOrigen.getSelectedItem();
        String destino = (String) comboDestino.getSelectedItem();

        if (origen == null || destino == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar origen y destino.");
            return;
        }

        String[][]horarios = SistemaVentaPasaje.getInstance().getHorariosDisponibles(fecha, origen, destino, 1);
        DefaultTableModel modelo = (DefaultTableModel) tablaViajes.getModel();

        modelo.setRowCount(0);
        if (horarios.length == 0) {
            JOptionPane.showMessageDialog(this, "No existen viajes disponibles para la fecha, origen y destino indicados.");
            return;
        }

        for (int i = 0; i < horarios.length; i++) {
            modelo.addRow(new Object[]{horarios[i][0], horarios[i][1], "$" + horarios[i][2], horarios[i][3]});
        }
    }


}