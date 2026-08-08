package mx.edu.utez.DesarrolloAcademico.model;

import java.sql.Date;

public class Periodo {
    private int id;
    private String division;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean activo;

    public Periodo() {}

    public Periodo(String division, Date fechaInicio, Date fechaFin, boolean activo) {
        this.division = division;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
    }

    public Periodo(int id, String division, Date fechaInicio, Date fechaFin, boolean activo) {
        this.id = id;
        this.division = division;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}