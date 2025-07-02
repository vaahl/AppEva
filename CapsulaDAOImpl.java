package com.capsulas.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CapsulaDAOImpl implements CapsulaDAO {

    // Crear una única instancia de DatabaseManager para toda la clase
    private DatabaseManager dbManager = new DatabaseManager();

    @Override
    public void crearCapsula(Capsula capsula) {
        String sql = "INSERT INTO CAPSULAS (NOMBRE, CONTENIDO, CATEGORIA) VALUES (?, ?, ?)";
        try (Connection conn = dbManager.conectar(); // Usa la instancia única
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, capsula.getNombre());
            pstmt.setString(2, capsula.getContenido());
            pstmt.setString(3, capsula.getCategoria());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        capsula.setId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Cápsula creada con ID: " + capsula.getId());
            }

        } catch (SQLException e) {
            System.err.println("Error al crear cápsula: " + e.getMessage());
            e.printStackTrace();
            // Es buena práctica lanzar una excepción personalizada o RuntimeException aquí
            // para que la UI pueda manejar el error de forma más robusta.
        }
        // ELIMINAR el bloque finally { dbManager.desconectar(); }
    }

    @Override
    public void actualizarCapsula(Capsula capsula) {
        String sql = "UPDATE CAPSULAS SET NOMBRE = ?, CONTENIDO = ?, CATEGORIA = ? WHERE ID = ?";
        try (Connection conn = dbManager.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, capsula.getNombre());
            pstmt.setString(2, capsula.getContenido());
            pstmt.setString(3, capsula.getCategoria());
            pstmt.setInt(4, capsula.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Cápsula con ID " + capsula.getId() + " actualizada.");
            } else {
                System.out.println("Cápsula con ID " + capsula.getId() + " no encontrada para actualizar.");
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar cápsula: " + e.getMessage());
            e.printStackTrace();
        }
        // ELIMINAR el bloque finally { dbManager.desconectar(); }
    }

    @Override
    public void eliminarCapsula(int id) {
        String sql = "DELETE FROM CAPSULAS WHERE ID = ?";
        try (Connection conn = dbManager.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Cápsula con ID " + id + " eliminada.");
            } else {
                System.out.println("Cápsula con ID " + id + " no encontrada para eliminar.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar cápsula: " + e.getMessage());
            e.printStackTrace();
        }
        // ELIMINAR el bloque finally { dbManager.desconectar(); }
    }

    @Override
    public Optional<Capsula> obtenerCapsulaPorId(int id) {
        String sql = "SELECT ID, NOMBRE, CONTENIDO, CATEGORIA FROM CAPSULAS WHERE ID = ?";
        try (Connection conn = dbManager.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Capsula(
                        rs.getInt("ID"),
                        rs.getString("NOMBRE"),
                        rs.getString("CONTENIDO"),
                        rs.getString("CATEGORIA")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cápsula por ID: " + e.getMessage());
            e.printStackTrace();
        }
        // ELIMINAR el bloque finally { dbManager.desconectar(); }
        return Optional.empty();
    }

    @Override
    public List<Capsula> obtenerTodasLasCapsulas() {
        List<Capsula> capsulas = new ArrayList<>();
        String sql = "SELECT ID, NOMBRE, CONTENIDO, CATEGORIA FROM CAPSULAS";
        try (Connection conn = dbManager.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                capsulas.add(new Capsula(
                    rs.getInt("ID"),
                    rs.getString("NOMBRE"),
                    rs.getString("CONTENIDO"),
                    rs.getString("CATEGORIA")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las cápsulas: " + e.getMessage());
            e.printStackTrace();
        }
        // ELIMINAR el bloque finally { dbManager.desconectar(); }
        return capsulas;
    }

    @Override
    public List<Capsula> obtenerCapsulasPorCategoria(String categoria) {
        List<Capsula> capsulas = new ArrayList<>();
        String sql = "SELECT ID, NOMBRE, CONTENIDO, CATEGORIA FROM CAPSULAS WHERE CATEGORIA = ?";
        try (Connection conn = dbManager.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categoria);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    capsulas.add(new Capsula(
                        rs.getInt("ID"),
                        rs.getString("NOMBRE"),
                        rs.getString("CONTENIDO"),
                        rs.getString("CATEGORIA")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cápsulas por categoría: " + e.getMessage());
            e.printStackTrace();
        }
        // ELIMINAR el bloque finally { dbManager.desconectar(); }
        return capsulas;
    }
}