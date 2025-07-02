// Archivo: com/capsulas/model/UserCapsula.java
package com.capsulas.model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*; // Necesario para BorderLayout, GridLayout, FlowLayout, etc.
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

public class UserCapsula extends JFrame {
    private CapsulaDAO capsulaDAO = new CapsulaDAOImpl();

    // --- Componentes para la PESTAÑA de ADMINISTRACIÓN ---
    private JTable adminCapsulaTable;
    private DefaultTableModel adminTableModel;
    private JTextField txtId, txtNombre, txtCategoriaAdmin;
    private JTextArea txtContenido;
    private JButton btnCrear, btnActualizar, btnEliminar, btnLimpiarCampos;

    // --- Componentes para la PESTAÑA de CLIENTE ---
    private JTable clientCapsulaTable;
    private DefaultTableModel clientTableModel;
    private JTextField txtCategoriaClienteFiltro;
    private JButton btnFiltrar, btnVerDetalles, btnSistemaFeedback, btnPlanesPago, btnDescargarContenido, btnSolicitarEnvio;

    public UserCapsula() {
        setTitle("Gestión Unificada de Cápsulas");
        setSize(1000, 700); // Ajusta el tamaño de la ventana principal
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana

        initComponents(); // Llama al método que crea todos los componentes manualmente

        // Configuración de modelos de tabla (esto sigue siendo igual)
        String[] columnNames = {"ID", "Nombre", "Contenido", "Categoría"};
        adminTableModel = new DefaultTableModel(columnNames, 0);
        adminCapsulaTable.setModel(adminTableModel);

        clientTableModel = new DefaultTableModel(columnNames, 0);
        clientCapsulaTable.setModel(clientTableModel);

        // Cargar datos iniciales
        loadAdminCapsulasIntoTable();
        loadClientCapsulasIntoTable();

        // Añadir manejadores de eventos
        addEventHandlers();
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // --- Panel de Administración (Pestaña 1) ---
        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new BorderLayout(10, 10)); // Espacio entre componentes

        // ******* INICIO DE LA SOLUCIÓN: Nuevo panel para agrupar el formulario y los botones *******
        JPanel topControlPanel = new JPanel();
        topControlPanel.setLayout(new BorderLayout(10, 10)); // Usar BorderLayout para este panel combinado

        // Panel de entrada de datos (formulario) - Se mantiene como estaba
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtId = new JTextField();
        txtId.setEditable(false);
        txtNombre = new JTextField(20);
        txtContenido = new JTextArea(5, 20);
        JScrollPane scrollPaneContenido = new JScrollPane(txtContenido);
        txtCategoriaAdmin = new JTextField(20);

        formPanel.add(new JLabel("ID:"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Contenido:"));
        formPanel.add(scrollPaneContenido);
        formPanel.add(new JLabel("Categoría:"));
        formPanel.add(txtCategoriaAdmin);

        // Añadir el formPanel al nuevo topControlPanel en la región NORTH
        topControlPanel.add(formPanel, BorderLayout.NORTH);

        // Panel de botones - Se mantiene como estaba
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnCrear = new JButton("Crear");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiarCampos = new JButton("Limpiar Campos");

        buttonPanel.add(btnCrear);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnLimpiarCampos);

        // Añadir el buttonPanel al nuevo topControlPanel en la región CENTER
        topControlPanel.add(buttonPanel, BorderLayout.CENTER);

        // Añadir el panel combinado (formulario + botones) al adminPanel principal de la pestaña en la región NORTH
        adminPanel.add(topControlPanel, BorderLayout.NORTH);
        // ******* FIN DE LA SOLUCIÓN *******

        // Tabla de administración - Ahora ocupa la región CENTER del adminPanel principal
        adminCapsulaTable = new JTable();
        JScrollPane adminScrollPane = new JScrollPane(adminCapsulaTable);
        adminScrollPane.setPreferredSize(new Dimension(800, 250));

        adminPanel.add(adminScrollPane, BorderLayout.CENTER); // La tabla ahora ocupa el CENTRO principal

        // Añadir el panel de administración como una pestaña
        tabbedPane.addTab("Administración", adminPanel);

        // --- Panel de Cliente (Pestaña 2) ---
        JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new BorderLayout(10, 10));

