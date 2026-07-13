package Vista;

import Controlador.SistemaVentaPasaje;
import Excepciones.SVPException;
import Modelo.Pasaje;
import Modelo.TipoDocumento;

import javax.swing.*;
import java.awt.event.*;

public class GUIPagoVenta extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JRadioButton efectivoRadioButton;
    private JRadioButton tarjetaRadioButton;

    private JTextField strNroTarjeta;

    private JLabel lblIdDocumento;
    private JLabel lblTipoDocumento;
    private JLabel lblMontoTotal;
    private JLabel lblNroTarjeta;

    private String idDocumento;
    private TipoDocumento tipo;
    private int monto;

    public GUIPagoVenta(String idDocumento, TipoDocumento tipo, int monto) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Pago de la venta");

        this.idDocumento = idDocumento;
        this.tipo = tipo;
        this.monto = monto;

        lblIdDocumento.setText(idDocumento);
        lblTipoDocumento.setText(tipo.toString());
        lblMontoTotal.setText("$" + monto);

        efectivoRadioButton.setSelected(true);

        lblNroTarjeta.setVisible(false);
        strNroTarjeta.setVisible(false);
        strNroTarjeta.setText("");

        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {onOK();
            }});

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {onCancel();
            }});

        efectivoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarTipoPago();
            }
        });

        tarjetaRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarTipoPago();
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

    private void cambiarTipoPago() {
        boolean pagoConTarjeta = tarjetaRadioButton.isSelected();
        lblNroTarjeta.setVisible(pagoConTarjeta);
        strNroTarjeta.setVisible(pagoConTarjeta);
        if (pagoConTarjeta) {
            strNroTarjeta.requestFocusInWindow();
        } else {
            strNroTarjeta.setText("");
        }
        pack();
    }

    private void onOK() {
        try {
            if (efectivoRadioButton.isSelected()) {
                SistemaVentaPasaje.getInstance().pagaVenta(idDocumento, tipo);
            } else {
                String numeroTexto = strNroTarjeta.getText().trim();
                if (numeroTexto.isBlank()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar el número de tarjeta.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                long numeroTarjeta;
                try {
                    numeroTarjeta = Long.parseLong(numeroTexto);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El número de tarjeta debe contener solo dígitos.", "Número de tarjeta inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (numeroTarjeta <= 0) {
                    JOptionPane.showMessageDialog(this, "El número de tarjeta debe ser mayor que cero.", "Número de tarjeta inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(numeroTexto.length()!=16){
                    JOptionPane.showMessageDialog(this, "El numero de la tarjeta debe contener 16 numeros exactos.", "Numero de tarjeta inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                SistemaVentaPasaje.getInstance().pagaVenta(idDocumento, tipo, numeroTarjeta);
            }
            SistemaVentaPasaje.getInstance().generatePasajesVenta(idDocumento, tipo);
            JOptionPane.showMessageDialog(this, "Venta pagada correctamente. Monto total: $" + monto + "\n" + "Los pasajes electrónicos fueron generados.", "Venta finalizada", JOptionPane.INFORMATION_MESSAGE);
            dispose();



        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al pagar la venta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();

    }
}