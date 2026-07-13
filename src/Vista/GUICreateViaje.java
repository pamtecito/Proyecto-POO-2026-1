package Vista;

import Controlador.ControladorEmpresa;
import Controlador.SistemaVentaPasaje;
import Excepciones.SVPException;
import Modelo.Auxiliar;
import Modelo.Bus;
import Modelo.Conductor;
import Modelo.Tripulante;
import Utilidades.IdPersona;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GUICreateViaje extends JDialog {

    private JPanel contentPane;
    private JButton buttonCrear;
    private JButton buttonVolver;
    private ArrayList<Bus> buses = new ArrayList<>();
    private ArrayList<Auxiliar> auxiliares = new ArrayList<>();
    private ArrayList<Conductor> conductores = new ArrayList<>();
    private JTextField strFecha;
    private JTextField strHora;
    private JTextField strPrecio;
    private JTextField strDuracionMinutos;

    private JComboBox<String> comboBoxBus;
    private JComboBox<String> comboBoxAux;
    private JComboBox<String> comboBoxCond;
    private JComboBox<String> comboBoxSalida;
    private JComboBox<String> comboBoxLlegada;
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

    public GUICreateViaje() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Creación de viaje");
        getRootPane().setDefaultButton(buttonCrear);
        cargar();

        buttonCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        comboBoxSalida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarLlegadas();
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

    private void cargar() {
        comboBoxBus.removeAllItems();
        comboBoxAux.removeAllItems();
        comboBoxCond.removeAllItems();
        comboBoxSalida.removeAllItems();
        comboBoxLlegada.removeAllItems();
        buses.clear();
        auxiliares.clear();
        conductores.clear();
        Object[][] empresas = ControladorEmpresa.getInstance().listEmpresas();

        for (Object[] fila : empresas) {
            Tripulante[] tripulantes = (Tripulante[]) fila[3];
            Bus[] busesEmpresa = (Bus[]) fila[4];

            for (Bus bus : busesEmpresa) {
                agregarBusSiNoExiste(bus);
            }

            for (Tripulante tripulante : tripulantes) {
                if (tripulante instanceof Auxiliar auxiliar) {
                    agregarAuxiliarSiNoExiste(auxiliar);

                } else if (tripulante instanceof Conductor conductor) {
                    agregarConductorSiNoExiste(conductor);
                }
            }
        }

        String[][] viajes = SistemaVentaPasaje.getInstance().listViajes();
        for (int i = 0; i < viajes.length; i++) {
            agregarSiNoExiste(comboBoxSalida, viajes[i][6]);
            agregarSiNoExiste(comboBoxSalida, viajes[i][7]);

            agregarSiNoExiste(comboBoxLlegada, viajes[i][6]);
            agregarSiNoExiste(comboBoxLlegada, viajes[i][7]);
        }

        actualizarLlegadas();
    }

    private void onOK() {
        String fechaTexto = strFecha.getText().trim();
        String horaTexto = strHora.getText().trim();
        String precioTexto = strPrecio.getText().trim();
        String duracionTexto = strDuracionMinutos.getText().trim();

        if (fechaTexto.isBlank() || horaTexto.isBlank() || precioTexto.isBlank() || duracionTexto.isBlank()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos.");
            return;
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaTexto, formatoFecha);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "La fecha debe tener formato dd/MM/yyyy.");
            return;
        }
        LocalTime hora;
        try {
            hora = LocalTime.parse(horaTexto, formatoHora);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "La hora debe tener formato HH:mm.");
            return;
        }
        int precio;
        try {
            precio = Integer.parseInt(precioTexto);
            if (precio <= 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser mayor que cero.");
                return;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número entero.");
            return;
        }
        int duracion;
        try {
            duracion = Integer.parseInt(duracionTexto);
            if (duracion <= 0) {
                JOptionPane.showMessageDialog(this, "La duración debe ser mayor que cero.");
                return;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La duración debe ser un número entero.");
            return;
        }

        String salida = (String) comboBoxSalida.getSelectedItem();
        String llegada = (String) comboBoxLlegada.getSelectedItem();
        int indiceBus = comboBoxBus.getSelectedIndex();
        int indiceAuxiliar = comboBoxAux.getSelectedIndex();
        int indiceConductor = comboBoxCond.getSelectedIndex();

        if (indiceBus == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un bus.");
            return;
        }

        if (indiceAuxiliar == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un auxiliar.");
            return;
        }

        if (indiceConductor == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un conductor.");
            return;
        }

        Bus bus = buses.get(indiceBus);
        Auxiliar auxiliar = auxiliares.get(indiceAuxiliar);
        Conductor conductor = conductores.get(indiceConductor);

        if (salida == null || llegada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar terminal de salida y llegada.");
            return;
        }

        if (salida.equals(llegada)) {
            JOptionPane.showMessageDialog(this, "La salida y la llegada deben ser diferentes.");
            return;
        }

        IdPersona[] tripulantes = {auxiliar.getIdPersona(), conductor.getIdPersona()};
        String[] comunas = {salida, llegada};

        try {
            SistemaVentaPasaje.getInstance().createViaje(fecha, hora, precio, duracion, bus.getPatente(), tripulantes, comunas);
            JOptionPane.showMessageDialog(this, "Viaje creado correctamente.");
            dispose();

        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al crear el viaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }

    private void agregarSiNoExiste(JComboBox<String> combo, String comuna) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).equals(comuna)) {
                return;
            }
        }
        combo.addItem(comuna);
    }

    private void agregarBusSiNoExiste(Bus bus) {
        for (Bus existente : buses) {
            if (existente.getPatente().equalsIgnoreCase(bus.getPatente())) {
                return;
            }
        }
        buses.add(bus);
        comboBoxBus.addItem(bus.getPatente());
    }

    private void agregarAuxiliarSiNoExiste(Auxiliar auxiliar) {
        for (Auxiliar existente : auxiliares) {
            if (existente.getIdPersona().equals(auxiliar.getIdPersona())) {
                return;
            }
        }
        auxiliares.add(auxiliar);
        comboBoxAux.addItem(auxiliar.getIdPersona().toString() + " - " + auxiliar.getNombreCompleto().toString());
    }

    private void agregarConductorSiNoExiste(Conductor conductor) {
        for (Conductor existente : conductores) {
            if (existente.getIdPersona()
                    .equals(conductor.getIdPersona())) {
                return;
            }
        }
        conductores.add(conductor);
        comboBoxCond.addItem(conductor.getIdPersona().toString() + " - " + conductor.getNombreCompleto().toString());
    }

    private void actualizarLlegadas() {
        String salida = (String) comboBoxSalida.getSelectedItem();
        String llegadaAnterior = (String) comboBoxLlegada.getSelectedItem();
        comboBoxLlegada.removeAllItems();
        String[][] viajes = SistemaVentaPasaje.getInstance().listViajes();
        for (int i = 0; i < viajes.length; i++) {
            String origen = viajes[i][6];
            String destino = viajes[i][7];

            if (!origen.equals(salida)) {
                agregarSiNoExiste(comboBoxLlegada, origen);
            }

            if (!destino.equals(salida)) {
                agregarSiNoExiste(comboBoxLlegada, destino);
            }
        }

        if (llegadaAnterior != null) {
            for (int i = 0; i < comboBoxLlegada.getItemCount(); i++) {

                if (comboBoxLlegada.getItemAt(i).equals(llegadaAnterior)) {
                    comboBoxLlegada.setSelectedItem(llegadaAnterior);
                }
            }
        }
    }
}