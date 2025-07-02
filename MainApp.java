package com.capsulas.model;

import javax.swing.SwingUtilities;



public class MainApp {
    public static void main(String[] args) {
        
      // Crear una instancia de DatabaseManager para inicializar la base de datos
        DatabaseManager dbManager = new DatabaseManager();
        dbManager.createTablesIfNotExist(); // Se asegura de que la tabla CAPSULAS exista

        // Lanzar la interfaz unificada de gestión de cápsulas
        SwingUtilities.invokeLater(() -> {
            UserCapsula unifiedFrame = new UserCapsula();
            unifiedFrame.setVisible(true);
        });
    }
}