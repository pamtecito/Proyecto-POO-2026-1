package Vista;
import Controlador.ControladorEmpresa;
import Modelo.Bus;
import Modelo.Tripulante;
import Modelo.Venta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class GUIListEmpresas extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tablaEmpresas;

    public GUIListEmpresas() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Listado de empresas");
        inicializarTabla();
        cargarEmpresas();

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
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
    private void inicializarTabla() {
        String[] columnas = {"RUT Empresa", "Nombre", "URL", "N° Tripulantes", "N° Buses", "N° Ventas"};
        tablaEmpresas.setModel(new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }});
    }
    private void cargarEmpresas() {
        Object[][] empresas = ControladorEmpresa.getInstance().listEmpresas();
        DefaultTableModel modelo = (DefaultTableModel) tablaEmpresas.getModel();
        modelo.setRowCount(0);

        if (empresas.length == 0) {
            JOptionPane.showMessageDialog(this, "No existen empresas registradas.");
            return;
        }

        for (Object[] empresa : empresas) {
            Tripulante[] tripulantes = (Tripulante[]) empresa[3];
            Bus[] buses = (Bus[]) empresa[4];
            Venta[] ventas = (Venta[]) empresa[5];
            modelo.addRow(new Object[]{empresa[0], empresa[1], empresa[2], tripulantes.length, buses.length, ventas.length});
        }
    }
}