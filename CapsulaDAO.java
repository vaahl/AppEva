// Archivo: com/capsulas/model/CapsulaDAO.java
package com.capsulas.model;

import java.util.List;
import java.util.Optional;

public interface CapsulaDAO {
    void crearCapsula(Capsula capsula);
    void actualizarCapsula(Capsula capsula);
    void eliminarCapsula(int id);
    Optional<Capsula> obtenerCapsulaPorId(int id);
    List<Capsula> obtenerTodasLasCapsulas();
    List<Capsula> obtenerCapsulasPorCategoria(String categoria);
}