        // Panel de filtro y acciones de cliente
        JPanel clientControlPanel = new JPanel();
        clientControlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        txtCategoriaClienteFiltro = new JTextField(15);
        btnFiltrar = new JButton("Filtrar por Categoría");
        btnVerDetalles = new JButton("Ver Detalles de Cápsula");
        btnSistemaFeedback = new JButton("Feedback");
        btnPlanesPago = new JButton("Planes de Pago");
        btnDescargarContenido = new JButton("Descargar");
        btnSolicitarEnvio = new JButton("Solicitar Envío");

        clientControlPanel.add(new JLabel("Filtrar Categoría:"));
        clientControlPanel.add(txtCategoriaClienteFiltro);
        clientControlPanel.add(btnFiltrar);
        clientControlPanel.add(btnVerDetalles);
        clientControlPanel.add(btnSistemaFeedback);
        clientControlPanel.add(btnPlanesPago);
        clientControlPanel.add(btnDescargarContenido);
        clientControlPanel.add(btnSolicitarEnvio);

        // Tabla de cliente (visualización)
        clientCapsulaTable = new JTable();
        JScrollPane clientScrollPane = new JScrollPane(clientCapsulaTable);
        clientScrollPane.setPreferredSize(new Dimension(800, 350));

        clientPanel.add(clientControlPanel, BorderLayout.NORTH);
        clientPanel.add(clientScrollPane, BorderLayout.CENTER);

        // Añadir el panel de cliente como una pestaña
        tabbedPane.addTab("Cliente", clientPanel);

