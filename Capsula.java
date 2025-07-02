// Archivo: com/capsulas/model/Capsula.java
package com.capsulas.model;

public class Capsula {
    private int id;
    private String nombre;
    private String contenido;
    private String categoria;

    public Capsula() {}

    public Capsula(int id, String nombre, String contenido, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.contenido = contenido;
        this.categoria = categoria;
    }

    public Capsula(String nombre, String contenido, String categoria) {
        this.nombre = nombre;
        this.contenido = contenido;
        this.categoria = categoria;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return "Capsula [ID=" + id + ", Nombre='" + nombre + "', Categoria='" + categoria + "']";
    }
}