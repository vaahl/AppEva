package com.capsulas.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Statement; 

/**
 *
 * @author Ceduc
 */
public class DatabaseManager {

    private final String BD = "capsulasdb";
    private final String URL = "jdbc:mysql://localhost:3306/" + BD;
    private final String user = "root";
    private final String password = "";
    private final String driver = "com.mysql.cj.jdbc.Driver";
    private Connection con;

    public Connection conectar() {
        try {
            Class.forName(driver);
            con = (Connection) DriverManager.getConnection(this.URL, this.user, this.password);
            System.out.println("SE CONECTO A BD " + BD);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("NO SE CONECTO A BD " + BD);
            // Es buena práctica relanzar la excepción o manejarla de forma más robusta
            // para que la aplicación sepa que la conexión falló.
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, "Error al conectar a la base de datos", ex);
        }
        return con;
    }

    //SOLUCIONAR PROBLEMA DE CONEXION CON BASE DE DATOS
    
    /*public void desconectar() {
        try {
            if (con != null && !con.isClosed()) { // Verificar si la conexión no es nula y no está cerrada
                con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
*/
    // --- Método corregido para crear la tabla si no existe ---
    public void createTablesIfNotExist() {
        // SQL para crear la tabla CAPSULAS en MySQL
        String createTableSQL = "CREATE TABLE IF NOT EXISTS CAPSULAS (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY," + // AUTO_INCREMENT para MySQL
                                "nombre VARCHAR(255) NOT NULL," +
                                "contenido TEXT NOT NULL," +
                                "categoria VARCHAR(100) NOT NULL" +
                                ");";
        try (Connection connection = conectar(); // Obtener la conexión
             Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Tabla 'CAPSULAS' verificada/creada exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear o verificar la tabla 'CAPSULAS': " + e.getMessage());
            e.printStackTrace();
            // Puedes lanzar una RuntimeException si fallar la creación de la tabla es un error fatal
            throw new RuntimeException("Fallo al inicializar la base de datos.", e);
        }
        // No es necesario desconectar aquí, el try-with-resources se encarga de cerrar la conexión
        // y el MainApp puede manejar la desconexión al finalizar.
    }

    
    //SOLUCIONAR PROBLEMA BASE DE DATOS
    public static void main(String[] args) {
        DatabaseManager cn = new DatabaseManager();
        try {
            Connection testCon = cn.conectar();
            if (testCon != null) {
                System.out.println("Conexión de prueba exitosa.");
                testCon.close(); // Cerrar la conexión de prueba
            }
        } catch (SQLException e) {
            System.err.println("Fallo la conexión de prueba: " + e.getMessage());
        }
        // cn.createTablesIfNotExist(); // Se puede probar aquí
    }
}

/*
    public static void main(String[] args) {
        DatabaseManager cn = new DatabaseManager();
        cn.conectar();
        // Opcional: Probar la creación de tablas aquí también para depuración
        // cn.createTablesIfNotExist();
        cn.desconectar();
    }
}
*/