        // Añadir el JTabbedPane al JFrame principal
        add(tabbedPane);
    }

    private void addEventHandlers() {
        // --- Manejadores de Eventos para la Pestaña de Administración ---
        btnCrear.addActionListener(e -> crearCapsula());
        btnActualizar.addActionListener(e -> actualizarCapsula());
        btnEliminar.addActionListener(e -> eliminarCapsula());
        btnLimpiarCampos.addActionListener(e -> clearAdminFields());

        adminCapsulaTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && adminCapsulaTable.getSelectedRow() != -1) {
                int selectedRow = adminCapsulaTable.getSelectedRow();
                txtId.setText(adminTableModel.getValueAt(selectedRow, 0).toString());
                txtNombre.setText(adminTableModel.getValueAt(selectedRow, 1).toString());
                txtContenido.setText(adminTableModel.getValueAt(selectedRow, 2).toString());
                txtCategoriaAdmin.setText(adminTableModel.getValueAt(selectedRow, 3).toString());
            }
        });

        // --- Manejadores de Eventos para la Pestaña de Cliente ---
        btnFiltrar.addActionListener(e -> filterCapsulasByCategoria());
        btnVerDetalles.addActionListener(e -> viewCapsulaDetails());
        btnSistemaFeedback.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Feedback (REQ_05) - No implementada."));
        btnPlanesPago.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Planes de Pago (REQ_06) - No implementada."));
        btnDescargarContenido.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Descarga (REQ_07) - No implementada."));
        btnSolicitarEnvio.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Solicitar Envío (REQ_08) - No implementada."));

        clientCapsulaTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && clientCapsulaTable.getSelectedRow() != -1) {
                // Aquí podrías cargar los detalles de la cápsula seleccionada si es necesario
            }
        });
    }

    // --- Métodos de Lógica para la Pestaña de Administración ---
    private void loadAdminCapsulasIntoTable() {
        adminTableModel.setRowCount(0);
        List<Capsula> capsulas = capsulaDAO.obtenerTodasLasCapsulas();
        for (Capsula capsula : capsulas) {
            Vector<Object> row = new Vector<>();
            row.add(capsula.getId());
            row.add(capsula.getNombre());
            row.add(capsula.getContenido());
            row.add(capsula.getCategoria());
            adminTableModel.addRow(row);
        }
    }

    private void crearCapsula() {
        try {
            String nombre = txtNombre.getText();
            String contenido = txtContenido.getText();
            String categoria = txtCategoriaAdmin.getText();

            if (nombre.isEmpty() || contenido.isEmpty() || categoria.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Capsula nuevaCapsula = new Capsula(nombre, contenido, categoria);
            capsulaDAO.crearCapsula(nuevaCapsula);
            JOptionPane.showMessageDialog(this, "Cápsula creada exitosamente.");
            loadAdminCapsulasIntoTable(); // Recargar tabla de admin
            loadClientCapsulasIntoTable(); // Recargar tabla de cliente para ver el nuevo dato
            clearAdminFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear cápsula: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void actualizarCapsula() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText();
            String contenido = txtContenido.getText();
            String categoria = txtCategoriaAdmin.getText();

            if (nombre.isEmpty() || contenido.isEmpty() || categoria.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Capsula capsulaActualizada = new Capsula(id, nombre, contenido, categoria);
            capsulaDAO.actualizarCapsula(capsulaActualizada);
            JOptionPane.showMessageDialog(this, "Cápsula actualizada exitosamente.");
            loadAdminCapsulasIntoTable(); // Recargar tabla de admin
            loadClientCapsulasIntoTable(); // Recargar tabla de cliente
            clearAdminFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Seleccione una cápsula o ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar cápsula: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void eliminarCapsula() {
        try {
            int id = Integer.parseInt(txtId.getText());
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar esta cápsula?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                capsulaDAO.eliminarCapsula(id);
                JOptionPane.showMessageDialog(this, "Cápsula eliminada exitosamente.");
                loadAdminCapsulasIntoTable();
                loadClientCapsulasIntoTable();
                clearAdminFields();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Seleccione una cápsula o ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar cápsula: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearAdminFields() {
        txtId.setText("");
        txtNombre.setText("");
        txtContenido.setText("");
        txtCategoriaAdmin.setText("");
        adminCapsulaTable.clearSelection();
    }

    // --- Métodos de Lógica para la Pestaña de Cliente ---
    private void loadClientCapsulasIntoTable() {
        clientTableModel.setRowCount(0);
        List<Capsula> capsulas = capsulaDAO.obtenerTodasLasCapsulas();
        for (Capsula capsula : capsulas) {
            Vector<Object> row = new Vector<>();
            row.add(capsula.getId());
            row.add(capsula.getNombre());
            row.add(capsula.getContenido());
            row.add(capsula.getCategoria());
            clientTableModel.addRow(row);
        }
    }

    private void filterCapsulasByCategoria() {
        String categoria = txtCategoriaClienteFiltro.getText().trim();
        clientTableModel.setRowCount(0);
        List<Capsula> capsulas;
        if (categoria.isEmpty()) {
            capsulas = capsulaDAO.obtenerTodasLasCapsulas();
        } else {
            capsulas = capsulaDAO.obtenerCapsulasPorCategoria(categoria);
        }

        for (Capsula capsula : capsulas) {
            Vector<Object> row = new Vector<>();
            row.add(capsula.getId());
            row.add(capsula.getNombre());
            row.add(capsula.getContenido());
            row.add(capsula.getCategoria());
            clientTableModel.addRow(row);
        }
    }

    private void viewCapsulaDetails() {
        int selectedRow = clientCapsulaTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una cápsula para ver los detalles.", "Selección Requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int capsulaId = (int) clientTableModel.getValueAt(selectedRow, 0);
        Optional<Capsula> capsulaOptional = capsulaDAO.obtenerCapsulaPorId(capsulaId);

        if (capsulaOptional.isPresent()) {
            Capsula capsula = capsulaOptional.get();
            String details = "ID: " + capsula.getId() + "\n" +
                             "Nombre: " + capsula.getNombre() + "\n" +
                             "Contenido: " + capsula.getContenido() + "\n" +
                             "Categoría: " + capsula.getCategoria();
            JOptionPane.showMessageDialog(this, details, "Detalles de la Cápsula", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron detalles para la cápsula seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}