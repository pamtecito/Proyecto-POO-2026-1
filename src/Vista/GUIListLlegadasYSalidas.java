package Vista;

import Controlador.ControladorEmpresa;
import Controlador.SistemaVentaPasaje;
import Excepciones.SVPException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class GUIListLlegadasYSalidas extends JDialog {
    private JPanel contentPane;
    private JComboBox comboTerminal;
    private JTextField txtTerminal;
    private JButton buscarButton;
    private JTable table1;
    private JTextField txtFecha;
    private JButton atrasButton;
    private SistemaVentaPasaje svp;
    private ControladorEmpresa ce;

    public GUIListLlegadasYSalidas() {
        svp = SistemaVentaPasaje.getInstance();
        ce = ControladorEmpresa.getInstance();

        setContentPane(contentPane);
        setModal(true);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscar();
            }
        });

        atrasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                GUIMenu menu = new GUIMenu();
                menu.setVisible(true);
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    }

    private void buscar(){
        try {
        String nombreTerminal = txtTerminal.getText().trim();
        if (nombreTerminal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un terminal.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        String fechaTexto = txtFecha.getText().trim();

        if (fechaTexto.isEmpty()){
            JOptionPane.showMessageDialog(this, "Debe ingresar una fecha.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        LocalDate fecha = LocalDate.parse(fechaTexto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));



        String[][] datos = ce.listLlegadasSalidasTerminal(nombreTerminal,fecha);

        String[] columnaPrincipal = {"Tipo", "Hora", "Patente", "Empresa", "Ventas"};

        table1.setModel(new DefaultTableModel(datos, columnaPrincipal));
        } catch (SVPException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }catch (DateTimeParseException e){
            JOptionPane.showMessageDialog(this, "La fecha debe tener formato dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
